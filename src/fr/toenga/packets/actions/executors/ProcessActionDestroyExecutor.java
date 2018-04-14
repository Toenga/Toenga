package fr.toenga.packets.actions.executors;

import fr.toenga.packets.actions.ProcessActionExecutor;
import fr.toenga.process.ToengaProcess;

public class ProcessActionDestroyExecutor extends ProcessActionExecutor
{

	@Override
	public void execute(ToengaProcess toengaProcess)
	{
		try
		{
			toengaProcess.stop();
			Thread.sleep(5000);
			toengaProcess.start();
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
	}

}
