package uk.co.n3tw0rk.websocketregistration.threads;

import uk.co.n3tw0rk.websocketregistration.events.Event;
import uk.co.n3tw0rk.websocketregistration.wrappers.AbstractionThread;

public class WebSocketServer extends AbstractionThread
{
	protected Event event;
	protected int port;

	protected boolean running = true;
	
	//public WebSocketServer( int port, Event event )
	//{
	//	this.port = port;
	//	this.event = event;
	//}

}
