package uk.co.n3tw0rk.websocketregistration.listeners;

import uk.co.n3tw0rk.websocketregistration.events.Event;

public class Events
{
	protected static Event mEvent;
	
	public synchronized static void setEvent( Event event )
	{
		mEvent = event;
	}
	
	public synchronized static Event getEvent() 
	{
		return mEvent;
	}
}
