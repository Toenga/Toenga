package fr.toenga.script.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.toenga.utis.StringUtils;

/**
 * Represent a custom object
 * @author LeLanN
 */
public class ObjectCustom extends MyObject
{
	private Map<String, MyObject> values;
	private String className;
	
	public ObjectCustom()
	{
		this.values = new HashMap<>();
	}
	
	public MyObject get(String s)
	{
		if(values.containsKey(s))
			return values.get(s);
		
		throw new IllegalArgumentException("Undefined member '" + s + "' in " + className);
	}
	
	public void set(String s, MyObject obj)
	{
		values.put(s, obj);
	}

	@Override
	public String toString()
	{
		return "{" + StringUtils.join(", ", " = ", values) + "}";
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
		return className;
	}
}
