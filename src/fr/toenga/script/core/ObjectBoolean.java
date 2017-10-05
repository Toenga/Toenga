package fr.toenga.script.core;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ObjectBoolean extends MyObject
{
	private boolean value;
	
	public ObjectBoolean(String value)
	{
		this( Boolean.parseBoolean(value) );
	}
	
	@Override
	public String toString()
	{
		return Boolean.toString(value);
	}

	@Override
	public boolean toBool()
	{
		return value;
	}
	
	@Override
	public int toInt()
	{
		return value ? 1 : 0;
	}
	
	@Override
	public double toDouble()
	{
		return value ? 1d : 0d;
	}

	@Override
	public List<MyObject> toList()
	{
		return MyObject.toList(this);
	}

	@Override
	public MyObject toObj()
	{
		return this;
	}
	
	@Override
	public String getValueType()
	{
		return "boolean";
	}

}
