package fr.toenga.script.parser.parsers;

import java.util.ArrayList;
import java.util.List;

import fr.toenga.script.parser.Parser;
import fr.toenga.script.representation.ScriptInstruction;
import fr.toenga.script.representation.ScriptInstructionBlock;

public class BlockParser {
	public static ScriptInstruction parse(Parser parser)
	{
		List<ScriptInstruction> instructions = new ArrayList<>();
		ScriptInstruction instruction = InstructionParser.tryParse(parser);
		
		while(instruction != null)
		{
			instructions.add(instruction);
			instruction = InstructionParser.tryParse(parser);
		}
		
		return new ScriptInstructionBlock(instructions);
	}
}
