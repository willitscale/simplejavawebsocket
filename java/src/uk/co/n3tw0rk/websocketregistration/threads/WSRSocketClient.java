package uk.co.n3tw0rk.websocketregistration.threads;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import uk.co.n3tw0rk.websocketregistration.framing.DataFrame;
import uk.co.n3tw0rk.websocketregistration.utils.WSRRequestParser;
import uk.co.n3tw0rk.websocketregistration.wrappers.WSRAbstractionThread;

public class WSRSocketClient extends WSRAbstractionThread
{
	private Socket socket = null;
	
	private StringBuilder input = null;
	private String output = null;

	private BufferedReader bufferedReader = null;
	private PrintWriter printWriter = null;
	private InputStreamReader inputStreamReader = null;
	
	private int charValue;
	
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
			
			this.inputStreamReader = new InputStreamReader( this.socket.getInputStream() );
			this.bufferedReader = new BufferedReader( this.inputStreamReader );
			this.printWriter = new PrintWriter( this.socket.getOutputStream(), true );

			while( this.listen )
			{
				if( !this.socket.isConnected() )
					this.listen = false;

				if( !handler )
				{

					this.input = new StringBuilder();

					DataFrame dataFrame = new DataFrame();
					
					while( -1 != ( this.charValue = ( int ) this.bufferedReader.read() ) )
					{
						dataFrame.setFrameData( this.charValue );
						System.out.println( this.charValue );
						//this.input.append( ( char ) charValue );
					}
					
				}
				else
				{
					handler = false;
					this.input = new StringBuilder();
	
					while( this.bufferedReader.ready() )
					{
						this.charValue = ( int ) this.bufferedReader.read();
						//System.out.println( charValue );
						this.input.append( ( char ) this.charValue );
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
