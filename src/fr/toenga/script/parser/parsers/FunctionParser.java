package fr.toenga.script.parser.parsers;

import fr.toenga.script.core.MyObject;
import fr.toenga.script.core.ObjectReference;
import fr.toenga.script.lexer.AutomataResult;
import fr.toenga.script.parser.Parser;
import fr.toenga.script.parser.WordType;
import fr.toenga.script.representation.ScriptInstruction;
import fr.toenga.script.representation.ScriptInstructionFunction;

public class FunctionParser
{
	public static ScriptInstructionFunction parse(Parser parser)
	{
		parser.verifyNext(WordType.FUNCTION);
		
		AutomataResult result = parser.getNextWord();
		
		if(result == null || result.getType() != WordType.ID)
			parser.throwException();
		
		parser.consumeWord();
		MyObject obj = new ObjectReference(result.getResult());
		
		parser.verifyNext(WordType.OPEN_SBLOCK);
		ScriptInstruction[] parameters = ChainParser.readValueList(parser, WordType.CLOSE_SBLOCK);
		
		ScriptInstruction block = BlockParser.parse(parser);
		parser.verifyNext(WordType.END);
		
		return new ScriptInstructionFunction(obj, parameters, block);
	}
}
