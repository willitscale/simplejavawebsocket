package uk.co.n3tw0rk.websocketregistration.wrappers;

import uk.co.n3tw0rk.websocketregistration.config.WSRConfig;

public class WSRAbstraction
{
	public void console( String message )
	{
		if( WSRConfig.DEBUGGING_VERBOSE )
		{
			String objectName = this.getClass().getName();
			System.out.println( objectName + " :: " + message );
		}
	}
}
