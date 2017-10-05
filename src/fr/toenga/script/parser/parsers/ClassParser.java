package fr.toenga.script.parser.parsers;

import java.util.ArrayList;
import java.util.List;

import fr.toenga.script.core.MyObject;
import fr.toenga.script.core.ObjectReference;
import fr.toenga.script.lexer.AutomataResult;
import fr.toenga.script.parser.Parser;
import fr.toenga.script.parser.WordType;
import fr.toenga.script.representation.ScriptInstructionClass;
import fr.toenga.script.representation.ScriptInstructionFunction;

public class ClassParser
{
	public static ScriptInstructionClass parse(Parser parser)
	{
		parser.verifyNext(WordType.CLASS);
		
		AutomataResult result = parser.getNextWord();
		
		if(result == null || result.getType() != WordType.ID)
			parser.throwException();
		
		parser.consumeWord();
		MyObject obj = new ObjectReference(result.getResult());
		
		List<ScriptInstructionFunction> functions = new ArrayList<ScriptInstructionFunction>();
		
		while(parser.testNext(WordType.FUNCTION, false))
			functions.add(FunctionParser.parse(parser));
		
		parser.verifyNext(WordType.END);
		
		return new ScriptInstructionClass(obj, functions);
	}
}
