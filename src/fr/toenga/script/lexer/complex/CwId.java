package fr.toenga.script.lexer.complex;

import java.util.List;

import fr.toenga.script.lexer.Automata;
import fr.toenga.script.lexer.AutomataNode;
import fr.toenga.script.parser.WordType;

public class CwId {
	public static void createIdWord(Automata automata)
	{
		ComplexWord 	w 			= new ComplexWord(automata.getEntryNode());
		AutomataNode 	node 		= new AutomataNode(false, WordType.ID, null);
		List<Character> characters 	= ComplexWord.buildCharacterList(true, true, true, '_');

		
		w.applyList(node, ComplexWord.buildCharacterList(true, true, false, '_'), false);
		
		while(w.getNodes().size() > 1)
		{
			w.setExitValue(WordType.ID);
			w.applyList(node, characters, false);
		}
	}
}
