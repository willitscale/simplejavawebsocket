package uk.co.n3tw0rk.websocketregistration;

import uk.co.n3tw0rk.websocketregistration.config.Config;
import uk.co.n3tw0rk.websocketregistration.threads.WebSocketServer;
import uk.co.n3tw0rk.websocketregistration.wrappers.Abstraction;

/**
 * Main Object
 * 
 * @package uk.co.n3tw0rk.websocketregistration
 * @version 0.1
 * @access public
 * @author James Lockhart <james@n3tw0rk.co.uk>
 */
public class Main extends Abstraction
{
	/**
	 * @var boolean
	 * @access private
	 */
	private boolean running = true;
	
	/**
	 * 	Constructor
	 * 
	 * @access public
	 * @constructor
	 */
	public Main()
	{
		try
		{
			( new WebSocketServer( 8081 ) ).start();
			this.infinite();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 	Infinite Method
	 * 		Keeps the application running indefinitely
	 * 
	 * @access public
	 * @return void
	 */
	public void infinite()
	{
		while( this.running )
		{
			try
			{
				Thread.sleep( Config.SLEEP_DELAY );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Main Method
	 * 
	 * @access public
	 * @static
	 * @param String[] args
	 * @return void
	 */
	public static void main( String [] args )
	{
		new Main();
	}
}
