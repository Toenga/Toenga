package fr.toenga.script.representation;

import java.util.List;

import fr.toenga.script.core.MyObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ScriptInstructionBlock extends ScriptInstruction
{
	private List<ScriptInstruction> instructions;
	
	@Override
	public MyObject execute()
	{
		MyObject result = null;
		
		for(ScriptInstruction instruction : instructions)
			result = instruction.execute();
		
		return result;
	}
	
	@Override
	public String toString()
	{
		String s = "";
		
		for(ScriptInstruction instruction : instructions)
			s += instruction + "\n";
		return s;
	}
}
