package uk.co.n3tw0rk.websocketregistration.threads;

import java.io.IOException;
import java.net.ServerSocket;

import uk.co.n3tw0rk.websocketregistration.exceptions.WSRSocketServerException;
import uk.co.n3tw0rk.websocketregistration.pools.WSRSocketPool;
import uk.co.n3tw0rk.websocketregistration.wrappers.WSRAbstractionThread;

public class WSRSocketServer extends WSRAbstractionThread
{
	private ServerSocket serverSocket;
	private int port;
	
	public WSRSocketServer( int port ) throws WSRSocketServerException, IOException
	{
		if( !WSRSocketPool.add( port ) )
			throw new WSRSocketServerException();
		
		this.port = port;
		this.serverSocket = new ServerSocket( this.port );
	}

	public void run()
	{
		System.out.println( "Server listening on port " + this.port );
		while( true )
		{
			try
			{
				( new WSRSocketClient( this.serverSocket.accept() ) ).start();
			}
			catch( IOException e )
			{
				e.printStackTrace();
			}
		}
	}
	
}
