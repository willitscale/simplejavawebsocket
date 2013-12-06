package uk.co.n3tw0rk.websocketregistration.framing;

import java.nio.ByteBuffer;

public interface DataFrameResponse extends DataFrame
{
	public void setPayload( String payloadData, byte op );

	public ByteBuffer getDataFrame();
}