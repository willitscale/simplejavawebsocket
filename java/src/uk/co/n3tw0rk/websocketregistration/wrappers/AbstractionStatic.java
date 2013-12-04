package uk.co.n3tw0rk.websocketregistration.wrappers;

import uk.co.n3tw0rk.websocketregistration.config.Config;

public class AbstractionStatic
{
	public static void console( String message )
	{
		if( Config.DEBUGGING_VERBOSE )
		{
			String objectName = "static";//getClass().getName();
			System.out.println( objectName + " :: " + message );
		}
	}

}
