package uk.co.n3tw0rk.websocketregistration.threads;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class ChannelWebSocketClient extends SocketClient
{
	protected ReadThread mRead;
	protected WriteThread mWrite;

	public ChannelWebSocketClient( Socket socket )
	{
		super( socket );
	}

	public void run()
	{
		try
		{
			console( "Client connected" );

			this.mRead = new ReadThread( this.socket.getInputStream() );
			this.mWrite = new WriteThread( this.socket.getOutputStream() );
			
			( new Thread( this.mRead ) ).start();
			( new Thread( this.mWrite ) ).start();
		}
		catch( Exception exception )
		{
			
		}
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
	}
	
	public class ReadThread implements Runnable
	{
		protected InputStream mIS;
		protected boolean mDiscrete = true;
		protected int mBuffer;
		protected StringBuilder mOutputBuffer = new StringBuilder();
		
		public ReadThread( InputStream is )
		{
			this.mIS = is;
		}

		@Override
		public void run()
		{
			if( null == socket )
			{
				return;
			}

			try
			{
				do
				{
					this.mBuffer = this.mIS.read();

					if( 0 < this.mIS.available() )
					{
						this.mDiscrete = false;
					}
					
					if( handshakeComplete() )
					{
						webSocketVersion.request.setData( this.mBuffer );
					}
					else
					{
						this.mOutputBuffer.append( ( char ) this.mBuffer );
					}
				}
				while( 0 != this.mIS.available() || this.mDiscrete );
				
				if( 0 < this.mOutputBuffer.length() )
				{
					mWrite.mWriteStack.add( new WriteEvent( this.mOutputBuffer.toString() ) );
					mWrite.notify();
					this.mOutputBuffer.setLength( 0 );
				}
			}
			catch( IOException e )
			{
				e.printStackTrace();
			}
			finally
			{
				this.run();
			}
		}
	}

	public class WriteThread implements Runnable
	{
		public Queue<WriteEvent> mWriteStack = new LinkedList<WriteEvent>();

		protected OutputStream mOS;
		
		public WriteThread( OutputStream os )
		{
			this.mOS = os;
		}
		
		@Override
		public void run()
		{
			if( null == socket )
			{
				return;
			}

			try
			{
				while( true )
				{
					if( 0 < this.mWriteStack.size() )
					{
						synchronized( socket )
						{
							WriteEvent event = this.mWriteStack.poll();

							if( null != event )
							{
								this.mOS.write( event.mBuffer );
								this.mOS.flush();
							}
						}
					}
					else
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
			finally
			{
				this.run();
			}
		}
	}
}
