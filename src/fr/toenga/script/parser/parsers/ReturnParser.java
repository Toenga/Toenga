package fr.toenga.script.parser.parsers;

import fr.toenga.script.parser.Parser;
import fr.toenga.script.parser.WordType;
import fr.toenga.script.representation.ScriptInstruction;
import fr.toenga.script.representation.ScriptInstructionReturn;

public class ReturnParser
{
	public static ScriptInstruction parse(Parser parser)
	{
		parser.verifyNext(WordType.RETURN);
		return new ScriptInstructionReturn(ValueParser.parse(parser));
	}
}
