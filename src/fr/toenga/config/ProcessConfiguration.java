package fr.toenga.config;

import fr.toenga.config.ToengaConfiguration.GitConfiguration.Locations.ModelFolder;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ProcessConfiguration
{
	
    private ModelFolder						folder;
    private String							parent;
    private String 							name;
    private ProcessConfigurationBehaviour[]	behaviours;
    private int								defaultWeight;
    private String							startCommand;
    
    @Data
    public class ProcessConfigurationBehaviour
    {
    	
    	private String		behaviour;
    	private String[]	arguments;
        
    }
    
}
