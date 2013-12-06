package uk.co.n3tw0rk.websocketregistration.threads;

import java.io.IOException;

import uk.co.n3tw0rk.websocketregistration.exceptions.SocketServerException;

/**
 * SSL WebSocket Server Object
 * 
 * @package uk.co.n3tw0rk.websocketregistration.threads
 * @version 0.1
 * @access public
 * @author James Lockhart <james@n3tw0rk.co.uk>
 */
public class SecureWebSocketServer extends WebSocketServer
{

	/**
	 * Constructor 
	 * 
	 * @access public
	 * @constructor
	 * @param port
	 * @throws SocketServerException
	 * @throws IOException
	 */
	public SecureWebSocketServer( int port )
		throws SocketServerException, IOException
	{
		super( port );
	}

}
