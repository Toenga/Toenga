package fr.toenga.script.lexer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fr.toenga.script.parser.WordType;
import lombok.Getter;
import lombok.Setter;

public class AutomataNode {
	private Map<Character, AutomataNode> 	nodes	   			= new HashMap<>();
	private Map<Character, Character>		ignored				= new HashMap<>();
	private boolean						 	ignoreWhitespaces 	= false;
	private WordType					 	exitValue 			= null;
	@Getter
	@Setter
	private AutomataNode					defNode				= null;
	
	public AutomataNode(boolean ignoreWhitespaces, WordType exitValue, AutomataNode defNode)
	{
		this.ignoreWhitespaces 	= ignoreWhitespaces;
		this.exitValue			= exitValue;
		this.defNode			= defNode;
	}
	
	public AutomataNode()
	{
		this(false, null, null);
	}
	
	public void createSimpleTransition(String word, WordType type, int pos)
	{
		if(pos >= word.length())
		{
			exitValue = type;
		}
		else
		{				
			char c = word.charAt(pos);
			
			if(!nodes.containsKey(c))
				addTransition(new AutomataNode(), c);
			
			nodes.get(c).createSimpleTransition(word, type, pos + 1);
		}
	}
	
	public void addTransition(AutomataNode node, char... values)
	{
		for(char c : values)
			nodes.put(c, node);
	}
	
	public void replace(Character value1, Character value2)
	{
		ignored.put(value1, value2);
	}
	
	public AutomataNode transition(char value)
	{
		if(ignoreWhitespaces && Character.isWhitespace(value))
			return this;
		
		AutomataNode res = nodes.get(value);
		return res == null ? defNode : res;
	}
	
	public Character getUsedValue(char value)
	{
		if(ignoreWhitespaces && Character.isWhitespace(value))
			return null;
		if(ignored.containsKey(value))
			return ignored.get(value);

		return value;
	}
	
	public WordType getExitValue()
	{
		return exitValue;
	}
	
	public void setExitValue(WordType exitValue)
	{
		this.exitValue = exitValue;
	}
	
	public void print(String deb)
	{
		Map<AutomataNode, List<Character>> reverseMap = new HashMap<>();
		
		for(Entry<Character, AutomataNode> e : nodes.entrySet())
		{
			if(!reverseMap.containsKey(e.getValue()))
				reverseMap.put(e.getValue(), new ArrayList<>());
			reverseMap.get(e.getValue()).add(e.getKey());
		}
		
		if(defNode != null)
		{
			if(reverseMap.containsKey(defNode))
				reverseMap.get(defNode).add('*');
			else reverseMap.put(defNode, Arrays.asList('*'));
		}
		
		for(Entry<AutomataNode, List<Character>> e : reverseMap.entrySet())
		{
			System.out.print(deb);
			for(Character c : e.getValue())
				System.out.print(c + " ");
			System.out.println("");
			
			if(e.getKey().equals(this))
				System.out.println(deb + "\tthis");
			else e.getKey().print(deb + "\t");
		}

		System.out.println(deb + exitValue);
	}
}
