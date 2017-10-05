package fr.toenga.script.representation;

import java.util.List;

import fr.toenga.script.core.MyObject;
import fr.toenga.utis.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScriptInstructionClass extends ScriptInstruction
{
	private MyObject name;
	private List<ScriptInstructionFunction> functions;
	
	@Override
	public MyObject execute()
	{
		return null;
	}

	@Override
	public String toString()
	{
		return "class " + name + "\n" + StringUtils.join("\n", functions) + "\nend";
	}
}
