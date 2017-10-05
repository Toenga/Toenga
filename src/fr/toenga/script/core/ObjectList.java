package fr.toenga.script.core;

import java.util.ArrayList;
import java.util.List;

public class ObjectList extends MyObject
{
	private List<MyObject> list;
	
	public ObjectList(MyObject... values)
	{
		this.list = new ArrayList<MyObject>();
		
		for(MyObject v : values)
			this.list.add(v);
	}
	
	public ObjectList(List<MyObject> values)
	{
		this.list = values;
	}

	@Override
	public String toString()
	{
		return list.toString();
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
		return list;
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
		return "List";
	}
}
