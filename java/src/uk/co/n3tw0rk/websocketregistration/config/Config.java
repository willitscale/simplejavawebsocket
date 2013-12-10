package uk.co.n3tw0rk.websocketregistration.config;

import uk.co.n3tw0rk.websocketregistration.wrappers.Abstraction;

/**
 * Config Object
 * 
 * @package uk.co.n3tw0rk.websocketregistration.config
 * @version 0.1
 * @access public
 * @author James Lockhart <james@n3tw0rk.co.uk>
 */
public class Config extends Abstraction
{
	/**
	 * @access public
	 * @var String
	 * @static
	 */
	public static String SERVER_KEY = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";

	/**
	 * Generate a Self Signed Certificate
	 * keytool -genkey -keyalg RSA -keystore keystore.jks -storepass password -validity 360 -keysize 2048
	 * 
	 * @see http://www.sslshopper.com/article-how-to-create-a-self-signed-certificate-using-java-keytool.html
	 * 
	 * Generate a Java keystore and key pair
	 * keytool -genkey -alias mydomain -keyalg RSA -keystore keystore.jks -keysize 2048
	 * 
	 * Generate a certificate signing request (CSR) for an existing Java keystore
	 * keytool -certreq -alias mydomain -keystore keystore.jks -file mydomain.csr
	 * 
	 * Import a root or intermediate CA certificate to an existing Java keystore
	 * keytool -import -trustcacerts -alias root -file Thawte.crt -keystore keystore.jks
	 * 
	 * Import a signed primary certificate to an existing Java keystore
	 * keytool -import -trustcacerts -alias mydomain -file mydomain.crt -keystore keystore.jks
	 * 
	 * Generate a keystore and self-signed certificate (see How to Create a Self Signed Certificate using Java Keytool for more info)
	 * keytool -genkey -keyalg RSA -alias selfsigned -keystore keystore.jks -storepass password -validity 360 -keysize 2048
	 * 
	 * @see http://www.sslshopper.com/article-most-common-java-keytool-keystore-commands.html
	 */
	
	/**
	 * @access public
	 * @var String
	 * @static
	 */
	public static char[] CERT_PASSWORD = { 't', 'e', 's', 't', 'i', 'n', 'g' };

	/**
	 * @access public
	 * @var String
	 * @static
	 */
	public static String CERT_PATH = "keys/keystore.jks";

	/**
	 * KeyStore Types
	 * 
	 * 		Type		Description
	 * 
	 *		jceks		The proprietary keystore implementation provided by the SunJCE provider.
	 *		jks			The proprietary keystore implementation provided by the SUN provider.
	 *		pkcs12		The transfer syntax for personal identity information as defined in PKCS #12.
	 * 
	 * @see http://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyStore
	 * @access public
	 * @var String
	 * @static
	 */
	public static String CERT_TYPE = "JKS";

	/**
	 * KeyStore Types
	 * 
	 * 		Type		Description
	 * 
	 *		jceks		The proprietary keystore implementation provided by the SunJCE provider.
	 *		jks			The proprietary keystore implementation provided by the SUN provider.
	 *		pkcs12		The transfer syntax for personal identity information as defined in PKCS #12.
	 * 
	 * @see http://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyStore
	 * @access public
	 * @var String
	 * @static
	 */
	public static String SSL_TYPE = "TLS";

	/**
	 * @access public
	 * @var String
	 * @static
	 */
	public static String WEBSOCKET_VERSION = "13";

	/**
	 * @access public
	 * @var boolean
	 * @static
	 * @final
	 */
	public final static boolean DEBUGGING_VERBOSE = true;

	/**
	 * @access public
	 * @var long
	 * @static
	 */
	public final static long SLEEP_DELAY = 100000L;

	/**
	 * @access public
	 * @var int
	 * @static
	 */
	public final static int SERVER_SOCKET = 443;
}
