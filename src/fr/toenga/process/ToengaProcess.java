package fr.toenga.process;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import fr.toenga.script.ToengaScript;
import fr.toenga.script.ToengaEnvironment;

public class ToengaProcess
{
	/*private Process handler;
	
	private ToengaEnvironment environment;
	private ToengaScript scriptBeforeStart;
	private ToengaScript scriptAfterStop;
	private String startProcessCommand;
	
	public void start() throws IOException
	{
		if(handler != null)
			throw new IllegalStateException("Process already started!");
		
		scriptBeforeStart.execute(environment);
		
		ProcessBuilder builder = new ProcessBuilder( environment.parseCommand(startProcessCommand) );
		handler = builder.start();
	
		new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					Process process = handler.onExit().get();
					
					if(process.exitValue() != 0)
						System.out.println(process.exitValue());
					
					scriptAfterStop.execute(environment);
				}
				catch (InterruptedException | ExecutionException e)
				{
					e.printStackTrace();
				}
			}
		};
	}*/
}
