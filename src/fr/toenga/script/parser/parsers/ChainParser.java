package fr.toenga.script.parser.parsers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.toenga.script.core.MyObject;
import fr.toenga.script.core.ObjectBoolean;
import fr.toenga.script.core.ObjectDecimal;
import fr.toenga.script.core.ObjectInteger;
import fr.toenga.script.core.ObjectReference;
import fr.toenga.script.core.ObjectString;
import fr.toenga.script.lexer.AutomataResult;
import fr.toenga.script.parser.Operator;
import fr.toenga.script.parser.Parser;
import fr.toenga.script.parser.WordType;
import fr.toenga.script.representation.ScriptInstruction;
import fr.toenga.script.representation.ScriptInstructionChain;
import fr.toenga.script.representation.ScriptInstructionChain.CallType;
import fr.toenga.script.representation.ScriptInstructionList;
import fr.toenga.script.representation.ScriptInstructionMap;
import fr.toenga.script.representation.ScriptInstructionValue;

public class ChainParser
{
	public static ScriptInstructionChain parse(Parser parser, Operator operator, ScriptInstruction first)
	{
		ScriptInstructionChain chain = first == null ? parseSingleValue(parser) : new ScriptInstructionChain(first, CallType.Basic, null, null);
		ScriptInstructionChain current = chain;

		while(parser.getNextWord() != null)
		{
			ScriptInstructionChain res = null;

			if( parser.testNext(WordType.CALL_SEPARATOR) )
				res = parseSingleValue(parser);
			
			else if( parser.testNext(WordType.OPEN_SBLOCK) )
				res = new ScriptInstructionChain(null, CallType.Function, readValueList(parser, WordType.CLOSE_SBLOCK), null);
			
			else if( parser.testNext(WordType.OPEN_ARRAY) )
				res = new ScriptInstructionChain(null, CallType.Array, readValueList(parser, WordType.CLOSE_ARRAY), null);
			
			else break;

			current.setNext(res);
			current = current.getNext();
		}

		return chain;
	}

	private static ScriptInstructionChain parseSingleValue(Parser parser)
	{
		AutomataResult result = parser.getNextWord();

		if(result == null)
			parser.throwException("A value was excepted");

		MyObject value = null;

		switch(result.getType())
		{
			case STRING_VALUE:
				parser.consumeWord();			
				value = new ObjectString(result.getResult());
				break;
			case DECIMAL_VALUE:
				parser.consumeWord();
				value = new ObjectDecimal(result.getResult());
				break;
			case INTEGER_VALUE:
				parser.consumeWord();
				value = new ObjectInteger(result.getResult());
				break;
			case BOOLEAN_VALUE:
				parser.consumeWord();
				value = new ObjectBoolean(result.getResult());
				break;
			case NONE_VALUE:
				parser.consumeWord();
				value = new ObjectReference(null);
				break;
			case ID:
				parser.consumeWord();
				value = new ObjectReference(result.getResult());
				break;
			case OPEN_MAP:
				parser.consumeWord();
				return new ScriptInstructionChain(new ScriptInstructionMap(readMap(parser)), CallType.Basic, null, null);
			case OPEN_ARRAY:
				parser.consumeWord();
				return new ScriptInstructionChain(new ScriptInstructionList(readValueList(parser, WordType.CLOSE_ARRAY), false), CallType.Basic, null, null);
			case DO:
				return new ScriptInstructionChain(LoopParser.parseFor(parser), CallType.Basic, null, null);
			default:
				parser.throwException("A value was excepted");
		}

		return new ScriptInstructionChain(new ScriptInstructionValue(value, null), CallType.Basic, null, null);
	}

	public static ScriptInstruction[] readValueList(Parser parser, WordType close)
	{
		List<ScriptInstruction> instructions = new ArrayList<>();

		if(parser.testNext(close))
			return new ScriptInstruction[0];

		do
			instructions.add(ValueParser.parse(parser, false, false));
		while(parser.testNext(WordType.VALUE_SEPARATOR));

		parser.verifyNext(close);
		return instructions.toArray(new ScriptInstruction[0]);
	}

	private static Map<ScriptInstruction, ScriptInstruction> readMap(Parser parser)
	{
		Map<ScriptInstruction, ScriptInstruction> map = new HashMap<>();
		
		if(parser.testNext(WordType.CLOSE_MAP))
			return map;

		do
		{
			boolean isConstructor = parser.testNext(WordType.DO, false);
			ScriptInstruction key = ValueParser.parse(parser, false, false);
			
			if(isConstructor)
			{
				map.put(key, null);
			}
			else
			{
				parser.verifyNext(WordType.ASSIGNATION_OPERATOR);
				ScriptInstruction value = ValueParser.parse(parser, false, false);
				
				map.put(key, value);
			}
		}
		while(parser.testNext(WordType.VALUE_SEPARATOR));

		parser.verifyNext(WordType.CLOSE_MAP);
		return map;
	}
}
