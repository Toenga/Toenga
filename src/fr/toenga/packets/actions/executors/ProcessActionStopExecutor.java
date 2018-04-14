package fr.toenga.packets.actions.executors;

import fr.toenga.packets.actions.ProcessActionExecutor;
import fr.toenga.process.ToengaProcess;

public class ProcessActionStopExecutor extends ProcessActionExecutor
{

	@Override
	public void execute(ToengaProcess toengaProcess)
	{
		toengaProcess.stop();
	}

}
