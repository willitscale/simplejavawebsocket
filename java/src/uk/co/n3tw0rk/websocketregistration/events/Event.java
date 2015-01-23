package uk.co.n3tw0rk.websocketregistration.events;

import uk.co.n3tw0rk.websocketregistration.wrappers.Abstraction;

public abstract class Event extends Abstraction
{
	public abstract String event( String data );
	public abstract String event( String data, String session );
}
