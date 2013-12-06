package uk.co.n3tw0rk.websocketregistration.structures;

import java.nio.ByteBuffer;

import uk.co.n3tw0rk.websocketregistration.framing.DataFrameResponse;
import uk.co.n3tw0rk.websocketregistration.wrappers.Abstraction;

public abstract class Response extends Abstraction
{
	protected DataFrameResponse dataFrameResponse;
	
	public abstract ByteBuffer process( Request request );

}
