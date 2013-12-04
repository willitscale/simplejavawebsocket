package uk.co.n3tw0rk.websocketregistration.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

import uk.co.n3tw0rk.websocketregistration.config.Config;
import uk.co.n3tw0rk.websocketregistration.wrappers.Abstraction;

public class Utils extends Abstraction
{
	public static String generateKey( String clientKey )
	{
		byte [] keyHash = sha1( clientKey + Config.SERVER_KEY );
		
		return base64Encode( keyHash );
	}

	public static String base64Decode( byte [] s )
	{
		return StringUtils.newStringUtf8( s );
	}

	public static String base64Encode( byte [] s )
	{
		return Base64.encodeBase64String( s );
	}

	public static byte[] md5( String md5 )
	{
		MessageDigest md = null;
		byte bytes[] = md5.getBytes();
		try
		{
			md = MessageDigest.getInstance( "MD5" );
		}
		catch( NoSuchAlgorithmException e )
		{
			e.printStackTrace();
		} 
		return md.digest( bytes );
	}
	public static byte[] sha1( String sha1 )
	{
		MessageDigest md = null;

		byte bytes[] = sha1.getBytes();

		try
		{
			md = MessageDigest.getInstance( "SHA-1" );
		}
		catch( NoSuchAlgorithmException e )
		{
			e.printStackTrace();
		} 
		return md.digest( bytes );
	}
	
	public static String byteConvert( byte[] b )
	{
		String result = "";

		for( int i = 0; i < b.length; i++ )
			result += Integer.toString( ( b[ i ] & 0xff ) + 0x100, 16 ).substring( 1 );

		return result;
	}
	
	public static byte[] stringConvert( String s )
	{
		return s.getBytes();
	}

	public static int generateMaskKeyPart()
	{
		return ( int )( Math.random() * ( ( ( ( int ) ( Byte.MAX_VALUE ) * 2 ) - 0) + 1) );
	}
	
}
