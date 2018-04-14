package fr.toenga.plugins;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.toenga.plugins.events.Event;
import fr.toenga.plugins.events.EventHandler;
import fr.toenga.plugins.events.EventHandler.EventPriority;
import fr.toenga.plugins.events.Listener;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class EventDispatcher
{

	private Map<Listener, List<HandlerInformation>>										handlersByListeners;
	private Map<Class<? extends Event>, Map<EventPriority, List<HandlerInformation>>>	handlersByEventsAndPriorities;

	protected EventDispatcher()
	{
		handlersByListeners = new HashMap<Listener, List<HandlerInformation>>();
		handlersByEventsAndPriorities = new HashMap<Class<? extends Event>, Map<EventPriority, List<HandlerInformation>>>();
	}

	protected <T extends Event> void dispatch(T e)
	{
		Map<EventPriority, List<HandlerInformation>> handlers = handlersByEventsAndPriorities.get(e.getClass());
		if (handlers != null)
		{
			dispatch(e, handlers.get(EventPriority.LOWEST));
			dispatch(e, handlers.get(EventPriority.LOW));
			dispatch(e, handlers.get(EventPriority.NORMAL));
			dispatch(e, handlers.get(EventPriority.HIGH));
			dispatch(e, handlers.get(EventPriority.HIGHEST));
		}
	}

	private void dispatch(Event e, List<HandlerInformation> handlers)
	{
		if (handlers != null)
		{
			for (HandlerInformation handler : handlers)
			{
				try
				{
					handler.invoke(e);
				}
				catch (IllegalAccessException | IllegalArgumentException exc)
				{
				}
				catch (InvocationTargetException exc)
				{
					System.out.println("Error whilst dispatching event " + e + " to listener " + handler.getListener() + "  :");
					exc.printStackTrace();
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected void registerListener(Listener listener)
	{
		List<HandlerInformation> methods = new ArrayList<HandlerInformation>();
		for(Method method : listener.getClass().getMethods())
		{
			if (method.isAnnotationPresent(EventHandler.class))
			{
				if (method.getParameterCount() != 1)
				{
					System.out.println(method + " has the @EventHandler annotation but have more or less than one argument !");
				}
				else if (!isEvent(method.getParameters()[0].getType()))
				{
					System.out.println(method + " has the @EventHandler annotation but parameter is not an event !");
				}
				else
				{
					Class<? extends Event> eventType = (Class<? extends Event>) method.getParameters()[0].getType();
					EventPriority priority = method.getAnnotation(EventHandler.class).priority();
					HandlerInformation handler = new HandlerInformation(listener, method, eventType, priority);

					addHandler(eventType, handler, priority);
					methods.add(handler);
				}
			}
		}

		if (!methods.isEmpty())
		{
			handlersByListeners.put(listener, methods);
		}
	}

	@SuppressWarnings("unlikely-arg-type")
	protected void unregisterListener(Listener listener)
	{
		List<HandlerInformation> handlersInformation = handlersByListeners.get(listener);
		if (handlersInformation != null)
		{
			for(HandlerInformation info : handlersInformation)
			{
				handlersByEventsAndPriorities.get(info.getEvent()).get(info.getPriority()).remove(info.getMethod());
			}
		}
	}

	private void addHandler(Class<? extends Event> type, HandlerInformation handler, EventPriority priority)
	{
		if (!handlersByEventsAndPriorities.containsKey(type))
		{
			handlersByEventsAndPriorities.put(type, new HashMap<EventPriority, List<HandlerInformation>>());
		}

		Map<EventPriority, List<HandlerInformation>> handlers = handlersByEventsAndPriorities.get(type);
		if (!handlers.containsKey(priority))
		{
			handlers.put(priority, new ArrayList<HandlerInformation>());
		}

		handlers.get(priority).add(handler);
	}

	private boolean isEvent(Class<?> clazz)
	{
		Class<?> parentClass = clazz.getSuperclass();
		if (parentClass.equals(Object.class))
		{
			return false;
		}
		else if (parentClass.equals(Event.class))
		{
			return true;
		}
		else 
		{
			return isEvent(parentClass);
		}
	}

	@AllArgsConstructor
	private class HandlerInformation
	{

		@Getter private Listener listener;
		@Getter private Method method;
		@Getter private Class<? extends Event> event;
		@Getter private EventPriority priority;

		public void invoke(Event e) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
		{
			method.invoke(listener, e);
		}
	}
}
