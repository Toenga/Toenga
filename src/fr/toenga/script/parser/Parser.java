package fr.toenga.script.parser;

import java.io.IOException;
import java.io.Reader;

import fr.toenga.script.lexer.Automata;
import fr.toenga.script.lexer.AutomataReader;
import fr.toenga.script.lexer.AutomataResult;
import fr.toenga.script.parser.parsers.ScriptParser;

public class Parser {
	private Automata automata;
	private AutomataReader reader;
	
	private AutomataResult current;
	
	public Parser(Reader r) throws IOException
	{
		this.reader = new AutomataReader(r);
		this.automata = new Automata();
	}
	
	public AutomataResult getNextWord()
	{
		try
		{
			if(current == null)
				current = automata.getNextWord(reader);
		}
		catch (IOException e)
		{
			throwException(); //FIXME
		}
		
		return current;
	}
	
	public void consumeWord()
	{
		current = null;
	}
	
	public boolean testNext(WordType type)
	{
		return testNext(type, true);
	}
	
	public boolean testNext(WordType type, boolean consume)
	{
		AutomataResult res = getNextWord();
		
		if(res == null || res.getType() != type)
			return false;
		
		if(consume)
			consumeWord();
		return true;
	}
	
	public void verifyNext(WordType type)
	{
		AutomataResult res = getNextWord();
		
		if(res == null || res.getType() != type)
			throwException();
		
		consumeWord();
	}
	
	public Operator getOperator(AutomataResult res)
	{
		if(res != null && res.getType() == WordType.OPERATOR)
			return Operator.getOperator(res.getResult());
		return null;
	}
	
	public Operator getAssignationOperator(AutomataResult res)
	{
		if(res != null && res.getType() == WordType.ASSIGNATION_OPERATOR)
			return Operator.getAssignationOperator(res.getResult());
		return null;
	}
		
	public void parse()
	{
		System.out.println(ScriptParser.parse("coucou", this));
	}

	public void throwException()
	{
		reader.throwException();
	}
	
	public void throwException(String message)
	{
		reader.throwException();
	}
}
