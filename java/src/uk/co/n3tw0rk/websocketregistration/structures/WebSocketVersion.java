package uk.co.n3tw0rk.websocketregistration.structures;

import java.nio.ByteBuffer;

public class WebSocketVersion
{
	public Handshake handshake;
	public Request request;
	public Response response;

	public byte[] process()
	{
		return this.process( null );
	}
	
	public byte[] process( String session )
	{
		ByteBuffer byteBuffer = this.response.process( this.request, session );
		
		if( null == byteBuffer )
		{
			return null;
		}

		byte[] bytes = new byte[ byteBuffer.limit() ];

		for( int i = 0; i < byteBuffer.limit(); i++ )
		{
			bytes[ i ] = byteBuffer.get( i );
		}

		this.request.flush();
		
		return bytes;
	}
}
