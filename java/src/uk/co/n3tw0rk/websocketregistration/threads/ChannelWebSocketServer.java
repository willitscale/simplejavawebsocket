package uk.co.n3tw0rk.websocketregistration.threads;

import java.io.IOException;

import uk.co.n3tw0rk.websocketregistration.events.Event;
import uk.co.n3tw0rk.websocketregistration.exceptions.SocketServerException;

public class ChannelWebSocketServer extends SocketServer
{

	public ChannelWebSocketServer( int port, Event event )
		throws SocketServerException, IOException
	{
		super( port, event );
	}

	/**
	 * 
	 */
	@Override
	public void run()
	{
		console( "Server listening on port " + this.port );

		while( this.running )
		{
			try
			{
				( new ChannelWebSocketClient( this.serverSocket.accept() ) ).start();
			}
			catch( IOException e )
			{
				e.printStackTrace();
			}
		}
	}
}
