package uk.co.n3tw0rk.websocketregistration.threads;

import java.io.IOException;
import java.net.ServerSocket;

import uk.co.n3tw0rk.websocketregistration.exceptions.SocketServerException;
import uk.co.n3tw0rk.websocketregistration.pools.SocketPool;
import uk.co.n3tw0rk.websocketregistration.wrappers.AbstractionThread;

public class SocketServer extends AbstractionThread
{
	private ServerSocket serverSocket;
	private int port;
	
	public SocketServer( int port ) throws SocketServerException, IOException
	{
		if( !SocketPool.add( port ) )
			throw new SocketServerException();
		
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
				( new SocketClient( this.serverSocket.accept() ) ).start();
			}
			catch( IOException e )
			{
				e.printStackTrace();
			}
		}
	}
	
}
