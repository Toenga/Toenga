package fr.toenga.script.core;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ObjectString extends MyObject
{
	private String value;
	
	@Override
	public String toString()
	{
		return value;
	}

	@Override
	public boolean toBool()
	{
		return true;
	}

	@Override
	public double toDouble()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public List<MyObject> toList()
	{
		return MyObject.toList(this);
	}

	@Override
	public int toInt()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public MyObject toObj()
	{
		return this;
	}

	@Override
	public String getValueType()
	{
		return "string";
	}
}
