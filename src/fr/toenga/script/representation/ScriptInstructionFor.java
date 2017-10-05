package fr.toenga.script.representation;

import fr.toenga.script.core.MyObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ScriptInstructionFor extends ScriptInstruction
{
	private ScriptInstruction variable;
	private ScriptInstruction iterator;
	private ScriptInstruction block;
	
	public MyObject execute()
	{	
		return null;
	}
	
	@Override
	public String toString()
	{
		return "for " + variable + " in " + iterator + " do " + block + " next";
	}
}