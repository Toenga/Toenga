package fr.toenga.packets.actions;

import lombok.Getter;
import lombok.Setter;

public abstract class ToengaClusterAction
{
	
	public abstract void execute();
	
	@Getter
    public enum Action
    {
		
        UPDATE_DATA("UPDATE_DATA", 0), 
        LIST_PROCESSES("LIST_PROCESSES", 1);
        
    	@Setter private String	name;
    	@Setter private int		id;
    	
        private Action(String name, int id)
        {
        	setName(name);
        	setId(id);
        }
        
    }
    
}