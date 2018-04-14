package fr.toenga.process;

import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class ToengaProcessMetadata
{
	
    private String				model;
    private String				staticId;
    private String				currentId;
    public Map<String, String>	keys;
    private int					maxWeight;
    private boolean				running;
    
}
