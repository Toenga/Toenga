package fr.toenga.packets.createprocess;

import java.util.Map;

import fr.toenga.Toenga;
import fr.toenga.packets.actions.ToengaClusterAction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class ToengaCreateProcessAction extends ToengaClusterAction
{
	
    private String					modelName;
    private String					staticId;
    private Map<String, String>		variables;
    
	@Override
	public void execute()
	{
		Toenga.getInstance().createProcess(this);
	}
    
}
