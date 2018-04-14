package fr.toenga.plugins;

import java.io.File;

import fr.toenga.Toenga;
import lombok.Getter;

public class Plugin
{
	
	@Getter private PluginInfo pluginInfo;

	public void onLoad()
	{

	}
	
	public void onEnable()
	{

	}
	public void onDisable()
	{

	}

	public Toenga getToenga()
	{
		return Toenga.getInstance();
	}

	public final File getDataFolder()
	{
		return new File("plugins" + File.separator + pluginInfo.getName());
	}

	@Override
	public final String toString()
	{
		return getPluginInfo().toString();
	}

	@Override
	public final boolean equals(Object o)
	{
		if (o instanceof Plugin)
		{
			Plugin plugin = (Plugin) o;
			return plugin.getPluginInfo().equals(getPluginInfo());
		}
		return false;
	}

	protected final void initPlugin(PluginInfo info)
	{
		this.pluginInfo = info;
		if (!getDataFolder().exists())
		{
			getDataFolder().mkdirs();
		}
	}
	
}
