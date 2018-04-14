
package fr.toenga.behaviour.defaults;

import java.io.IOException;
import fr.toenga.process.ToengaProcess;
import fr.toenga.behaviour.Trigger;
import fr.toenga.behaviour.Behaviour;

public class Commands extends Behaviour
{
	
    public Commands(Trigger trigger)
    {
        super(trigger);
    }
    
    @Override
    public void execute(ToengaProcess process, String[] args)
    {
        for (String command : args)
        {
            try
            {
                new ProcessBuilder(new String[] { "sh", "-c", command }).start().waitFor();
            }
            catch (InterruptedException | IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }
    
}