package uk.co.n3tw0rk.websocketregistration.framing;

import java.nio.ByteBuffer;


public interface DataFrameResponse extends DataFrame
{
	public ByteBuffer getDataFrame();

	public void setFinal( byte fin );
	
	public boolean isFinal();
	
	public void setOPCode( byte opcode );
	
	public byte getOPCode();
	
	public void setMasked( boolean masked );
	
	public boolean isMasked();
	
	public void setPayload( String payloadData );
	
	public String getPayload();
	
	public void setStatusCode( int code );
	
	public int getStatusCode();
}