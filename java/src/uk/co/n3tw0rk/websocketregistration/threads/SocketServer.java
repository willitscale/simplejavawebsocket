package uk.co.n3tw0rk.websocketregistration.threads;

import java.io.IOException;
import java.net.ServerSocket;

import uk.co.n3tw0rk.websocketregistration.exceptions.SocketServerException;
import uk.co.n3tw0rk.websocketregistration.pools.SocketPool;
import uk.co.n3tw0rk.websocketregistration.wrappers.AbstractionThread;

/**
 * Web Socket Server Object
 * 
 * @package uk.co.n3tw0rk.websocketregistration
 * @version 0.1
 * @access public
 * @abstract
 * @author James Lockhart <james@n3tw0rk.co.uk>
 */
public class SocketServer extends WebSocketServer
{
	protected ServerSocket serverSocket;
	
	/**
	 * Constructor 
	 * 
	 * @access public
	 * @constructor
	 * @param port
	 * @throws SocketServerException
	 * @throws IOException
	 */
	public SocketServer( int port )
		throws SocketServerException, IOException
	{
		if( !SocketPool.add( port ) )
			throw new SocketServerException();
		
		this.port = port;
		this.serverSocket = new ServerSocket( this.port );
	}

	/**
	 * 
	 */
	public void run()
	{
		console( "Server listening on port " + this.port );

		while( this.running )
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
