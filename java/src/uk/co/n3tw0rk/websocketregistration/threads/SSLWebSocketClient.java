package uk.co.n3tw0rk.websocketregistration.threads;

import java.io.InputStream;
import java.io.OutputStream;

import javax.net.ssl.SSLSocket;

import uk.co.n3tw0rk.websocketregistration.factories.WebsocketVersionFactory;
import uk.co.n3tw0rk.websocketregistration.structures.WebSocketVersion;

public class SSLWebSocketClient extends WebSocketClient
{
	private SSLSocket socket = null;
	
	private StringBuilder input = null;
	private byte[] output;

	private InputStream inputStream = null;
	private OutputStream outputStream = null;
	
	private int buffer;
	
	private boolean listen = true;

	private WebSocketVersion webSocketVersion;
	
	public SSLWebSocketClient( SSLSocket socket )
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

				boolean discrete = true;
				
				do
				{
					this.buffer = this.inputStream.read();

					/**
					 * Bit of a nasty fudge for SSL Sockets discrete records until I get an SSL handshake implemented
					 * 
					 * @see http://download.java.net/jdk7/archive/b123/docs/api/javax/net/ssl/SSLSocket.html#addHandshakeCompletedListener(javax.net.ssl.HandshakeCompletedListener)
					 * @see https://forums.oracle.com/thread/1146968
					 */
					if( 0 < this.inputStream.available() )
					{
						discrete = false;
					}
					
					console( this.buffer );
					
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
				while( 0 != this.inputStream.available() || discrete );

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
						this.listen = false;

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
