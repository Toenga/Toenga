package fr.toenga.script.lexer.complex;

import java.util.ArrayList;
import java.util.List;

import fr.toenga.script.lexer.AutomataNode;
import fr.toenga.script.parser.WordType;
import lombok.Getter;

public class ComplexWord {
	@Getter
	private List<AutomataNode> nodes;
	
	public ComplexWord(AutomataNode node)
	{
		this.nodes = new ArrayList<>();
		this.nodes.add(node);
	}
	
	public void applyList(AutomataNode on, List<Character> characters, boolean optional)
	{
		List<AutomataNode> nNodes = new ArrayList<AutomataNode>();
		
		nNodes.add(on);

		for(AutomataNode node : nodes)
		{
			for(char c : characters)
			{
				AutomataNode n = node.transition(c);
				
				if(n == null)
					node.addTransition(on, c);
				else if(!nNodes.contains(n))
					nNodes.add(n);
			}
		}
		
		if(optional)
			nodes.addAll(nNodes);
		else nodes = nNodes;
	}
	
	public void setExitValue(WordType type)
	{
		for(AutomataNode node : nodes)
			if(node.getExitValue() == null)
				node.setExitValue(type);
	}
	
	public static List<Character> buildCharacterList(boolean lowerAlp, boolean upperAlp, boolean digits, char... other)
	{
		List<Character> car = new ArrayList<Character>();
		
		if(lowerAlp)
			for(char c = 'a'; c <= 'z'; c++)
				car.add(c);
		if(upperAlp)
			for(char c = 'A'; c <= 'Z'; c++)
				car.add(c);
		if(digits)
			for(char c = '0'; c <= '9'; c++)
				car.add(c);
		
		for(char c : other)
			car.add(c);
		
		return car;
	}
}
