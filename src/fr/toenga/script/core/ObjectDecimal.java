package fr.toenga.script.core;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ObjectDecimal extends MyObject
{
	private double value;
	
	public ObjectDecimal(String value)
	{
		this( Double.parseDouble(value) );
	}
	
	@Override
	public String toString()
	{
		return Double.toString(value);
	}

	@Override
	public boolean toBool() 
	{
		return value != Double.NaN;
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
		return (int) value;
	}

	@Override
	public MyObject toObj()
	{
		return this;
	}

	@Override
	public String getValueType()
	{
		return "double";
	}
}
