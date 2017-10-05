package fr.toenga.script.lexer.complex;

import java.util.Arrays;

import fr.toenga.script.lexer.Automata;
import fr.toenga.script.lexer.AutomataNode;
import fr.toenga.script.parser.WordType;

public class CwNumber {
	public static void createNumberWord(Automata automata)
	{
		ComplexWord w = new ComplexWord(automata.getEntryNode());
		AutomataNode node = new AutomataNode(false, WordType.INTEGER_VALUE, null);
		AutomataNode nodeFloat = new AutomataNode();
		AutomataNode nodeFloatOk = new AutomataNode(false, WordType.DECIMAL_VALUE, null);

		w.applyList(new AutomataNode(), Arrays.asList('-'), true);
		w.applyList(node, ComplexWord.buildCharacterList(false, false, true), false);
		w.applyList(node, ComplexWord.buildCharacterList(false, false, true), false);
		
		w.applyList(nodeFloat, Arrays.asList('.'), true);
		w.applyList(nodeFloatOk, ComplexWord.buildCharacterList(false, false, true), false);
		w.applyList(nodeFloatOk, ComplexWord.buildCharacterList(false, false, true), false);

		w.applyList(createExpWord(), Arrays.asList('e', 'E'), true);
	}
	
	public static AutomataNode createExpWord()
	{
		AutomataNode node1 = new AutomataNode(), node2 = new AutomataNode(), node3 = new AutomataNode();
		
		ComplexWord w = new ComplexWord(node1);
		
		w.applyList(node2, Arrays.asList('-'), true);
		w.applyList(node3, ComplexWord.buildCharacterList(false, false, true), false);
		w.applyList(node3, ComplexWord.buildCharacterList(false, false, true), false);
		w.setExitValue(WordType.DECIMAL_VALUE);

		return node1;
	}
}
