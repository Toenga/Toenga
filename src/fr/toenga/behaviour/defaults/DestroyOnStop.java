
package fr.toenga.behaviour.defaults;

import fr.toenga.process.ToengaProcess;
import fr.toenga.behaviour.Trigger;
import fr.toenga.behaviour.Behaviour;

public class DestroyOnStop extends Behaviour
{
	
    public DestroyOnStop()
    {
        super(Trigger.AFTER_STOP);
    }
    
    @Override
    public void execute(ToengaProcess process, String[] args)
    {
        process.destroy();
    }
    
}