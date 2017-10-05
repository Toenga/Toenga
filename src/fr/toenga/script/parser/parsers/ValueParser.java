package fr.toenga.script.parser.parsers;

import java.util.ArrayList;
import java.util.List;

import fr.toenga.script.lexer.AutomataResult;
import fr.toenga.script.parser.Operator;
import fr.toenga.script.parser.Parser;
import fr.toenga.script.parser.WordType;
import fr.toenga.script.representation.ScriptInstruction;
import fr.toenga.script.representation.ScriptInstructionAssignation;
import fr.toenga.script.representation.ScriptInstructionList;
import fr.toenga.script.representation.ScriptInstructionOperator;

public class ValueParser {
	public static ScriptInstruction parse(Parser parser)
	{
		return parse(parser, true, true);
	}
	
	public static ScriptInstruction parse(Parser parser, boolean allowTuples, boolean allowAssignations)
	{
		List<ScriptInstruction> tuple = new ArrayList<ScriptInstruction>();
		ScriptInstruction ins = parse(parser, null, null);
		
		while(true)
		{
			AutomataResult result = parser.getNextWord();
	
			if(result == null)
				break;
	
			Operator operator = parser.getOperator(result);
	
			if(operator != null && operator.getPriority() == 2)
			{
				parser.consumeWord();
				ins = new ScriptInstructionOperator(operator, ins, parse(parser, null, null));
			}
			else if(result.getType() == WordType.VALUE_SEPARATOR && allowTuples)
			{
				parser.consumeWord();
				tuple.add(ins);

				ins = parse(parser, null, null);
			}
			else
				break;
		}

		if(tuple.size() > 0)
		{
			if(ins != null)
				tuple.add(ins);
			ins = new ScriptInstructionList(tuple.toArray(new ScriptInstruction[0]), true);
		}
		
		AutomataResult result = parser.getNextWord();
		Operator operator = parser.getAssignationOperator(result);
		
		if(operator != null && allowAssignations)
		{
			parser.consumeWord();
			return new ScriptInstructionAssignation(ins, parse(parser), operator);
		}
		
		return ins;
	}

	private static ScriptInstruction parse(Parser parser, ScriptInstruction ins, Operator opp)
	{
		AutomataResult result = parser.getNextWord();

		if(result == null)
		{
			return ins;
		}

		Operator operator = parser.getOperator(result);

		if(operator != null)
		{
			if(operator.getPriority() == 2)
				return verifyValidity(parser, ins);
			
			parser.consumeWord();

			if(ins == null)
			{
				if(opp != null || operator.getPriority() != 0)
					parser.throwException();

				return parse(parser, null, operator);
			}
			else if(ins instanceof ScriptInstructionOperator)
			{
				ScriptInstructionOperator sio = (ScriptInstructionOperator) ins;

				if(sio.complete())
				{
					ScriptInstructionOperator nsio = new ScriptInstructionOperator(operator, sio, null);

					if(operator.getPriority() == 0)
					{
						nsio.setRight( parse(parser, null, null) );
						return nsio;
					}

					return parse(parser, nsio, opp);
				}
				else if(opp == null && operator.getPriority() == 0)
				{
					return parse(parser, ins, operator);
				}
				else
				{
					parser.throwException();
				}
			}
			else
			{
				ScriptInstructionOperator nsio = new ScriptInstructionOperator(operator, ins, null);

				if(operator.getPriority() == 0)
				{
					nsio.setRight( parse(parser, null, null) );
					return nsio;
				}

				return parse(parser, nsio, null);
			}
		}
				
		if(ins == null || ins instanceof ScriptInstructionOperator)
		{
			ScriptInstruction instruction = getNextValue(parser, result, opp);
			if(instruction == null)
				return verifyValidity(parser, ins);
			
			ScriptInstructionOperator sio = (ScriptInstructionOperator) ins;

			if(sio == null)
			{
				return parse(parser, instruction, null);
			}
			else if(sio.complete())
			{
				parser.throwException();
			}
			else if(sio.getLeft() != null)
			{
				sio.setRight(instruction);
				return parse(parser, sio, null);
			}
		}
		else
		{
			return ins;
		}

		return null;
	}

	private static ScriptInstruction getNextValue(Parser parser, AutomataResult result, Operator operator)
	{
		switch(result.getType())
		{
			case ID:
			case INTEGER_VALUE:
			case DECIMAL_VALUE:
			case BOOLEAN_VALUE:
			case NONE_VALUE:
			case STRING_VALUE:
			case OPEN_MAP:
			case OPEN_ARRAY:
			case DO:
				return ChainParser.parse(parser, operator, null);
			case OPEN_SBLOCK:
				parser.consumeWord();
				ScriptInstruction ins = parse(parser);
					
				if(parser.getNextWord() == null || parser.getNextWord().getType() != WordType.CLOSE_SBLOCK)
					parser.throwException();
				
				parser.consumeWord();
	
				return ChainParser.parse(parser, null, ins);
			default:
				return null;
		}
	}
	
	private static ScriptInstruction verifyValidity(Parser parser, ScriptInstruction ins)
	{
		if(ins instanceof ScriptInstructionOperator)
		{
			ScriptInstructionOperator sio = (ScriptInstructionOperator) ins;

			if(!sio.complete())
			{
				parser.throwException();
			}
		}
		else if(ins == null)
			parser.throwException();

		return ins;
	}
}
