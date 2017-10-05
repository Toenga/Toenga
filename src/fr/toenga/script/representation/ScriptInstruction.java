package fr.toenga.script.representation;

import fr.toenga.script.core.MyObject;
import lombok.Getter;

public abstract class ScriptInstruction
{
	@Getter
	private ScriptPosition position;
	
	/*public ScriptInstruction(ScriptPosition position)
	{
		this.position = position;
	}*/
	
	public abstract MyObject execute();
}
