package fr.toenga.script.representation;

import fr.toenga.script.core.MyObject;
import fr.toenga.script.core.ObjectReference;
import fr.toenga.script.core.ObjectTuple;
import fr.toenga.script.parser.Operator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ScriptInstructionAssignation extends ScriptInstruction
{
	public ScriptInstruction to;
	public ScriptInstruction value;
	public Operator operator;
	
	@Override
	public MyObject execute()
	{
		MyObject obj = to.execute();
		
		if(obj instanceof ObjectReference)
		{
			MyObject val = value.execute().toObj();
			
			
		}
		else if(obj instanceof ObjectTuple)
		{
			MyObject val = value.execute().toObj();
			
			if(!(val instanceof ObjectTuple))
				throw new IllegalArgumentException("Assignation Error: can't assign " + val.getValueType() + " to a tuple!");
			
			ObjectTuple to = (ObjectTuple) obj, from = (ObjectTuple) val;
			
			if(to.length() != from.length())
				throw new IllegalArgumentException("Assignation Error: tuples have different length!");
		}
		
		return null;
	}
	
	@Override
	public String toString()
	{
		return to + " " + operator.getAssignationValue() + " " + value;
	}
}
