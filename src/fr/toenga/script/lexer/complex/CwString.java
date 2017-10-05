package fr.toenga.script.lexer.complex;

import fr.toenga.script.lexer.Automata;
import fr.toenga.script.lexer.AutomataNode;
import fr.toenga.script.parser.WordType;

public class CwString
{
	public static void createStringWord(Automata automata)
	{
		createForQuote('"', automata, false);
		createForQuote('\'', automata, false);
		createForQuote('"', automata, true);
		createForQuote('\'', automata, true);
	}
	
	private static void createForQuote(char c, Automata automata, boolean a)
	{
		AutomataNode node1 = new AutomataNode(), node2 = new AutomataNode(), node3 = new AutomataNode(false, WordType.STRING_VALUE, null);
		node1.setDefNode(node1);
		
		if(!a)
		{
			node1.addTransition(node2, '\\');
			node1.replace('\\', null);
			
			node2.addTransition(node1, '\\', '0', 'n', 't', 'r', 'b', 'f', '"', '\'');
			node2.replace('0', '\0');
			node2.replace('n', '\n');
			node2.replace('t', '\t');
			node2.replace('r', '\r');
			node2.replace('b', '\b');
			node2.replace('f', '\f');
		}
		
		node1.addTransition(node3, c);
		node1.replace(c, null);
		
		AutomataNode entry = automata.getEntryNode();
		
		if(a)
		{
			if(entry.transition('@') != null)
				entry = entry.transition('@');
			else
			{
				entry.addTransition(node2, '@');
				entry.replace('@', null);
				
				entry = node2;
			}
		}
		
		entry.addTransition(node1, c);
		entry.replace(c, null);
	}
}
