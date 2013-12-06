package uk.co.n3tw0rk.websocketregistration.utils;

import uk.co.n3tw0rk.websocketregistration.structures.Request;
import uk.co.n3tw0rk.websocketregistration.wrappers.AbstractionStatic;

public class RequestParser extends AbstractionStatic
{
	public static byte[] responseHeader( String request )
	{
		return buildResponse( parseRequest( request ) );
	}
	
	public static Request parseRequest( String request )
	{
		Request wsrRequest = new Request();

		console( request );
		
		String headers [] = request.split( "\r\n" );
		
		for( int i = 0; i < headers.length; i++ )
		{
			if( headers[ i ].matches( "^Host: (.*)" ) )
				wsrRequest.host = headers[ i ].replace( "Host: ", "" );
			else if( headers[ i ].matches( "^Origin: (.*)" ) )
				wsrRequest.origin = headers[ i ].replace( "Origin: ", "" );
			else if( headers[ i ].matches( "^Sec-WebSocket-Key: (.*)" ) )
				wsrRequest.socketKey = headers[ i ].replace( "Sec-WebSocket-Key: ", "" );
			else if( headers[ i ].matches( "^Sec-WebSocket-Key1: (.*)" ) )
				wsrRequest.socketKey1 = headers[ i ].replace( "Sec-WebSocket-Key1: ", "" );
			else if( headers[ i ].matches( "^Sec-WebSocket-Key2: (.*)" ) )
				wsrRequest.socketKey2 = headers[ i ].replace( "Sec-WebSocket-Key2: ", "" );
			else if( headers[ i ].matches( "^Sec-WebSocket-Version: (.*)" ) )
				wsrRequest.socketVersion = headers[ i ].replace( "Sec-WebSocket-Version: ", "" );
			else if( headers[ i ].matches( "^Sec-WebSocket-Extensions: (.*)" ) )
				wsrRequest.socketExtension = headers[ i ].replace( "Sec-WebSocket-Extensions: ", "" );
		}

		return wsrRequest;
	}
	
	public static byte[] buildResponse( Request wsrRequest )
	{
		String response = "HTTP/1.1 101 WebSocket Protocol Handshake\r\n" +
				"Upgrade: WebSocket\r\n" +
				"Connection: Upgrade\r\n" +
				//"Sec-WebSocket-Origin: " + wsrRequest.origin + "\r\n" +
				//"Sec-WebSocket-Location: " + wsrRequest.host + "\r\n" +
				"Sec-WebSocket-Accept: " + Utils.generateKey( wsrRequest.socketKey ) + "\r\n\r\n";
		
		return Utils.stringConvert( response );
	}
}
