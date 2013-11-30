package uk.co.n3tw0rk.websocketregistration.threads;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import uk.co.n3tw0rk.websocketregistration.framing.DataFrameBuilder;
import uk.co.n3tw0rk.websocketregistration.utils.WSRRequestParser;
import uk.co.n3tw0rk.websocketregistration.wrappers.WSRAbstractionThread;

public class WSRSocketClient extends WSRAbstractionThread
{
	private Socket socket = null;
	
	private StringBuilder input = null;
	private String output = null;

	private BufferedReader bufferedReader = null;
	private PrintWriter printWriter = null;
	private InputStream inputStream = null;
	
	private int buffer;
	
	private boolean listen = true;
	
	public WSRSocketClient( Socket socket )
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
			//this.bufferedReader = new BufferedReader( this.inputStream );
			this.printWriter = new PrintWriter( this.socket.getOutputStream(), true );

			while( this.listen )
			{
				if( !this.socket.isConnected() )
					this.listen = false;

				if( !handler )
				{

					this.input = new StringBuilder();

					DataFrameBuilder dataFrameBuilder = new DataFrameBuilder();


					while( 0 != this.inputStream.available() )
					{
						this.buffer = this.inputStream.read();
						
						dataFrameBuilder.setFrameData( this.buffer );
						
						console( this.buffer );
						console( ( char ) this.buffer );
						
						this.input.append( ( char ) this.buffer );
					}
					
					if( 0 < this.input.length() )
						console( this.input.toString() );
					
				}
				else
				{
					handler = false;
					this.input = new StringBuilder();

					while( 0 != this.inputStream.available() )
					{
						this.buffer = this.inputStream.read();
						this.input.append( ( char ) this.buffer );
					}

					if( 0 == this.input.length() )
						continue;
	
					this.output = WSRRequestParser.responseHeader( this.input.toString() );
	
					this.printWriter.println( this.output );
						
					this.printWriter.flush();
				}
			}

			console( "Client disconnected" );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
}
