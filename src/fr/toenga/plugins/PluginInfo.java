package fr.toenga.plugins;

import java.io.File;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PluginInfo
{
	
	private String name, version, mainClass;
	private List<String> authors, depends, softDepends;
	private transient File jarFile;

	@Override
	public String toString()
	{
		return name;
	}

	@Override
	public final boolean equals(Object o)
	{
		if(o instanceof PluginInfo)
		{
			PluginInfo pluginInfo = (PluginInfo) o;
			return pluginInfo.getName().equalsIgnoreCase(getName());
		}
		return false;
	}
	
}