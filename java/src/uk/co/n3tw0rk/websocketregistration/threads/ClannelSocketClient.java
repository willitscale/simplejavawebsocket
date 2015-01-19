package uk.co.n3tw0rk.websocketregistration.threads;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

import uk.co.n3tw0rk.websocketregistration.structures.WebSocketVersion;
import uk.co.n3tw0rk.websocketregistration.wrappers.AbstractionThread;

public class ClannelSocketClient extends AbstractionThread
{
	protected SocketChannel mClient;
	protected Map<String, String> mClientProperties = new HashMap<String, String>();
	protected SelectionKey mClientKey;

	protected WebSocketVersion webSocketVersion;

	public ClannelSocketClient( SocketChannel client )
	{
		this.mClient = client;
	}
	
	public boolean handshakeComplete()
	{
		return ( null != this.webSocketVersion && this.webSocketVersion.handshake.isEstablished( ) );
	}
	
	protected boolean validateConnection()
	{
		return ( !this.mClient.isConnected() );
	}
}
