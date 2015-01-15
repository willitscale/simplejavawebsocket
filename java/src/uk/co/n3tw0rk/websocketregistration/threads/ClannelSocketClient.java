package uk.co.n3tw0rk.websocketregistration.threads;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import uk.co.n3tw0rk.websocketregistration.structures.WebSocketVersion;

public class ClannelSocketClient
{

	protected Socket socket = null;
	
	protected StringBuilder input = null;
	protected byte[] output;

	protected InputStream inputStream = null;
	protected OutputStream outputStream = null;
	
	protected int buffer;
	
	protected boolean listen = true;

	protected WebSocketVersion webSocketVersion;
	
	public ClannelSocketClient( Socket socket )
	{
		this.socket = socket;
	}
}
