package uk.co.n3tw0rk.websocketregistration.threads;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.Queue;

import uk.co.n3tw0rk.websocketregistration.exceptions.HandshakeException;
import uk.co.n3tw0rk.websocketregistration.exceptions.WebsocketVersionException;
import uk.co.n3tw0rk.websocketregistration.factories.WebsocketVersionFactory;

public class ChannelWebSocketClient extends ClannelSocketClient
{
	protected ReadThread mRead;
	protected WriteThread mWrite;
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

    	this.mRead = new ReadThread();
    	this.mWrite = new WriteThread();

    	( new Thread( this.mRead ) ).start();
    	( new Thread( this.mWrite ) ).start();
	}

	public class WriteEvent
	{
		public byte[] mBuffer;
		
		public WriteEvent()
		{
			mBuffer = new byte[ 0 ];
		}
		
		public WriteEvent( String event )
		{
			this.mBuffer = event.getBytes();
		}
		
		public WriteEvent( byte[] event )
		{
			this.mBuffer = event;
		}
	}

	/**
	 * <strong>Read Thread Class</strong>
	 * 
	 * @author James Lockhart <james@n3tw0rk.co.uk>
	 * @since 2015-01-13
	 */
	public class ReadThread implements Runnable
	{
		protected boolean mDiscrete = true;
		protected StringBuilder mOutputBuffer = new StringBuilder();

        ByteBuffer mBuffer = ByteBuffer.allocate( 128 );
        int mBytesRead = 0;

		@Override
		public void run()
		{
            for(;;)
            {
    			if( null == mClient || !mClient.isConnected() )
    			{
    				return;
    			}

	            this.mBytesRead = 0;

				try
				{
					int bytesRead;

		            while( 0 < ( bytesRead = mClient.read( this.mBuffer ) ) )
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
							mWrite.addEvent( webSocketVersion.handshake.getResponse() );
							console( "Handshake Complete" );
						}
						else
						{
							mWrite.addEvent( webSocketVersion.process() );
						}
		
						if( webSocketVersion.response.isClosed() )
						{
							console( "Websocket Closed" );
							mWrite.kill();
						}
		            }
		            Thread.sleep( 100L );
				}
				catch( IOException e )
				{
					e.printStackTrace();
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

	/**
	 * <strong>Write Thread Class</strong>
	 * 
	 * @author James Lockhart <james@n3tw0rk.co.uk>
	 * @since 2015-01-13
	 */
	public class WriteThread extends Thread
	{
		public Queue<WriteEvent> mWriteStack = new LinkedList<WriteEvent>();

		public synchronized void addEvent( byte[] eventBytes )
		{
			System.out.println( new StringBuilder().append( eventBytes ).toString() );
			this.mWriteStack.add( new WriteEvent( eventBytes ) );
			this.notify();
		}
		
		public synchronized void kill()
		{
			this.interrupt();

			try
			{
				mClient.close();
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
				if( null == mClient || !mClient.isConnected() )
				{
					return;
				}

				try
				{
					if( 0 < this.mWriteStack.size() )
					{
						WriteEvent event = this.mWriteStack.poll();
	
						if( null != event )
						{
							mClient.write( ByteBuffer.wrap( event.mBuffer ) );
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
				}
			}
		}
	}
}
