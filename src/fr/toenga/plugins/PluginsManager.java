package fr.toenga.plugins;

import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;

import fr.toenga.common.utils.general.GsonUtils;
import fr.toenga.plugins.events.Event;
import fr.toenga.plugins.events.Listener;

public class PluginsManager
{

	private Map<String, Plugin> plugins;
	private Map<String, PluginInfo> toLoad;

	private Map<Plugin, List<Listener>> listenersByPlugin;

	private EventDispatcher dispatcher;

	public PluginsManager()
	{
		plugins = new LinkedHashMap<String, Plugin>();
		toLoad = new HashMap<String, PluginInfo>();

		listenersByPlugin = new HashMap<Plugin, List<Listener>>();

		dispatcher = new EventDispatcher();
	}

	public PluginInfo detectPlugin(File jarFile)
	{
		String jarName = jarFile.getName().toLowerCase();

		if (jarFile.isDirectory() || !jarName.endsWith(".jar"))
		{
			return null;
		}

		try
		{
			JarFile jar = new JarFile(jarFile);
			JarEntry pluginYml = jar.getJarEntry("ladder.yml");
			if (pluginYml == null)
			{
				pluginYml = jar.getJarEntry("plugin.yml");
			}

			if (pluginYml == null)
			{
				jar.close();
				throw new RuntimeException("JAR File " + jarFile.getName() + " doesn't contains a configuration file (ladder.yml or plugin.yml)! Could not load the plugin.");
			}

			String content = CharStreams.toString(new InputStreamReader(jar.getInputStream(pluginYml), Charsets.UTF_8));
			jar.close();

			PluginInfo config = GsonUtils.getGson().fromJson(content, PluginInfo.class);

			if (config.getAuthors() == null) config.setAuthors(new ArrayList<>());
			if (config.getAuthors().isEmpty()) config.getAuthors().add("An Unnamed Author");
			if (config.getMainClass() == null) throw new RuntimeException("Invalid " + pluginYml.getName() + " for " + jarFile.getName() + " : plugin.yml don't specify main class!");
			if (config.getName() == null) throw new RuntimeException("Invalid " + pluginYml.getName() + " for " + jarFile.getName() + " : plugin.yml don't specify plugin name !");
			if (config.getVersion() == null) config.setVersion("Unknown version");

			String lowerName = config.getName().toLowerCase();
			if (toLoad.containsKey(lowerName) || plugins.containsKey(lowerName))
			{
				throw new RuntimeException("Plugin '" + config.getName() + "'already loaded (" + jarFile + ")");
			}

			return new PluginInfo(config.getName(), config.getVersion(), config.getMainClass(), config.getAuthors(), config.getDepends(), config.getSoftDepends(), jarFile);
		}
		catch(Throwable t)
		{
			t.printStackTrace();
		}
		return null;
	}

	public void detectPlugins(File folder)
	{
		if (!folder.exists())
		{
			folder.mkdirs();
		}
		if (!folder.isDirectory())
		{
			return;
		}

		for (File file : folder.listFiles())
		{
			PluginInfo pi = detectPlugin(file);
			if(pi != null)
			{
				toLoad.put(pi.getName().toLowerCase(), pi);
			}
		}
	}

	public void loadPlugins()
	{
		for (PluginInfo plugin : toLoad.values())
		{
			try
			{
				loadPlugin(plugin);
			}
			catch(Throwable t)
			{
				System.out.println("Unable to load plugin " + plugin);
				if (t instanceof StackOverflowError)
				{
					System.out.println("Error is an StackOverflowError. Check if the plugin contains circular dependencies.");
				}
				else
				{
					t.printStackTrace();
				}
			}
		}
		toLoad.clear();
	}

	@SuppressWarnings("resource")
	public boolean loadPlugin(PluginInfo plugin) throws Throwable
	{
		if (getPlugin(plugin) != null)
		{
			return true;
		}
		if (plugin.getDepends() != null)
		{
			for (String depend : plugin.getDepends())
			{
				if (!loadPlugin(toLoad.get(depend.toLowerCase())))
				{
					throw new RuntimeException("Unknow dependency '" + depend + "' for " + plugin);
				}
			}
		}

		if (plugin.getSoftDepends() != null)
		{
			for (String softdepend : plugin.getSoftDepends())
			{
				loadPlugin(toLoad.get(softdepend.toLowerCase()));
			}
		}

		URLClassLoader loader = new PluginClassLoader(new URL[]{plugin.getJarFile().toURI().toURL()});
		Class<?> main = loader.loadClass(plugin.getMainClass());
		Plugin clazz = (Plugin) main.getDeclaredConstructor().newInstance();

		clazz.initPlugin(plugin);
		plugins.put(plugin.getName().toLowerCase(), clazz);
		clazz.onLoad();

		return true;
	}

	public void unloadPlugin(Plugin plugin)
	{
		unregisterAllEvents(plugin);

		while (plugins.values().remove(plugin));
	}

	public void enablePlugins()
	{
		for (Plugin plugin : plugins.values())
		{
			enablePlugin(plugin);
		}
	}

	public void enablePlugin(Plugin plugin)
	{
		try
		{
			plugin.onEnable();
			System.out.println(plugin.getPluginInfo() + " enabled !");
		}
		catch(Throwable t)
		{
			System.out.println("Unable to enable plugin " + plugin);
			t.printStackTrace();
		}
	}

	public void disablePlugin(Plugin plugin)
	{
		try
		{
			plugin.onDisable();
			System.out.println(plugin.getPluginInfo() + " disabled !");
		}
		catch(Throwable t)
		{
			System.out.println("Unable to disable plugin " + plugin);
			t.printStackTrace();
		}
	}

	public void disablePlugins()
	{
		for (Plugin plugin : plugins.values())
		{
			disablePlugin(plugin);
		}
	}

	public Plugin getPlugin(String name)
	{
		return plugins.get(name.toLowerCase());
	}

	public Plugin getPlugin(PluginInfo info)
	{
		return plugins.get(info.getName().toLowerCase());
	}

	public Collection<Plugin> getPlugins()
	{
		return Collections.unmodifiableCollection(plugins.values());
	}

	public void registerEvents(Plugin plugin, Listener listener)
	{
		dispatcher.registerListener(listener);

		if (!listenersByPlugin.containsKey(plugin))
		{
			listenersByPlugin.put(plugin, new ArrayList<Listener>());
		}
		listenersByPlugin.get(plugin).add(listener);
	}

	public void unregisterEvents(Plugin plugin, Listener listener)
	{
		List<Listener> listeners = listenersByPlugin.get(plugin);
		if (listeners != null)
		{
			listeners.remove(listener);
			dispatcher.unregisterListener(listener);
		}
	}

	public void unregisterAllEvents(Plugin plugin)
	{
		List<Listener> listeners = listenersByPlugin.get(plugin);
		for (Listener listener : listeners)
		{
			dispatcher.unregisterListener(listener);
		}

		listenersByPlugin.remove(plugin);
	}

	public <T extends Event> T dispatchEvent(T e)
	{
		dispatcher.dispatch(e);

		return e;
	}

}