package fr.toenga.script.representation;

import fr.toenga.script.core.MyObject;
import fr.toenga.script.parser.Operator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter@Setter
public class ScriptInstructionOperator extends ScriptInstruction
{
	private Operator operator;
	private ScriptInstruction left;
	private ScriptInstruction right;
	
	@Override
	public MyObject execute()
	{
		MyObject left = this.left.execute(), right = this.right.execute();
		
		if(left == null || right == null || operator == null)
			return null;
		
		return left;
	}
	
	public boolean complete()
	{
		return left != null && right != null && operator != null;
	}
	
	@Override
	public String toString()
	{
		return "(" + left + " " + operator.getValue() + " " + right + ")";
	}
}
