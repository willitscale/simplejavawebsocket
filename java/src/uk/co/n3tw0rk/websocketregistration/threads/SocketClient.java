package uk.co.n3tw0rk.websocketregistration.threads;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import uk.co.n3tw0rk.websocketregistration.factories.WebsocketVersionFactory;
import uk.co.n3tw0rk.websocketregistration.structures.WebSocketVersion;
import uk.co.n3tw0rk.websocketregistration.wrappers.AbstractionThread;

public class SocketClient extends WebSocketClient
{
	protected Socket socket = null;
	
	protected StringBuilder input = null;
	protected byte[] output;

	protected InputStream inputStream = null;
	protected OutputStream outputStream = null;
	
	protected int buffer;
	
	protected boolean listen = true;

	protected WebSocketVersion webSocketVersion;
	
	public SocketClient( Socket socket )
	{
		this.socket = socket;
	}

	public void run()
	{
		try
		{
			console( "Client connected" );
			
			this.inputStream = this.socket.getInputStream();
			this.outputStream = this.socket.getOutputStream();

			this.input = new StringBuilder();

			reset : 
			while( this.listen )
			{

				// Something has happened to the connection so kill off this connection and thread
				if( this.validateConnection() )
				{
					console( "Websocket Error" );
					this.listen = false;
					break reset;
				}

				do
				{
					// Causes the thread to wait until there is data available
					this.buffer = this.inputStream.read();

					// WebSocket Error as all data should be in single singed byte range ( 0 - 255 )
					if( -1 >= this.buffer || 256 <= this.buffer )
					{
						console( "Websocket Error" );
						this.listen = false;
						break reset;
					}

					if( this.handshakeComplete() )
					{
						this.webSocketVersion.request.setData( this.buffer );
					}
					else
					{
						this.input.append( ( char ) this.buffer );
					}
					
				}
				while( 0 != this.inputStream.available() );

				if( this.handshakeComplete() )
				{
					byte[] buffer = this.webSocketVersion.process();

					if( null != buffer )
					{
						this.outputStream.write( buffer );
						this.outputStream.flush();
					}
					
					if( this.webSocketVersion.response.isClosed() )
					{
						console( "Websocket Closed" );
						this.listen = false;
						break reset;
					}
				}
				else
				{
					// Invalid Header Sent so drop the connection
					if( 0 == this.input.length() )
					{
						this.listen = false;
					}

					this.webSocketVersion = ( new WebsocketVersionFactory( this.input.toString() ) ).getVersion();

					this.output = this.webSocketVersion.handshake.getResponse();

					if( null != this.output )
					{
						this.outputStream.write( this.output );
						this.outputStream.flush();
					}

					this.input = null;
					
					console( "Handshake Complete" );
				}
			}

			console( "Client disconnected" );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

		try
		{
			this.inputStream.close();

			this.outputStream.close();

			this.socket.close();
		}
		catch( Exception e )
		{
			
		}
	}
	
	public boolean handshakeComplete()
	{
		return ( null != this.webSocketVersion && this.webSocketVersion.handshake.isEstablished( ) );
	}
	
	private boolean validateConnection()
	{
		return ( !this.socket.isConnected() || this.socket.isInputShutdown() || this.socket.isOutputShutdown() );
	}
}
