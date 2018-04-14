
package fr.toenga.process;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.newsclub.net.unix.AFUNIXServerSocket;
import org.newsclub.net.unix.AFUNIXSocketAddress;

import fr.toenga.Toenga;
import fr.toenga.behaviour.BehaviourCaller;
import fr.toenga.behaviour.Trigger;
import fr.toenga.common.utils.general.FileUtils;
import fr.toenga.common.utils.general.JsonUtils;
import fr.toenga.utils.ToengaUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class ToengaProcess
{
	private static String			TOENGA_METADATA;
	private static String			TOENGA_SOCKET;

	private File					metadataFile;
	private File					processFolder;
	private ToengaProcessMetadata	metadata;
	private ProcessModel			model;
	private Process					process;
	private ProcessCyclicLog		logs;
	private List<ProcessSocket>		sockets;
	private UUID					uniqueId;
	private boolean					destroyed;

	static
	{
		ToengaProcess.TOENGA_METADATA = ".toenga.tmp";
		ToengaProcess.TOENGA_SOCKET = "toenga.sock";
	}

	public static ToengaProcess createProcess(ProcessModel model, String staticId)
	{
		File file;
		UUID uuid;
		do
		{
			uuid = UUID.randomUUID();
			file = new File(Toenga.processFolder, uuid.toString());
		}
		while (file.exists());
		return new ToengaProcess(model, uuid, file, staticId);
	}

	public ToengaProcess(final ProcessModel model, final UUID uniqueId, final File file, final String staticId)
	{
		setSockets(new ArrayList<ProcessSocket>());
		setDestroyed(false);
		file.mkdirs();
		setProcessFolder(file);
		setMetadataFile(new File(file, ToengaProcess.TOENGA_METADATA));
		setMetadata(new ToengaProcessMetadata());
		setModel(model);
		getMetadata().setMaxWeight(model.defaultWeight);
		getMetadata().setModel(model.name);
		getMetadata().setRunning(false);
		(getMetadata().keys = new HashMap<String, String>()).put("dir/process", getProcessFolder().getAbsolutePath());
		setUniqueId(uniqueId);

		saveMetadata();
		executeTrigger(Trigger.CREATE);
	}

	public ToengaProcess(final File file)
	{
		setSockets(new ArrayList<ProcessSocket>());
		setDestroyed(false);
		assert file.exists();
		setUniqueId(UUID.fromString(file.getName()));
		setProcessFolder(file);
		setMetadataFile(new File(file, ToengaProcess.TOENGA_METADATA));
		setMetadata(JsonUtils.load(getMetadataFile(), ToengaProcessMetadata.class));

		if (getMetadata() == null)
		{
			System.err.println("Detect process with an unknow model: no metadata. Running destroy().");
			this.destroy();
			return;
		}

		setModel(ProcessModelMap.getInstance().getModel(getMetadata().getModel()));
		if (getModel() == null)
		{
			System.err.println("Detect process with an unknow model: " + getMetadata().getModel() + ". Running destroy().");
			this.destroy();
			return;
		}
		if (getMetadata().isRunning())
		{
			System.err.println("Detect unstopped process. Running AfterStop trigger.");
			getMetadata().setRunning(false);
			this.executeTrigger(Trigger.AFTER_STOP);
		}
		if (file.exists())
		{
			this.saveMetadata();
		}
	}

	private void executeTrigger(Trigger trigger)
	{
		if (getModel() == null || !getModel().getBehaviours().containsKey(trigger))
		{
			return;
		}

		for (BehaviourCaller caller : getModel().getBehaviours().get(trigger))
		{
			caller.execute(this);
		}
	}

	public boolean isRunning()
	{
		return getProcess() != null && getProcess().isAlive();
	}

	public void start() throws IOException
	{
		assert !this.isRunning();
		executeTrigger(Trigger.BEFORE_START);
		getMetadata().setMaxWeight(getModel().getDefaultWeight());
		getMetadata().setRunning(true);
		setLogs(new ProcessCyclicLog());
		saveMetadata();
		ProcessBuilder builder = new ProcessBuilder(ToengaUtils.formatToengaStr(getModel().getStartCommand(), this).split(" "));
		builder.directory(getProcessFolder());
		setProcess(builder.start());
		getProcess().onExit().thenAccept(handle -> this.processStopped());
		new Thread(() -> this.readProcessOutput()).start();
		ProcessSocket socket = new ProcessSocket(this, AFUNIXServerSocket.newInstance(), new AFUNIXSocketAddress(new File(getProcessFolder(), ToengaProcess.TOENGA_SOCKET)));
		getSockets().add(socket);
		executeTrigger(Trigger.AFTER_START);
	}

	public void stop()
	{
		assert this.isRunning();
		executeTrigger(Trigger.BEFORE_STOP);
		for (ProcessSocket socket : getSockets())
		{
			socket.close();
		}
		getSockets().clear();
		getProcess().destroy();
	}

	private void processStopped()
	{
		setLogs(null);
		getMetadata().setRunning(false);
		saveMetadata();
		executeTrigger(Trigger.AFTER_STOP);
	}

	public void destroy()
	{
		setDestroyed(true);
		executeTrigger(Trigger.DESTROY);
		FileUtils.delete(getProcessFolder());
	}

	private void readProcessOutput()
	{
		int value = 0;
		try
		{
			final InputStream is = getProcess().getInputStream();
			while (getProcess().isAlive())
			{
				if ((value = is.read()) == -1)
				{
					break;
				}
				this.output(value);
			}
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}

	private void output(final int output)
	{
		getLogs().add(output);
		for (ProcessSocket socket : getSockets())
		{
			socket.output(output);
		}
	}

	public void input(int input)
	{
		assert this.isRunning();
		try
		{
			getProcess().getOutputStream().write(input);
			getProcess().getOutputStream().flush();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}

	public boolean canAcceptSocket() {
		return Toenga.getInstance().getConfiguration().isAllowParallelConsole() || getSockets().stream().mapToInt(socket -> socket.socketCount()).sum() == 0;
	}

	public void acceptSocket(Socket socket) throws IOException
	{
		if (getSockets().isEmpty() || !isRunning()) {
			socket.getOutputStream().write("This process isn't running!\n".getBytes());
			socket.close();
		}
		else
		{
			getSockets().get(0).accept(socket);
		}
	}

	public void saveMetadata()
	{
		JsonUtils.save(getMetadataFile(), getMetadata(), false);
	}

}
