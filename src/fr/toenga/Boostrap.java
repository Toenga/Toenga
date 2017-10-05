package fr.toenga;

import java.io.FileReader;
import java.io.IOException;

import fr.toenga.script.parser.Parser;

public class Boostrap {
	public static void main(String[] args)
	{
		try
		{
			new Parser( new FileReader("test.my") ).parse();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
