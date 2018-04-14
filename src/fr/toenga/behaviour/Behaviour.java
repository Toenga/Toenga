package fr.toenga.behaviour;

import java.util.HashMap;
import java.util.Map;

import fr.toenga.behaviour.defaults.AllocatePort;
import fr.toenga.behaviour.defaults.Commands;
import fr.toenga.behaviour.defaults.DestroyOnStop;
import fr.toenga.process.ToengaProcess;
import lombok.Data;

@Data
public abstract class Behaviour
{
	
    private static Map<String, Behaviour>	behaviours;
    private Trigger							trigger;
    
    static {
        behaviours = new HashMap<String, Behaviour>();
        registerBehaviour("trigger/start/allocate-port", new AllocatePort());
        registerBehaviour("trigger/stop/destroy", new DestroyOnStop());
        registerBehaviour("trigger/start/commands", new Commands(Trigger.BEFORE_START));
        registerBehaviour("trigger/stop/commands", new Commands(Trigger.AFTER_STOP));
        registerBehaviour("trigger/destroy/commands", new Commands(Trigger.DESTROY));
    }
    
    public static void registerBehaviour(String name, Behaviour behaviour)
    {
        Behaviour.behaviours.put(name, behaviour);
    }
    
    public static Behaviour getBehaviour(String name)
    {
        return Behaviour.behaviours.get(name);
    }
    
    public Behaviour(Trigger trigger)
    {
        setTrigger(trigger);
    }
    
    public abstract void execute(ToengaProcess p0, String[] p1);
    
}
