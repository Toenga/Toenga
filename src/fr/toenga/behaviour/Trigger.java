package fr.toenga.behaviour;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum Trigger
{
	
    CREATE("CREATE", 0), 
    BEFORE_START("BEFORE_START", 1), 
    AFTER_START("AFTER_START", 2), 
    BEFORE_STOP("BEFORE_STOP", 3), 
    AFTER_STOP("AFTER_STOP", 4), 
    DESTROY("DESTROY", 5);
    
	@Setter private String	name;
	@Setter private int		id;
	
    private Trigger(String name, int id)
    {
    	setName(name);
    	setId(id);
    }
    
}
