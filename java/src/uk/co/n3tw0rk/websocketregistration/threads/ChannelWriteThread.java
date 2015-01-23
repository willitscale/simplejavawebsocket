package uk.co.n3tw0rk.websocketregistration.threads;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentLinkedQueue;

import uk.co.n3tw0rk.websocketregistration.pools.ChannelSocketPool;
import uk.co.n3tw0rk.websocketregistration.structures.WriteEvent;

/**
 * <strong>Write Thread Class</strong>
 * 
 * @author James Lockhart <james@n3tw0rk.co.uk>
 * @since 2015-01-13
 */
public class ChannelWriteThread extends ClannelSocketClient
{
	protected ConcurrentLinkedQueue<WriteEvent> mQueue = new ConcurrentLinkedQueue<WriteEvent>();

	protected String mSession;
	
	public ChannelWriteThread( SocketChannel client, String session )
	{
		super( client );
		this.mSession = session;
	}
	
	public synchronized void addEvent( byte[] eventBytes )
	{
		this.mQueue.add( new WriteEvent( eventBytes ) );
		this.notify();
	}
	
	public synchronized void kill()
	{
		this.interrupt();

		try
		{
			this.mClient.close();
		}
		catch( IOException e )
		{
			e.printStackTrace();
		}

		this.notify();
	}
	
	@Override
	public void run()
	{
		for(;;)
		{
			if( null == this.mClient || !this.mClient.isConnected() || !this.mClient.isOpen() )
			{
				return;
			}

			try
			{
				if( 0 < this.mQueue.size() )
				{
					WriteEvent event = this.mQueue.poll();

					if( null != event && 0 < event.mBuffer.length )
					{
						this.mClient.write( ByteBuffer.wrap( event.mBuffer ) );
					}
				}
				else
				{
					synchronized( this )
					{
						this.wait();
					}
				}
			}
			catch( InterruptedException e )
			{
				e.printStackTrace();
			}
			catch( IOException e )
			{
				e.printStackTrace();
				try
				{
					this.mClient.close();
				}
				catch( IOException e1 )
				{
					e1.printStackTrace();
				}
				ChannelSocketPool.remove( this.mSession );
				return;
			}
		}
	}
}