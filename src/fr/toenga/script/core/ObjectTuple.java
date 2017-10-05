package fr.toenga.script.core;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ObjectTuple extends MyObject
{
	private List<MyObject> objects;
	
	@Override
	public String toString()
	{
		String s = "(";
		boolean f = true;
		
		for(MyObject obj : objects)
		{
			if(f)
				f = false;
			else s += ", ";
			
			s += obj;
		}
		
		return s + ")";
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
		return objects;
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
		return "tuple";
	}
	
	public int length()
	{
		return objects.size();
	}
}
