package fr.toenga.script.core;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ObjectReference extends MyObject
{
	private String variableName;
	
	public MyObject getValue()
	{
		return new ObjectString(variableName); // FIXME
	}

	@Override
	public String toString()
	{
		return getValue().toString();
	}

	@Override
	public boolean toBool()
	{
		return getValue().toBool();
	}

	@Override
	public double toDouble()
	{
		return getValue().toDouble();
	}

	@Override
	public List<MyObject> toList()
	{
		return getValue().toList();
	}

	@Override
	public int toInt()
	{
		return getValue().toInt();
	}

	@Override
	public MyObject toObj()
	{
		return getValue();
	}

	@Override
	public String getValueType()
	{
		return getValue().getValueType();
	}
}
