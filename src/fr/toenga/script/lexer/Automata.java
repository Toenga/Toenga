package fr.toenga.script.lexer;

import java.io.IOException;

import fr.toenga.script.lexer.complex.CwId;
import fr.toenga.script.lexer.complex.CwNumber;
import fr.toenga.script.lexer.complex.CwString;
import fr.toenga.script.parser.Operator;
import fr.toenga.script.parser.WordType;

public class Automata {
	private AutomataNode entryNode;
	
	public Automata()
	{
		this.entryNode = new AutomataNode(true, null, null);

		for(Operator o : Operator.values())
		{
			if(o.getValue() != null)
				addSimpleWord(o.getValue(), WordType.OPERATOR);
			if(o.getAssignationValue() != null)
				addSimpleWord(o.getAssignationValue(), WordType.ASSIGNATION_OPERATOR);
		}
		
		addSimpleWord("function", WordType.FUNCTION);
		addSimpleWord("class", WordType.CLASS);
		addSimpleWord("end", WordType.END);
		addSimpleWord("if", WordType.IF);
		addSimpleWord("if", WordType.IF);
		addSimpleWord("then", WordType.THEN);
		addSimpleWord("elif", WordType.ELIF);
		addSimpleWord("else", WordType.ELSE);
		addSimpleWord("endif", WordType.ENDIF);
		addSimpleWord("for", WordType.FOR);
		addSimpleWord("in", WordType.IN);
		addSimpleWord("while", WordType.WHILE);
		addSimpleWord("do", WordType.DO);
		addSimpleWord("next", WordType.NEXT);
		addSimpleWord("return", WordType.RETURN);
		addSimpleWord("(", WordType.OPEN_SBLOCK);
		addSimpleWord(")", WordType.CLOSE_SBLOCK);
		addSimpleWord("[", WordType.OPEN_ARRAY);
		addSimpleWord("]", WordType.CLOSE_ARRAY);
		addSimpleWord("{", WordType.OPEN_MAP);
		addSimpleWord("}", WordType.CLOSE_MAP);
		addSimpleWord(",", WordType.VALUE_SEPARATOR);
		addSimpleWord(".", WordType.CALL_SEPARATOR);
		addSimpleWord("=", WordType.ASSIGNATION_OPERATOR);
		addSimpleWordGroup(WordType.INCREMENT_OPERATOR, "++", "--");
		
		addSimpleWordGroup(WordType.BOOLEAN_VALUE, "True", "False");
		addSimpleWordGroup(WordType.NONE_VALUE, "None");

		CwNumber.createNumberWord(this);
		CwId.createIdWord(this);
		CwString.createStringWord(this);
	}
	
	public AutomataNode getEntryNode()
	{
		return entryNode;
	}
	
	public AutomataResult getNextWord(AutomataReader reader) throws IOException
	{
		AutomataNode node = entryNode;
		AutomataNode curr = entryNode;
		
		String currentValue = "";
		
		while(reader.hasNextChar())
		{
			curr = node.transition( reader.getNextChar() );

			if(curr == null)
			{
				if(node.getExitValue() != null)
					return new AutomataResult(node.getExitValue(), currentValue);
				else if( reader.getNextChar() != '\0' ) // not the end
					reader.throwException();
				else reader.consumeCurrentChar(); // the end
			}
			else
			{
				Character c = node.getUsedValue( reader.getNextChar() );
				if(c != null)
					currentValue += c;
				
				reader.consumeCurrentChar();
				node = curr;
			}
		}
		
		return null; // ne devrait pas arriver
	}
	
	public void addSimpleWord(String word, WordType type)
	{
		entryNode.createSimpleTransition(word, type, 0);
	}
	
	public void addSimpleWordGroup(WordType type, String... words)
	{
		for(String word : words)
			addSimpleWord(word, type);
	}
}
