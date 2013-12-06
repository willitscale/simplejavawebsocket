package uk.co.n3tw0rk.websocketregistration.threads;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

import uk.co.n3tw0rk.websocketregistration.framing.DataFrameRequest;
import uk.co.n3tw0rk.websocketregistration.framing.DataFrameResponse;
import uk.co.n3tw0rk.websocketregistration.utils.RequestParser;
import uk.co.n3tw0rk.websocketregistration.wrappers.AbstractionThread;

public class SocketClient extends AbstractionThread
{
	private Socket socket = null;
	
	private StringBuilder input = null;
	private byte[] output;

	private InputStream inputStream = null;
	private OutputStream outputStream = null;
	
	private int buffer;
	
	private boolean listen = true;
	
	private DataFrameRequest dataFrameRequest = null;
	private DataFrameResponse dataFrameResponse = null;
	
	public SocketClient( Socket socket )
	{
		this.socket = socket;
	}

	public void run()
	{
		try
		{
			boolean handler = true;
			console( "Client connected" );
			
			this.inputStream = this.socket.getInputStream();
			this.outputStream = this.socket.getOutputStream();

			this.input = new StringBuilder();

			reset : 
			while( this.listen )
			{

				// Something has happened to the connection so kill off this connection and thread
				if( !this.socket.isConnected() || this.socket.isInputShutdown() || this.socket.isOutputShutdown() )
					this.listen = false;

				if( !handler )
				{
					this.dataFrameRequest = new DataFrameRequest();
					this.dataFrameResponse = new DataFrameResponse();
				}

				do
				{
					// Causes the thread to wait until there is data available
					this.buffer = this.inputStream.read();

					// WebSocket Error as all data should be in single singed byte range ( 0 - 255 )
					if( -1 == this.buffer )
					{
						console( "Websocket Error" );
						this.listen = false;
						break reset;
					}

					if( !handler )
					{

						//console( "<< " + this.buffer );
						this.dataFrameRequest.setFrameData( this.buffer );
					}
					else
						this.input.append( ( char ) this.buffer );
					
				}
				while( 0 != this.inputStream.available() );

				if( !handler )
				{
					this.dataFrameResponse.setPayload( this.dataFrameRequest.getPayload(), this.dataFrameRequest.getOPCode() );
	
					ByteBuffer byteBuffer = this.dataFrameResponse.getDataFrame();

					this.outputStream.write( byteBuffer.array(), 0, byteBuffer.limit() );
					
					this.outputStream.flush();
				}
				else
				{
					// Invalid Header Sent so drop the connection
					if( 0 == this.input.length() )
						this.listen = false;

					handler = false;

					this.output = RequestParser.responseHeader( this.input.toString() );
					this.outputStream.write( this.output );
					this.outputStream.flush();
				}
			}
			
			this.inputStream.close();
			this.outputStream.close();
			this.socket.close();

			console( "Client disconnected" );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
}
