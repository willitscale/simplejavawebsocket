package uk.co.n3tw0rk.websocketregistration.threads;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

import uk.co.n3tw0rk.websocketregistration.exceptions.HandshakeException;
import uk.co.n3tw0rk.websocketregistration.exceptions.WebsocketVersionException;
import uk.co.n3tw0rk.websocketregistration.factories.WebsocketVersionFactory;

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
			if( null == socket || socket.isClosed() )
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

					// WebSocket Error as all data should be in single singed byte range ( 0 - 255 )
					if( -1 >= this.mBuffer || 256 <= this.mBuffer )
					{
						console( "Websocket Error" );
						break;
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
					webSocketVersion = ( new WebsocketVersionFactory( this.mOutputBuffer.toString() ) ).getVersion();
					mWrite.addEvent( webSocketVersion.handshake.getResponse() );
					this.mOutputBuffer.setLength( 0 );
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
			finally
			{
				this.run();
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

		protected OutputStream mOS;
		
		public WriteThread( OutputStream os )
		{
			this.mOS = os;
		}
		
		public synchronized void addEvent( byte[] eventBytes )
		{
			System.out.println( "addEvent 1" );
			this.mWriteStack.add( new WriteEvent( eventBytes ) );
			System.out.println( "addEvent 2" );
			this.notify();
			System.out.println( "addEvent 3" );
		}
		
		public synchronized void kill()
		{
			this.interrupt();

			try
			{
				socket.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			this.notify();
		}
		
		@Override
		public void run()
		{
			if( null == socket || socket.isClosed() )
			{
				return;
			}

			System.out.println( "THREAD ITERATION" );
			
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
						synchronized( this.mWriteStack )
						{
							this.wait();
						}
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
			//	this.run();
			}
		}
	}
}
