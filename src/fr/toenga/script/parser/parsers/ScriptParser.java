package fr.toenga.script.parser.parsers;

import java.util.ArrayList;
import java.util.List;

import fr.toenga.script.parser.Parser;
import fr.toenga.script.representation.Script;
import fr.toenga.script.representation.ScriptInstruction;
import fr.toenga.script.representation.ScriptInstructionBlock;
import fr.toenga.script.representation.ScriptInstructionClass;
import fr.toenga.script.representation.ScriptInstructionFunction;

public class ScriptParser
{
	public static Script parse(String name, Parser parser)
	{
		List<ScriptInstruction> defaultFunction = new ArrayList<>();
		List<ScriptInstructionFunction> functions = new ArrayList<>();
		List<ScriptInstructionClass> classes = new ArrayList<>();
		
		while(parser.getNextWord() != null)
		{
			switch(parser.getNextWord().getType())
			{
				case CLASS:
					classes.add(ClassParser.parse(parser));
					break;
				case FUNCTION:
					functions.add(FunctionParser.parse(parser));
					break;
				default:
					defaultFunction.add(BlockParser.parse(parser));
				break;
			}
		}
		
		ScriptInstruction block = new ScriptInstructionBlock(defaultFunction);
		ScriptInstructionFunction def = new ScriptInstructionFunction(null, null, block);

		return new Script(functions, classes, def, name);
	}
}
