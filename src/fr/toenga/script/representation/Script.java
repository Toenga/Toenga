package fr.toenga.script.representation;

import java.util.List;

import fr.toenga.utis.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Script
{
	private List<ScriptInstructionFunction> functions;
	private List<ScriptInstructionClass> classes;
	private ScriptInstructionFunction mainFunction;
	
	private String name;
	
	@Override
	public String toString()
	{
		return StringUtils.join("\n\n", classes) + "\n\n" + StringUtils.join("\n\n", functions) + "\n\n" + mainFunction;
	}
}
