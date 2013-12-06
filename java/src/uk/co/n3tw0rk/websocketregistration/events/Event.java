package uk.co.n3tw0rk.websocketregistration.events;

import uk.co.n3tw0rk.websocketregistration.wrappers.Abstraction;

public abstract class Event extends Abstraction
{
	public abstract byte[] event( String data );
}
