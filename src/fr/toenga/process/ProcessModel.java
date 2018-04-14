package fr.toenga.process;

import java.util.List;
import java.util.Map;

import fr.toenga.behaviour.BehaviourCaller;
import fr.toenga.behaviour.Trigger;
import lombok.Data;

@Data
public class ProcessModel
{
	
    public String								name;
    public Map<Trigger, List<BehaviourCaller>>	behaviours;
    public int									defaultWeight;
    public String								startCommand;
    
}
