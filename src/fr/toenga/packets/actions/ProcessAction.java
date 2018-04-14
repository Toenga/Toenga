package fr.toenga.packets.actions;

import fr.toenga.packets.actions.executors.ProcessActionDestroyExecutor;
import fr.toenga.packets.actions.executors.ProcessActionRestartExecutor;
import fr.toenga.packets.actions.executors.ProcessActionStartExecutor;
import fr.toenga.packets.actions.executors.ProcessActionStopExecutor;
import lombok.Getter;
import lombok.Setter;

@Getter
public enum ProcessAction
{

	START		("START", 0, new ProcessActionDestroyExecutor()), 
	STOP		("STOP", 1, new ProcessActionRestartExecutor()), 
	RESTART		("RESTART", 2, new ProcessActionStartExecutor()), 
	DESTROY		("DESTROY", 3, new ProcessActionStopExecutor());

	@Setter private String					name;
	@Setter private int						id;
	@Setter private ProcessActionExecutor	defaultExecutor;

	private ProcessAction(String name, int id, ProcessActionExecutor defaultExecutor)
	{
		setName(name);
		setId(id);
		setDefaultExecutor(defaultExecutor);
	}

}