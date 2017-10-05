package fr.toenga.script.lexer;

import java.io.IOException;
import java.io.Reader;

public class AutomataReader {
	private Reader 	reader;
	private int		next;
	
	private int pos, line = 1;
	
	public AutomataReader(Reader r) throws IOException
	{
		this.reader = r;
		next = r.read();
	}
	
	public char getNextChar()
	{
		if(next == -1)
			return '\0';
		return (char)next;
	}
	
	public void consumeCurrentChar() throws IOException
	{
		if(next == -1)
			next = -2;
		else next = reader.read();
		
		pos++;
		
		if(next == '\n')
		{
			pos = 0;
			line++;
		}
	}
	
	public boolean hasNextChar()
	{
		return next != -2;
	}
	
	public void throwException()
	{
		throw new IllegalArgumentException("Unexcepted token at line " + line + ", column " + pos + " ('" + ((char) next) + "')");
	}
}
