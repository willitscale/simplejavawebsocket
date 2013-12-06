package uk.co.n3tw0rk.websocketregistration.structures;

import java.nio.ByteBuffer;

public class WebSocketVersion
{
	public Handshake handshake;
	public Request request;
	public Response response;
	
	public byte[] process()
	{
		ByteBuffer byteBuffer = this.response.process( this.request );
		
		byte[] bytes = new byte[ byteBuffer.limit() ];

		for( int i = 0; i < byteBuffer.limit(); i++ )
			bytes[ i ] = byteBuffer.get( i );
		
		this.request.flush();
		
		return bytes;
	}
}
