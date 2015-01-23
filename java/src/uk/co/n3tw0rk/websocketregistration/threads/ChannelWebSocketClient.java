package uk.co.n3tw0rk.websocketregistration.threads;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.security.SecureRandom;

import uk.co.n3tw0rk.websocketregistration.pools.ChannelSocketPool;

public class ChannelWebSocketClient extends ClannelSocketClient
{
	protected ChannelReadThread mRead;
	protected ChannelWriteThread mWrite;
	protected String mSessionID;

	public String session()
	{
		if( null == this.mSessionID )
		{
			SecureRandom random = new SecureRandom();
			this.mSessionID = new BigInteger( 130, random ).toString( 32 );
		}
		return this.mSessionID;
	}

	public ChannelWebSocketClient( SocketChannel client, Selector selector )
	{
		super( client );
    	System.out.println( this.getClass().getName() + " - Initialised  - Session : " + this.session() );
		
		if( this.mClient != null)
        {
			try
			{
				this.mClient.configureBlocking( false );
                this.mClientKey = this.mClient.register( selector, SelectionKey.OP_READ, SelectionKey.OP_WRITE );
                this.mClientProperties.put( ChannelWebSocketServer.CHANNEL_TYPE, ChannelWebSocketServer.CLIENT_CHANNEL );
                this.mClientKey.attach( this.mClientProperties );
			}
			catch( ClosedChannelException e )
			{
				e.printStackTrace();
			}
			catch( IOException e )
			{
				e.printStackTrace();
			}
        }
	}

	public void run()
	{
    	System.out.println( this.getClass().getName() + " - Running" );
    	
    	this.mRead = new ChannelReadThread( this.mClient, this.session() );
    	this.mWrite = new ChannelWriteThread( this.mClient, this.session() );

    	ChannelSocketPool.add( this.session(), this.mWrite );

    	( new Thread( this.mRead ) ).start();
    	( new Thread( this.mWrite ) ).start();
	}

}
