package uk.co.n3tw0rk.websocketregistration.wrappers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import uk.co.n3tw0rk.websocketregistration.config.Config;

public class Abstraction
{
	public static DateFormat dateFormat = new SimpleDateFormat("[yyyy/MM/dd HH:mm:ss] :: ");
	public static String objectName = null;
	public static Date date = null;

	public void console( String message )
	{
		if( Config.DEBUGGING_VERBOSE )
		{
			objectName = this.getClass().getName();
			date = new Date();
			System.out.println( dateFormat.format(date) + objectName + " :: " + message );
		}
	}

	public void console( short message )
	{
		this.console( "" + message );
	}

	public void console( byte message )
	{
		this.console( "" + message );
	}

	public void console( float message )
	{
		this.console( "" + message );
	}

	public void console( double message )
	{
		this.console( "" + message );
	}

	public void console( char message )
	{
		this.console( "" + message );
	}

	public void console( long message )
	{
		this.console( "" + message );
	}

	public void console( int message )
	{
		this.console( "" + message );
	}
}
