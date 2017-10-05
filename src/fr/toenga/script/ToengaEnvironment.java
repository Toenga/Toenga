package fr.toenga.script;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToengaEnvironment
{	
	private Map<String, String> variables;
	
	public ToengaEnvironment()
	{
		this.variables = new HashMap<>();
	}
	
	public List<String> parseCommand(String str)
	{
		return new ArrayList<>();
	}
	
	public String parseString(String str)
	{
		return str;
	}
}
