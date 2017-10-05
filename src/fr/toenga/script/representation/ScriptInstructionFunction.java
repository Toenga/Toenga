package fr.toenga.script.representation;

import fr.toenga.script.core.MyObject;
import fr.toenga.utis.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScriptInstructionFunction extends ScriptInstruction
{
	private MyObject name;
	private ScriptInstruction[] params;
	private ScriptInstruction block;
	
	
	@Override
	public MyObject execute()
	{
		return null;
	}

	@Override
	public String toString()
	{
		return "function " + name + "(" + StringUtils.join(", ", params) + ")\n" + block + "end";
	}
}
