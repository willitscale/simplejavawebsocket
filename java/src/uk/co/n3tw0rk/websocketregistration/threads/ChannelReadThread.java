package uk.co.n3tw0rk.websocketregistration.threads;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import uk.co.n3tw0rk.websocketregistration.exceptions.HandshakeException;
import uk.co.n3tw0rk.websocketregistration.exceptions.WebsocketVersionException;
import uk.co.n3tw0rk.websocketregistration.factories.WebsocketVersionFactory;
import uk.co.n3tw0rk.websocketregistration.pools.ChannelSocketPool;

/**
 * <strong>Read Thread Class</strong>
 * 
 * @author James Lockhart <james@n3tw0rk.co.uk>
 * @since 2015-01-13
 */
public class ChannelReadThread extends ClannelSocketClient
{
	protected boolean mDiscrete = true;
	protected StringBuilder mOutputBuffer = new StringBuilder();

	protected String mSession;
	
	public ChannelReadThread( SocketChannel client, String session )
	{
		super( client );
		this.mSession = session;
	}

    ByteBuffer mBuffer = ByteBuffer.allocate( 128 );
    int mBytesRead = 0;

	@Override
	public void run()
	{
        for(;;)
        {
			if( null == this.mClient || !this.mClient.isConnected() || !this.mClient.isOpen() )
			{
				return;
			}

            this.mBytesRead = 0;

			try
			{
				int bytesRead;

	            while( 0 < ( bytesRead = this.mClient.read( this.mBuffer ) ) )
				{
	            	this.mBytesRead += bytesRead;
	            	
	            	this.mBuffer.flip();

	            	int bufferPosition = 0;
	            	
					for( byte b : this.mBuffer.array() )
					{
						if( handshakeComplete() )
						{
							webSocketVersion.request.setData( b );
						}
						else
						{
							this.mOutputBuffer.append( ( char ) b );
						}
						
						if( ++bufferPosition >= bytesRead )
						{
							break;
						}
					}

					this.mBuffer.clear();
				}

	            if( 0 < this.mBytesRead )
	            {
					if( !handshakeComplete() )
					{
						webSocketVersion = ( new WebsocketVersionFactory( this.mOutputBuffer.toString() ) ).getVersion();
						ChannelSocketPool.put( this.mSession, webSocketVersion.handshake.getResponse() );
						console( "Handshake Complete" );
					}
					else
					{
						ChannelSocketPool.put( this.mSession, webSocketVersion.process( this.mSession ) );
					}
	
					if( webSocketVersion.response.isClosed() )
					{
						console( "Websocket Closed" );
						this.mClient.close();
					}
	            }
	            Thread.sleep( 100L );
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
				return;
			}
			catch( WebsocketVersionException e )
			{
				e.printStackTrace();
			}
			catch( HandshakeException e )
			{
				e.printStackTrace();
			}
			catch( InterruptedException e )
			{
				e.printStackTrace();
			}
		}
	}
}