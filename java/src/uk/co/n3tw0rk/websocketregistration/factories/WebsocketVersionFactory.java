package uk.co.n3tw0rk.websocketregistration.factories;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.co.n3tw0rk.websocketregistration.structures.hixie75.*;
import uk.co.n3tw0rk.websocketregistration.structures.hixie76.*;
import uk.co.n3tw0rk.websocketregistration.structures.hybi00.*;
import uk.co.n3tw0rk.websocketregistration.structures.hybi07.*;
import uk.co.n3tw0rk.websocketregistration.structures.hybi10.*;
import uk.co.n3tw0rk.websocketregistration.structures.rfc6455.*;
import uk.co.n3tw0rk.websocketregistration.exceptions.HandshakeException;
import uk.co.n3tw0rk.websocketregistration.exceptions.WebsocketVersionException;
import uk.co.n3tw0rk.websocketregistration.structures.WebSocketVersion;
import uk.co.n3tw0rk.websocketregistration.wrappers.Abstraction;

public class WebsocketVersionFactory extends Abstraction
{
	private HashMap<String, String> headers;
	private String uri;
	
	public WebsocketVersionFactory( String header ) throws WebsocketVersionException
	{
		this.extractHeaders( header );
		this.validateHeaders();
	}
	
	private void extractHeaders( String header )
	{
		this.headers = new HashMap<String, String>();

		console( header );
		
		String headerParts [] = header.split( "\r\n" );

		
		for( int i = 0; i < headerParts.length; i++ )
		{
			if( headerParts[ i ].matches( "^GET /([^ ]*) (.*)" ) )
			{
				Pattern p = Pattern.compile( "^GET /([A-Za-z0-9_]+) (.*)" );
				Matcher m = p.matcher( headerParts[ i ] );

				if( m.find() )
				{
					this.uri = m.group( 1 );
					continue;
				}
				this.uri = "";
			}

			String parts [] = headerParts[ i ].split( ": " );
			
			if( 2 != parts.length )
			{
				console( "Invalid Header -- " + headerParts[ i ] );
				continue;
			}
			
			this.headers.put( parts[ 0 ].trim(), parts[ 1 ].trim() );
		}
		
	}
	
	public void validateHeaders() throws WebsocketVersionException
	{
		if( null == this.headers.get( "Sec-WebSocket-Version" ) )
			throw new WebsocketVersionException();
	}
	
	public WebSocketVersion getVersion() throws WebsocketVersionException, HandshakeException
	{
		if( null == this.headers.get( "Sec-WebSocket-Version" ) )
			throw new WebsocketVersionException();

		int version = Integer.parseInt( this.headers.get( "Sec-WebSocket-Version" ) );

		WebSocketVersion webSocketVersion = new WebSocketVersion();

		switch( version )
		{
			case 4 :
			{
				break;
			}
			case 5 :
			{
				break;
			}
			case 6 :
			{
				break;
			}
			case 7 :
			{
				break;
			}
			case 8 :
			{
				break;
			}
			case 9 :
			{
				break;
			}
			case 10 :
			{
				break;
			}
			case 11 :
			{
				break;
			}
			case 12 :
			{
				break;
			}
			case 13 :
			{
				webSocketVersion.handshake = new RFC6455_Handshake( this.headers, this.uri );
				webSocketVersion.request = new RFC6455_Request();
				webSocketVersion.response = new RFC6455_Response();
				break;
			}
			default :
			{
				// Unsupported Version
				throw new WebsocketVersionException();
			}
		}

		return webSocketVersion;
	}

}
