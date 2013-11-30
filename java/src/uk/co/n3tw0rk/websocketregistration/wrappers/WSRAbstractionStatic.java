package uk.co.n3tw0rk.websocketregistration.wrappers;

import uk.co.n3tw0rk.websocketregistration.config.WSRConfig;

public class WSRAbstractionStatic
{
	public static void console( String message )
	{
		if( WSRConfig.DEBUGGING_VERBOSE )
		{
			String objectName = "static";//getClass().getName();
			System.out.println( objectName + " :: " + message );
		}
	}

}
