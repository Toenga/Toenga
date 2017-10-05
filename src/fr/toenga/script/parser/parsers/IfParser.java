package fr.toenga.script.parser.parsers;

import fr.toenga.script.parser.Parser;
import fr.toenga.script.parser.WordType;
import fr.toenga.script.representation.ScriptInstruction;
import fr.toenga.script.representation.ScriptInstructionIf;

public class IfParser
{
	public static ScriptInstruction parse(Parser parser)
	{
		parser.verifyNext(WordType.IF);
		ScriptInstruction condition = ValueParser.parse(parser);
		parser.verifyNext(WordType.THEN);
		ScriptInstruction block = BlockParser.parse(parser);

		ScriptInstructionIf result = new ScriptInstructionIf(condition, block, null);
		ScriptInstructionIf current = result;
		
		while(parser.testNext(WordType.ELIF))
		{
			condition = ValueParser.parse(parser);
			parser.verifyNext(WordType.THEN);
			block = BlockParser.parse(parser);
			
			current.setElseBlock((current = new ScriptInstructionIf(condition, block, null)));
		}
		
		if(parser.testNext(WordType.ELSE))
		{
			current.setElseBlock( BlockParser.parse(parser) );
		}
		
		parser.verifyNext(WordType.ENDIF);
		
		return result;
	}
}
