package fr.toenga.script.representation;

import fr.toenga.script.core.MyObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ScriptInstructionChain extends ScriptInstruction
{
	private ScriptInstruction value;
	private CallType callType;
	private ScriptInstruction[] values;
	private ScriptInstructionChain next;
	
	@Override
	public MyObject execute()
	{
		return null;
	}
	
	public static enum CallType
	{
		Basic,
		Array,
		Function
	}
	
	@Override
	public String toString()
	{
		String s = (value == null ? "" : value) + "";
		
		if(callType == CallType.Array)
		{
			s += "[";
			boolean f = true;
			
			for(ScriptInstruction ins : values)
			{
				s += (!f ? ", " : "") + ins.toString();
				f = false;
			}
			
			s += "]";
		}
		
		if(callType == CallType.Function)
		{
			s += "(";
			boolean f = true;
			
			for(ScriptInstruction ins : values)
			{
				s += (!f ? ", " : "") + ins.toString();
				f = false;
			}
			
			s += ")";
		}
		
		ScriptInstructionChain v = next;
		
		if(v != null)
			s += (v.callType == CallType.Basic ? "." : "") + v.toString();

		return s;
	}
}
