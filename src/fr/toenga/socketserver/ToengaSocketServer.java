package fr.toenga.socketserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.toenga.Toenga;
import fr.toenga.common.utils.general.GsonUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class ToengaSocketServer
{

	private ServerSocket server;
	private List<Socket> sockets;

	public ToengaSocketServer(ServerSocket server, SocketAddress address) throws IOException
	{
		setSockets(new CopyOnWriteArrayList<Socket>());
		(this.server = server).bind(address);
		new Thread(() -> this.waitEntry()).start();
	}

	public int socketCount()
	{
		return getSockets().size();
	}

	public void close()
	{
		for (Socket socket : getSockets())
		{
			try
			{
				socket.close();
			}
			catch (IOException exception)
			{
				exception.printStackTrace();
			}
		}
		try
		{
			getServer().close();
		}
		catch (IOException exception)
		{
			exception.printStackTrace();
		}
	}

	private void waitEntry()
	{
		try
		{
			Socket socket;
			while ((socket = getServer().accept()) != null)
			{
				this.accept(socket);
			}
		}
		catch (IOException exception)
		{
			exception.printStackTrace();
		}
	}

	public void accept(Socket socket)
	{
		new Thread(() -> this.listen(socket)).start();
	}

	private void listen(Socket socket)
	{
		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			String line = reader.readLine();
			try
			{
				TokenInfo info = GsonUtils.getGson().fromJson(line, TokenInfo.class);
				info.setSocket(socket);
				info.call(Toenga.getInstance().getRedisService());
			}
			catch (Exception exception)
			{
				exception.printStackTrace();
				System.out.print("Sended: '" + line + "'");
			}
		}
		catch (IOException exception)
		{
			exception.printStackTrace();
		}
	}

}
