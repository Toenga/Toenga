package fr.toenga.plugins.events;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface  EventHandler
{

	EventPriority priority()
	default EventPriority.NORMAL;

	public static enum EventPriority
	{
		LOWEST,
		LOW,
		NORMAL,
		HIGH,
		HIGHEST;
	}

}
