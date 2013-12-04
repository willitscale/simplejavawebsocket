package uk.co.n3tw0rk.websocketregistration.pools;

import java.util.HashMap;

import uk.co.n3tw0rk.websocketregistration.wrappers.Abstraction;

public class Pool extends Abstraction
{
	public static HashMap<Integer, Boolean> pool = null;
	
	public static boolean active( Integer socket )
	{
		verify();
		return pool.containsKey( socket );
	}
	
	public static boolean add( Integer socket )
	{
		verify();
		if( active( socket ) )
			return false;

		pool.put( socket, true );
		return true;
	}
	
	public static void remove( Integer socket )
	{
		verify();
		pool.remove( socket );
	}
	
	public static void verify()
	{
		if( null == pool )
			pool = new HashMap<Integer, Boolean>();
	}

}
