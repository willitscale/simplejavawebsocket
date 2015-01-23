package uk.co.n3tw0rk.websocketregistration.pools;

import java.util.HashMap;
import uk.co.n3tw0rk.websocketregistration.threads.ChannelWriteThread;
import uk.co.n3tw0rk.websocketregistration.wrappers.Abstraction;

public class ChannelSocketPool extends Abstraction
{
	protected static HashMap<String, ChannelWriteThread> mPool = null;

	public static synchronized void put( String session, byte[] bytes )
	{
		verify();

		if( !mPool.containsKey( session ) )
		{
			return;
		}

		mPool
			.get( session )
			.addEvent( bytes );
	}

	public static synchronized void add( String session, ChannelWriteThread writeThread )
	{
		verify();
		mPool.put( session, writeThread );
	}

	public static synchronized void remove( String session )
	{
		verify();
		mPool.remove( session );
	}

	public static synchronized void verify()
	{
		if( null == mPool )
		{
			mPool = new HashMap<String,ChannelWriteThread>();
		}
	}

}
