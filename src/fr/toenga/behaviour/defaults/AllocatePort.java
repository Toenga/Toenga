
package fr.toenga.behaviour.defaults;

import java.io.IOException;
import java.net.ServerSocket;
import fr.toenga.process.ToengaProcess;
import fr.toenga.behaviour.Trigger;
import fr.toenga.behaviour.Behaviour;

public class AllocatePort extends Behaviour
{
	
    public AllocatePort()
    {
        super(Trigger.BEFORE_START);
    }
    
    @Override
    public void execute(ToengaProcess process, String[] args)
    {
        int port = 0;
        
        try
        {
            ServerSocket server = new ServerSocket(0);
            port = server.getLocalPort();
            server.close();
        }
        catch (IOException e)
        {
            throw new IllegalStateException("Can't find a free port", e);
        }
        
        System.out.println("Allocate port " + port);
        process.getMetadata().getKeys().put("vars/port", Integer.toString(port));
    }
    
}