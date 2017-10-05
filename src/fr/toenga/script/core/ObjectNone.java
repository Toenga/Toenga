package fr.toenga.script.core;

import java.util.List;

public class ObjectNone extends MyObject
{
	@Override
	public String toString()
	{
		return "None";
	}

	@Override
	public boolean toBool()
	{
		return false;
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
		return "None";
	}
}
