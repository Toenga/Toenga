package fr.toenga.script.representation;

import fr.toenga.script.core.MyObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ScriptInstructionIf extends ScriptInstruction
{
	private ScriptInstruction condition;
	private ScriptInstruction thenBlock;
	private ScriptInstruction elseBlock;
	
	public MyObject execute()
	{
		if(condition.execute().toBool())
		{
			return thenBlock.execute();
		}
		else if(elseBlock != null)
		{
			return elseBlock.execute();
		}
		
		return null;
	}
	
	@Override
	public String toString()
	{
		String s = "if " + condition + " then\n";
		s += thenBlock;
		
		if(elseBlock != null)
			s += "else " + elseBlock;
		else s += "endif";
		
		return s;
	}
}
