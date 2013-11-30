package uk.co.n3tw0rk.websocketregistration.wrappers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import uk.co.n3tw0rk.websocketregistration.config.WSRConfig;

public class WSRAbstractionThread extends Thread
{
	public static DateFormat dateFormat = new SimpleDateFormat("[yyyy/MM/dd HH:mm:ss] :: ");
	public static String objectName = null;
	public static Date date = null;

	public void console( String message )
	{
		if( WSRConfig.DEBUGGING_VERBOSE )
		{
			objectName = this.getClass().getName();
			date = new Date();
			System.out.println( dateFormat.format(date) + objectName + " :: " + message );
		}
	}
}
