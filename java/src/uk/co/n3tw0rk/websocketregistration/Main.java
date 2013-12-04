package uk.co.n3tw0rk.websocketregistration;

import uk.co.n3tw0rk.websocketregistration.framing.DataFrameRequest;
import uk.co.n3tw0rk.websocketregistration.threads.SocketServer;
import uk.co.n3tw0rk.websocketregistration.wrappers.Abstraction;

public class Main extends Abstraction
{
	public Main()
	{
		try
		{
			( new SocketServer( 8081 ) ).start();
			this.infinite();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	
	public void infinite()
	{
		while( true )
		{
			try
			{
				Thread.sleep( 10000 );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
		}
	}
	
	public static void main( String [] args )
	{
		new Main();
	}

}
