package fr.toenga.packets.actions.executors;

import java.io.IOException;

import fr.toenga.packets.actions.ProcessActionExecutor;
import fr.toenga.process.ToengaProcess;

public class ProcessActionStartExecutor extends ProcessActionExecutor
{

	@Override
	public void execute(ToengaProcess toengaProcess)
	{
		try
		{
			toengaProcess.start();
		}
		catch (IOException exception)
		{
			exception.printStackTrace();
		}
	}

}
