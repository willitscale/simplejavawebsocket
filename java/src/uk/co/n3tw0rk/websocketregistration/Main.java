package uk.co.n3tw0rk.websocketregistration;

import uk.co.n3tw0rk.websocketregistration.threads.WSRSocketServer;
import uk.co.n3tw0rk.websocketregistration.wrappers.WSRAbstraction;

public class Main extends WSRAbstraction
{
	public Main()
	{
		try
		{
			( new WSRSocketServer( 8081 ) ).start();
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
