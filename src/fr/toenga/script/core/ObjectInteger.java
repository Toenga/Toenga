package fr.toenga.script.core;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ObjectInteger extends MyObject
{
	private int value;
	
	public ObjectInteger(String value)
	{
		this( Integer.parseInt(value) );
	}
	
	@Override
	public String toString()
	{
		return Integer.toString(value);
	}

	@Override
	public boolean toBool()
	{
		return value != 0;
	}

	@Override
	public double toDouble()
	{
		return value;
	}

	@Override
	public List<MyObject> toList()
	{
		return MyObject.toList(this);
	}

	@Override
	public int toInt()
	{
		return value;
	}

	@Override
	public MyObject toObj()
	{
		return this;
	}

	@Override
	public String getValueType()
	{
		return "integer";
	}
}
