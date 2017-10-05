package fr.toenga.script.parser.parsers;

import fr.toenga.script.parser.Parser;
import fr.toenga.script.parser.WordType;
import fr.toenga.script.representation.ScriptInstruction;
import fr.toenga.script.representation.ScriptInstructionFor;
import fr.toenga.script.representation.ScriptInstructionWhile;

public class LoopParser
{
	public static ScriptInstruction parseFor(Parser parser)
	{
		ScriptInstruction variable = null, iterator = null, block = null;
		
		if(parser.testNext(WordType.FOR))
		{
			variable = ValueParser.parse(parser);
			parser.verifyNext(WordType.IN);
			iterator = ValueParser.parse(parser);
			
			parser.verifyNext(WordType.DO);
			block = BlockParser.parse(parser);
			parser.verifyNext(WordType.NEXT);
		}
		else if(parser.testNext(WordType.DO))
		{
			block = ValueParser.parse(parser);
			parser.verifyNext(WordType.FOR);
			variable = ValueParser.parse(parser);
			parser.verifyNext(WordType.IN);
			iterator = ValueParser.parse(parser, false, false);
		}
		
		return new ScriptInstructionFor(variable, iterator, block);
	}
	
	public static ScriptInstruction parseWhile(Parser parser)
	{
		ScriptInstruction condition = null, block = null;
		
		parser.verifyNext(WordType.WHILE);
		condition = ValueParser.parse(parser);
		
		parser.verifyNext(WordType.DO);
		block = BlockParser.parse(parser);
		parser.verifyNext(WordType.NEXT);
		
		return new ScriptInstructionWhile(condition, block);
	}
}
