package fr.toenga.script.representation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScriptPosition
{
	private String file;
	private int line;
}
