package fr.toenga.process;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.toenga.behaviour.BehaviourCaller;
import fr.toenga.behaviour.Trigger;
import fr.toenga.config.ProcessConfiguration;
import fr.toenga.config.ProcessConfiguration.ProcessConfigurationBehaviour;
import lombok.Data;
import lombok.Getter;

@Data
public class ProcessModelMap
{

	@Getter private static ProcessModelMap		instance;
	private Map<String, ProcessModel>			knownModels;
	private Map<String, ProcessModel>			newModels;
	private Map<String, ProcessConfiguration>	configs;

	static
	{
		instance = new ProcessModelMap();
	}

	public ProcessModelMap()
	{
		this.setKnownModels(new HashMap<String, ProcessModel>());
	}

	private ProcessModel recursivlyLoad(Set<String> known, ProcessConfiguration config)
	{
		known.add(config.getName());

		if (getNewModels().containsKey(config.getName()))
		{
			return getNewModels().get(config.getName());
		}

		ProcessModel model = getKnownModels().get(config.getName());

		if (model == null)
		{
			model = new ProcessModel();
		}

		model.setName(config.getName());
		model.setDefaultWeight(config.getDefaultWeight());
		model.setStartCommand(config.getStartCommand());

		if (config.getParent() != null)
		{
			if (!getConfigs().containsKey(config.getParent()))
			{
				System.err.println("Model " + model.name + " refer to unknow model " + config.getParent());
				return null;
			}

			if (known.contains(config.getParent()))
			{
				System.err.println("Recursive model inheritance detected! Can't load " + config.getName());
				return null;
			}

			if (recursivlyLoad(known, getConfigs().get(config.getParent())) == null)
			{
				System.err.println("Can't load " + config.getName() + ": parent can't be load.");
				return null;
			}

			model.setBehaviours(getNewModels().get(config.getParent()).getBehaviours());
		}
		else
		{
			model.setBehaviours(new HashMap<Trigger, List<BehaviourCaller>>());
		}

		for (ProcessConfigurationBehaviour processConfigurationBehaviour : config.getBehaviours())
		{
			BehaviourCaller caller = BehaviourCaller.createBehaviourCaller(config.getFolder(), processConfigurationBehaviour);
			Trigger trigger = caller.getBehaviour().getTrigger();
			if (!model.getBehaviours().containsKey(trigger))
			{
				model.getBehaviours().put(trigger, new LinkedList<BehaviourCaller>());
			}
			model.getBehaviours().get(trigger).add(caller);
		}
		
		getNewModels().put(config.getName(), model);
		return model;
	}

	public void reloadModels(Map<String, ProcessConfiguration> configurations)
	{
		this.setConfigs(configurations);
		this.setNewModels(new HashMap<String, ProcessModel>());
		
		for (ProcessConfiguration configuration : configurations.values())
		{
			this.recursivlyLoad(new HashSet<String>(), configuration);
		}
		
		this.setKnownModels(getNewModels());
		this.setNewModels(null);
	}

	public ProcessModel getModel(final String model)
	{
		return getKnownModels().get(model);
	}
	
}