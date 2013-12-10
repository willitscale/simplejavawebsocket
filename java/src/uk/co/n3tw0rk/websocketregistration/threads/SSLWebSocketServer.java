package uk.co.n3tw0rk.websocketregistration.threads;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;

import uk.co.n3tw0rk.websocketregistration.config.Config;
import uk.co.n3tw0rk.websocketregistration.events.Event;
import uk.co.n3tw0rk.websocketregistration.exceptions.SocketServerException;
import uk.co.n3tw0rk.websocketregistration.wrappers.AbstractionThread;

/**
 * SSL WebSocket Server Object
 * 
 * @package uk.co.n3tw0rk.websocketregistration.threads
 * @version 0.1
 * @access public
 * @author James Lockhart <james@n3tw0rk.co.uk>
 */
public class SSLWebSocketServer extends WebSocketServer
{

	/**
	 * Constructor 
	 * 
	 * @access public
	 * @constructor
	 * @param port
	 * @throws SocketServerException
	 * @throws IOException
	 * @throws KeyStoreException 
	 * @throws CertificateException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 * @throws UnrecoverableKeyException 
	 */
	public SSLWebSocketServer( int port, Event event )
		throws SocketServerException, IOException, 
			KeyStoreException, NoSuchAlgorithmException, 
			CertificateException, KeyManagementException, 
			UnrecoverableKeyException
	{
		
		/**
		 * To generate a keystore file :
		 * 	keytool -genkey -alias simplejavawebsocket -keyalg RSA -keystore keystore.jks -keysize 2048
		 */
		
		this.port = port;
		
		this.keyStore = KeyStore.getInstance( Config.CERT_TYPE );

		InputStream inputStream = new FileInputStream( Config.CERT_PATH );
		this.keyStore.load( inputStream, Config.CERT_PASSWORD );
		inputStream.close();

		this.keyManagerFactory = KeyManagerFactory.getInstance( KeyManagerFactory.getDefaultAlgorithm() );
		this.keyManagerFactory.init( this.keyStore, Config.CERT_PASSWORD );

		this.sslContext = SSLContext.getInstance( Config.SSL_TYPE );
		this.sslContext.init( keyManagerFactory.getKeyManagers(), null, null );

		this.sslServerSocketFactory = this.sslContext.getServerSocketFactory();
		
		this.sslServerSocket = ( SSLServerSocket )this.sslServerSocketFactory.createServerSocket( this.port );

	}
	
	private KeyStore keyStore;
	private KeyManagerFactory keyManagerFactory;
	private SSLContext sslContext;
	private SSLServerSocketFactory sslServerSocketFactory;
	private SSLServerSocket sslServerSocket;

	public void run()
	{
		console( "Server listening on port " + this.port );

		while( this.running )
		{
			try
			{	
				( new SSLWebSocketClient( ( SSLSocket ) this.sslServerSocket.accept() ) ).start();
			}
			catch( IOException e )
			{
				e.printStackTrace();
			}
		}
        //ss.close();
	}
	
}
