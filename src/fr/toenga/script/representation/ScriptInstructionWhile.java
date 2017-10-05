package fr.toenga.script.representation;

import fr.toenga.script.core.MyObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ScriptInstructionWhile extends ScriptInstruction
{
	private ScriptInstruction condition;
	private ScriptInstruction block;
	
	public MyObject execute()
	{
		MyObject value = null;
		
		while(condition.execute().toBool())
		{
			value = block.execute();
		}
		
		return value;
	}
	
	@Override
	public String toString()
	{
		return "while " + condition + " do " + block + " next";
	}
}