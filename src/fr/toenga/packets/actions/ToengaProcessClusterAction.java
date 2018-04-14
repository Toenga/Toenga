package fr.toenga.packets.actions;

import java.util.UUID;

import fr.toenga.Toenga;
import fr.toenga.process.ToengaProcess;

public class ToengaProcessClusterAction extends ToengaClusterAction
{

	private UUID			serverId;
	private ProcessAction	action;

	@Override
	public void execute()
	{
		ToengaProcess process = Toenga.getInstance().getProcess(serverId);
		action.getDefaultExecutor().execute(process);
	}

}
