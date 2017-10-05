package fr.toenga.script.core;

import java.util.ArrayList;
import java.util.List;

public abstract class MyObject
{
	public static List<MyObject> toList(MyObject obj)
	{
		List<MyObject> list = new ArrayList<>();
		
		list.add(obj);
		return list;
	}
	
	public static MyObject addStatic(MyObject obj1, MyObject obj2)
	{
		obj1 = obj1.toObj(); // for references
		obj2 = obj2.toObj(); // for references

		try
		{
			if(obj1 instanceof ObjectList || obj2 instanceof ObjectList)
			{
				List<MyObject> obj = obj1.toList();
				
				obj.addAll(obj2.toList());
				return new ObjectList(obj);
			}
			
			if(obj1 instanceof ObjectTuple || obj2 instanceof ObjectTuple)
			{
				List<MyObject> obj = obj1.toList();
				
				obj.addAll(obj2.toList());
				return new ObjectTuple(obj);
			}
			
			if(obj1 instanceof ObjectString || obj2 instanceof ObjectString)
				return new ObjectString(obj1.toString() + obj2.toString());
			
			if(obj1 instanceof ObjectDecimal || obj2 instanceof ObjectDecimal)
				return new ObjectDecimal(obj1.toDouble() + obj2.toDouble());
			
			if(obj1 instanceof ObjectDecimal || obj2 instanceof ObjectDecimal)
				return new ObjectInteger(obj1.toInt() + obj2.toInt());
			
			if(obj1 instanceof ObjectBoolean || obj2 instanceof ObjectBoolean)
				return new ObjectInteger(obj1.toInt() + obj2.toInt());
		}
		catch(Exception unused) {}
		
		throw new IllegalArgumentException("Type Error: unsupported operand (+): " + obj1.getValueType() + " and " + obj2.getValueType());
	}
	
	public static MyObject minusStatic(MyObject obj1, MyObject obj2)
	{
		obj1 = obj1.toObj(); // for references
		obj2 = obj2.toObj(); // for references
		
		try
		{
			if(obj1 instanceof ObjectDecimal || obj2 instanceof ObjectDecimal)
				return new ObjectDecimal(obj1.toDouble() - obj2.toDouble());
			
			if(obj1 instanceof ObjectDecimal || obj2 instanceof ObjectDecimal)
				return new ObjectInteger(obj1.toInt() - obj2.toInt());
			
			if(obj1 instanceof ObjectBoolean || obj2 instanceof ObjectBoolean)
				return new ObjectInteger(obj1.toInt() - obj2.toInt());
		}
		catch(Exception unused) {}
		
		throw new IllegalArgumentException("Type Error: unsupported operand (+): " + obj1.getValueType() + " and " + obj2.getValueType());
	}
	
	public static MyObject divStatic(MyObject obj1, MyObject obj2)
	{
		obj1 = obj1.toObj(); // for references
		obj2 = obj2.toObj(); // for references
		
		try
		{
			if(obj1 instanceof ObjectDecimal || obj2 instanceof ObjectDecimal)
				return new ObjectDecimal(obj1.toDouble() / obj2.toDouble());
			
			if(obj1 instanceof ObjectDecimal || obj2 instanceof ObjectDecimal)
				return new ObjectInteger(obj1.toInt() / obj2.toInt());
			
			if(obj1 instanceof ObjectBoolean || obj2 instanceof ObjectBoolean)
				return new ObjectInteger(obj1.toInt() / obj2.toInt());
		}
		catch(Exception e)
		{
			if(e instanceof ArithmeticException)
				throw new IllegalArgumentException(e.getMessage());
		}
		
		throw new IllegalArgumentException("Type Error: unsupported operand (+): " + obj1.getValueType() + " and " + obj2.getValueType());
	}
	
	public static MyObject moduloStatic(MyObject obj1, MyObject obj2)
	{
		obj1 = obj1.toObj(); // for references
		obj2 = obj2.toObj(); // for references
		
		try
		{
			if(obj1 instanceof ObjectDecimal || obj2 instanceof ObjectDecimal)
				return new ObjectDecimal(obj1.toDouble() % obj2.toDouble());
			
			if(obj1 instanceof ObjectDecimal || obj2 instanceof ObjectDecimal)
				return new ObjectInteger(obj1.toInt() % obj2.toInt());
			
			if(obj1 instanceof ObjectBoolean || obj2 instanceof ObjectBoolean)
				return new ObjectInteger(obj1.toInt() % obj2.toInt());
		}
		catch(Exception unused) {}
		
		throw new IllegalArgumentException("Type Error: unsupported operand (+): " + obj1.getValueType() + " and " + obj2.getValueType());
	}
	
	public abstract String toString();
	
	public abstract boolean toBool();
	
	public abstract double toDouble();
	
	public abstract List<MyObject> toList();
	
	public abstract int toInt();
	
	public abstract MyObject toObj();
	
	public abstract String getValueType();
}
