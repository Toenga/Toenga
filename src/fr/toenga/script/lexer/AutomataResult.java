package fr.toenga.script.lexer;

import fr.toenga.script.parser.WordType;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AutomataResult
{
	private WordType type;
	private String result;
}
