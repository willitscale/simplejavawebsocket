package uk.co.n3tw0rk.websocketregistration.threads;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.channels.ServerSocketChannel;

import uk.co.n3tw0rk.websocketregistration.events.Event;
import uk.co.n3tw0rk.websocketregistration.exceptions.SocketServerException;
import uk.co.n3tw0rk.websocketregistration.pools.SocketPool;

public class ChannelSocketServer
{
	protected Event event;
	protected int port;

	protected boolean running = true;

}
