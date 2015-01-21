package uk.co.n3tw0rk.websocketregistration.threads;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import uk.co.n3tw0rk.websocketregistration.events.Event;
import uk.co.n3tw0rk.websocketregistration.exceptions.SocketServerException;
import uk.co.n3tw0rk.websocketregistration.pools.SocketPool;

public class ChannelWebSocketServer extends ChannelSocketServer
{

    public final static String CLIENT_CHANNEL = "clientChannel";
    public final static String SERVER_CHANNEL = "serverChannel";
    public final static String CHANNEL_TYPE = "channelType";
    
	protected ServerSocketChannel mSocketChannel;
	
	protected Selector mSelector;
	
	protected SelectionKey mSelectionKey;
	
	protected Map<String, String> mProperties = new HashMap<String, String>();
	
	/**
	 * Constructor 
	 * 
	 * @access public
	 * @constructor
	 * @param port
	 * @throws SocketServerException
	 * @throws IOException
	 */
	public ChannelWebSocketServer( int port, Event event )
		throws SocketServerException, IOException
	{
		if( !SocketPool.add( port ) )
		{
			throw new SocketServerException();
		}

		this.port = port;

		this.mSocketChannel = ServerSocketChannel.open();
		this.mSocketChannel.bind( new InetSocketAddress( "", port ) );
		this.mSocketChannel.configureBlocking(false);

        this.mSelector = Selector.open();
        
        this.mProperties.put( CHANNEL_TYPE, SERVER_CHANNEL );
        
        this.mSelectionKey = this.mSocketChannel.register( this.mSelector, SelectionKey.OP_ACCEPT );
        this.mSelectionKey.attach( this.mProperties );
	}

	/**
	 * 
	 */
	public void run()
	{
		console( "Server listening on port " + this.port );
        for(;;)
        {
            try
            {
				if( this.mSelector.select() == 0 )
				{
				    continue;
				}

	            Set<SelectionKey> selectedKeys = this.mSelector.selectedKeys();
	            Iterator<SelectionKey> iterator = selectedKeys.iterator();
	
	            while( iterator.hasNext() )
	            {
	                SelectionKey key = iterator.next();
	
	                if( ( ( Map<?,?> ) key.attachment() ).get( CHANNEL_TYPE ).equals( SERVER_CHANNEL ) )
	                {
	                    ServerSocketChannel serverSocketChannel = ( ServerSocketChannel ) key.channel();
	                    new Thread( new ChannelWebSocketClient( serverSocketChannel.accept(), this.mSelector ) ).start();
	                }
	
	                iterator.remove();
	            }
			}
            catch( IOException e )
            {
				e.printStackTrace();
			}
        }
	}
}
