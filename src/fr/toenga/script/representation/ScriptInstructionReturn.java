package fr.toenga.script.representation;

import fr.toenga.script.core.MyObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ScriptInstructionReturn extends ScriptInstruction
{
	private ScriptInstruction value;

	public MyObject execute()
	{
		return null;
	}

	@Override
	public String toString()
	{
		return "return " + value;
	}
}