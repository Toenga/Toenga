package fr.toenga.script.parser.parsers;

import fr.toenga.script.lexer.AutomataResult;
import fr.toenga.script.parser.Parser;
import fr.toenga.script.representation.ScriptInstruction;

public class InstructionParser
{
	public static ScriptInstruction tryParse(Parser parser)
	{
		return parse(parser, false);
	}
	
	public static ScriptInstruction parse(Parser parser)
	{
		return parse(parser, true);
	}
	
	private static ScriptInstruction parse(Parser parser, boolean thr)
	{
		if(parser.getNextWord() == null)
			return null;
		
		AutomataResult result = parser.getNextWord();
		
		switch(result.getType())
		{
			case FOR:
				return LoopParser.parseFor(parser);
			case IF:
				return IfParser.parse(parser);
			case WHILE:
				return LoopParser.parseWhile(parser);
			case RETURN:
				return ReturnParser.parse(parser);
			case INCREMENT_OPERATOR:
			case DECIMAL_VALUE:
			case INTEGER_VALUE:
			case NONE_VALUE:
			case BOOLEAN_VALUE:
			case OPEN_MAP:
			case OPEN_ARRAY:
			case OPEN_SBLOCK:
			case OPERATOR:
			case STRING_VALUE:
			case ID:
			case DO:
				return ValueParser.parse(parser);
			default:
				if(thr)
					parser.throwException();
		}
		
		return null;
	}
}
