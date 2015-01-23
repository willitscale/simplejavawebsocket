package uk.co.n3tw0rk.websocketregistration.structures;

import java.nio.ByteBuffer;

import uk.co.n3tw0rk.websocketregistration.framing.DataFrameResponse;
import uk.co.n3tw0rk.websocketregistration.wrappers.Abstraction;

public abstract class Response extends Abstraction
{
	protected boolean closedSent = false;

	protected DataFrameResponse dataFrameResponse;
	
	public abstract ByteBuffer process( Request request );
	
	public abstract ByteBuffer process( Request request, String session );

	public abstract boolean isClosed();
}
