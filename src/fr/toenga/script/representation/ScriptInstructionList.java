package fr.toenga.script.representation;

import fr.toenga.script.core.MyObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ScriptInstructionList extends ScriptInstruction
{
	private ScriptInstruction[] values;
	private boolean tuple;
	
	@Override
	public MyObject execute()
	{
		return null;
	}
	
	@Override
	public String toString()
	{
		String s = tuple ? "(" : "[";
		boolean f = true;
		
		for(ScriptInstruction v : values)
		{
			if(!f) s += ", ";
			else f = false;
			
			s += v;
		}
		
		return s + (tuple ? ")" : "]");
	}
}
