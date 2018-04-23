package fr.toenga;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import fr.toenga.common.tech.rabbitmq.RabbitService;
import fr.toenga.common.tech.redis.RedisService;
import fr.toenga.common.utils.general.FileUtils;
import fr.toenga.common.utils.general.JsonUtils;
import fr.toenga.config.ProcessConfiguration;
import fr.toenga.config.ToengaConfiguration;
import fr.toenga.config.ToengaConfiguration.GitConfiguration.Locations.ModelFolder;
import fr.toenga.config.ToengaConfiguration.GitConfiguration.Repository;
import fr.toenga.config.ToengaWebSocketServerConfiguration;
import fr.toenga.packets.createprocess.ToengaCreateProcessAction;
import fr.toenga.plugins.PluginManager;
import fr.toenga.process.ProcessModel;
import fr.toenga.process.ProcessModelMap;
import fr.toenga.process.ToengaProcess;
import fr.toenga.rabbit.RabbitActionListener;
import fr.toenga.socketserver.ToengaSocketServer;
import fr.toenga.utils.ToengaUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = false)
@Data
public class Toenga
{

	public final static File 			pluginFolder;
	public static final File			dataFolder;
	public static final File			processFolder;
	@Getter private static final Toenga	instance;

	private ToengaConfiguration			configuration;
	private Map<String, String>			globalVars;
	private Map<UUID, ToengaProcess>	processList;
	
	private PluginManager				pluginManager;

	private RedisService				redisService;
	private RabbitService				rabbitService;
	private ToengaSocketServer			socketServer;

	static
	{
		pluginFolder = new File("plugins");
		dataFolder = new File("data");
		processFolder = new File("process");
		instance = new Toenga();
	}

	public Toenga()
	{
		setGlobalVars(new HashMap<>());
		setProcessList(new HashMap<>());

		if (!processFolder.exists())
		{
			processFolder.mkdirs();
		}

		this.reloadConfiguration();
		this.getGlobalVars().put("dir/data", dataFolder.getAbsolutePath());

		File[] fileList = processFolder.listFiles();
		int length = fileList.length;
		for (int i = 0; i < length; i++)
		{
			File file = fileList[i];
			ToengaProcess process = new ToengaProcess(file);
			if (!process.isDestroyed())
			{
				processList.put(process.getUniqueId(), process);
			}
		}
		
		enablePlugins();
	}

	public void reloadConfiguration()
	{
		File config = new File("config.json");
		setConfiguration(JsonUtils.load(config, ToengaConfiguration.class));

		if (getRedisService() != null && !this.getRedisService().isDead())
		{
			getRedisService().remove();
		}
		
		if (getRabbitService() != null && !this.getRabbitService().isDead())
		{
			getRabbitService().remove();
		}

		if (getSocketServer() != null)
		{
			try
			{
				getSocketServer().stop();
			}
			catch (Exception exception)
			{
				exception.printStackTrace();
			}
		}

		try
		{
			ToengaWebSocketServerConfiguration wsConfig = getConfiguration().getWebSocketServer();
			setSocketServer(new ToengaSocketServer(wsConfig.getPort(), wsConfig.getEncryptionKey(), wsConfig.getWhitelist()));
			getSocketServer().start();
		}
		catch (Exception error)
		{
			error.printStackTrace();
		}

		this.setRedisService(new RedisService("mainRedis", getConfiguration().getRedisConfig()));
		this.setRabbitService(new RabbitService("mainRabbit", getConfiguration().getRabbitConfig()));
		this.pullOrClone();
		new RabbitActionListener(getRabbitService(), getConfiguration().getToengaName());
	}
	
	private void enablePlugins()
	{
		pluginFolder.mkdir();

		setPluginManager(new PluginManager());
		getPluginManager().detectPlugins(pluginFolder);

		getPluginManager().loadPlugins();
		getPluginManager().enablePlugins();
	}

	private void pullOrClone()
	{
		for (Repository repository : getConfiguration().getToengaData().getRepositories())
		{
			ToengaUtils.pullOrClone(getConfiguration(), repository);
		}

		File config = new File("config.json");
		File nConfig = new File(dataFolder, getConfiguration().getToengaData().getLocations().getToengaConfig());

		try
		{
			if (nConfig.exists() && !FileUtils.contentEquals(nConfig, config))
			{
				FileUtils.copyFile(nConfig, config);
				setConfiguration(JsonUtils.load(config, ToengaConfiguration.class));
			}
		}
		catch (Exception error)
		{
			error.printStackTrace();
		}

		Map<String, ProcessConfiguration> configs = new HashMap<>();
		for (ModelFolder modelFolder : getConfiguration().getToengaData().getLocations().getModels())
		{
			File models = new File(dataFolder, modelFolder.getFolder());
			if (!models.exists())
			{
				continue;
			}
			if (!models.isDirectory())
			{
				continue;
			}
			for (File model : models.listFiles())
			{
				if (model.isDirectory())
				{
					continue;
				}
				if (model.getName().endsWith("json"))
				{
					ProcessConfiguration conf = JsonUtils.load(model, ProcessConfiguration.class);
					conf.setFolder(modelFolder);
					if (conf.getName() != null)
					{
						configs.put(conf.getName(), conf);
					}
				}
			}
		}
		
		ProcessModelMap.getInstance().reloadModels(configs);
		JsonUtils.save(config, getConfiguration(), true);
	}

    
    public ToengaProcess createProcess(ToengaCreateProcessAction packet)
    {
        ToengaProcess process = this.createProcess(packet.getModelName(), packet.getStaticId());
        process.getMetadata().getKeys().putAll(packet.getVariables());
        return process;
    }
    
    public ToengaProcess createProcess(String modelName)
    {
        return createProcess(modelName, null);
    }
    
    public ToengaProcess createProcess(String modelName, String fixedId)
    {
        ProcessModel model = ProcessModelMap.getInstance().getModel(modelName);
        assert model != null : "Unknown model " + modelName;
        return createProcess(model, fixedId);
    }
    
    public ToengaProcess createProcess(ProcessModel model)
    {
        return createProcess(model, null);
    }
    
    public ToengaProcess createProcess(ProcessModel model, final String fixedId)
    {
        assert model != null;
        final ToengaProcess process = ToengaProcess.createProcess(model, fixedId);
        if (process == null)
        {
            return null;
        }
        getProcessList().put(process.getUniqueId(), process);
        return process;
    }

    public void shutdownAllProcess()
    {
        for (ToengaProcess process : getProcessList().values())
        {
            process.stop();
        }
    }
    
    public ToengaProcess getProcess(UUID uniqueId)
    {
        return getProcessList().get(uniqueId);
    }
    
}