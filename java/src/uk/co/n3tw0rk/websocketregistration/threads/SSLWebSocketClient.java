package uk.co.n3tw0rk.websocketregistration.threads;

import java.net.Socket;

/**
 * SSL WebSocket Client Object
 * 
 * @package uk.co.n3tw0rk.websocketregistration.threads
 * @version 0.1
 * @access public
 * @author James Lockhart <james@n3tw0rk.co.uk>
 */
public class SSLWebSocketClient extends WebSocketClient
{

	/**
	 * Constructor 
	 * 
	 * @access public
	 * @constructor
	 * @param socket
	 */
	public SSLWebSocketClient( Socket socket )
	{
		super( socket );
	}

}
