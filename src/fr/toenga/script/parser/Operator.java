package fr.toenga.script.parser;

import lombok.Getter;

public enum Operator
{
	PLUS("+", "+=", 0),
	MINUS("-", "-=", 0),
	MULT("*", "*=", 1),
	DIV("/", "/=", 1),
	MOD("%", "%=", 1),
	OR("||", "|=", 2),
	AND("&&", "&=", 2),
	NOT("!", null, 0),
	EQUAL("==", "===", 2),
	EQUALINF("<=", null, 2),
	INF("<", null, 2),
	EQUALSUP(">=", null, 2),
	SUP(">", null, 2),
	NEQUAL("!=", "!==", 2),
	SET(null, "=", 2);
//	BNOT("~", 0),
//	BLEFT("<<", 1),
//	BRIGHT(">>", 1),
//	BAND("&", 1),
//	BOR("|", 1),
//	BXOR("^", 1);
	
	@Getter
	private final String value, assignationValue;
	@Getter
	private final int priority;
	
	Operator(String value, String assignationValue, int priority)
	{
		this.value = value;
		this.assignationValue = assignationValue;
		this.priority = priority;
	}
	
	public static Operator getOperator(String val)
	{
		for(Operator o : values())
			if(val.equals(o.value))
				return o;
		return null;
	}
	
	public static Operator getAssignationOperator(String val)
	{
		for(Operator o : values())
			if(val.equals(o.assignationValue))
				return o;
		return null;
	}
}