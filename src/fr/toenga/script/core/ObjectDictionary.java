package fr.toenga.script.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.toenga.utis.StringUtils;

public class ObjectDictionary extends MyObject
{
	private Map<MyObject, MyObject> objects = new HashMap<>();
	
	@Override
	public String toString()
	{
		return "{" + StringUtils.join(", ", " = ", objects) + "}";
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
		return "Dictionary";
	}

}
