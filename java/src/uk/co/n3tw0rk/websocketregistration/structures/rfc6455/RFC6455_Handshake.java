package uk.co.n3tw0rk.websocketregistration.structures.rfc6455;

import java.util.HashMap;

import uk.co.n3tw0rk.websocketregistration.exceptions.HandshakeException;
import uk.co.n3tw0rk.websocketregistration.structures.Handshake;
import uk.co.n3tw0rk.websocketregistration.utils.Utils;

public class RFC6455_Handshake extends Handshake
{
	public RFC6455_Handshake( HashMap<String, String> headers, String uri )
		throws HandshakeException
	{
		super( headers, uri );

		this.buildResponse();
	}

	@Override
	public byte[] getResponse()
	{
		return this.response;
	}
	
	@Override
	public String getURI()
	{
		return this.uri;
	//	GET /test HTTP/1.1	
	}

	@Override
	protected void buildResponse()
	{
		StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append( "HTTP/1.1 101 Switching Protocols" + Handshake.HEADER_EOL );
		
		stringBuilder.append( "Upgrade: WebSocket" + Handshake.HEADER_EOL );
		
		stringBuilder.append( "Connection: Upgrade" + Handshake.HEADER_EOL );

		if( this.headers.containsKey( "Origin" ) )
			stringBuilder.append("Sec-WebSocket-Origin: " + this.headers.get( "Origin" ) + Handshake.HEADER_EOL );
		
		if( this.headers.containsKey( "Host" ) )
			stringBuilder.append( "Sec-WebSocket-Location: " + this.headers.get( "Host" ) + Handshake.HEADER_EOL );

		stringBuilder.append( "Sec-WebSocket-Accept: " + Utils.generateKey( this.headers.get( "Sec-WebSocket-Key" ) ) + 
			Handshake.HEADER_EOL + Handshake.HEADER_EOL );

		this.response = Utils.stringConvert( stringBuilder.toString() );
		
		this.established = true;
	}

	@Override
	protected void validate()
		throws HandshakeException
	{
		if( !this.headers.containsKey( "Sec-WebSocket-Key" ) )
			throw new HandshakeException();
	}
}
