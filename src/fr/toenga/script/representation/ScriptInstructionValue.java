package fr.toenga.script.representation;

import fr.toenga.script.core.MyObject;
import fr.toenga.script.parser.Operator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ScriptInstructionValue extends ScriptInstruction
{
	private MyObject value;
	private Operator modifier;
	
	@Override
	public MyObject execute() {
		if(modifier != null)
		{
			//FIXME
		}
		
		return value;
	}
	
	@Override
	public String toString()
	{
		return (modifier == null ? "" : modifier.getValue()) + value.toString();
	}
}
