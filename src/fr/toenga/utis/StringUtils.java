package fr.toenga.utis;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

public class StringUtils
{
	@SuppressWarnings("unchecked")
	public static <T> String join(String joiner, T... obj)
	{
		if(obj == null) return "";
		
		String r = "";
		boolean f = true;
		
		for(T o : obj)
		{
			if(f)
				f = false;
			else r += joiner;
			
			r += o;
		}
		
		return r;
	}
	
	public static <T> String join(String joiner, Collection<T> obj)
	{
		if(obj == null) return "";
		
		String r = "";
		boolean f = true;
		
		for(T o : obj)
		{
			if(f)
				f = false;
			else r += joiner;
			
			r += o;
		}
		
		return r;
	}
	
	public static <K, V> String join(String valueJoin, String keyJoiner, Map<K, V> map)
	{
		if(map == null) return "";
		
		String r = "";
		boolean f = true;
		
		for(Entry<K, V> entry : map.entrySet())
		{
			if(f)
				f = false;
			else r += valueJoin;
			
			r += entry.getKey() + keyJoiner + entry.getValue();
		}
		
		return r;
	}
}
