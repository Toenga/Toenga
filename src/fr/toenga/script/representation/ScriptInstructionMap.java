package fr.toenga.script.representation;

import java.util.Map;
import java.util.Map.Entry;

import fr.toenga.script.core.MyObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ScriptInstructionMap extends ScriptInstruction
{
	private Map<ScriptInstruction, ScriptInstruction> map;
	
	@Override
	public MyObject execute()
	{
		return null;
	}

	@Override
	public String toString()
	{
		String s = "{";
		boolean first = true;
		
		for(Entry<ScriptInstruction, ScriptInstruction> entries : map.entrySet())
		{
			if(first)
				first = false;
			else s += ", ";
			
			s += entries.getKey() + " = " + entries.getValue();
		}
		
		return s + "}";
	}
}
