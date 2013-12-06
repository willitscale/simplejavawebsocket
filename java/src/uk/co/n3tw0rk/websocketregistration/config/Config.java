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
	 * @access public
	 * @var String
	 * @static
	 */
	public static String WEBSOCKET_VERSION = "13";

	/**
	 * @access public
	 * @var boolean
	 * @static
	 */
	public static boolean DEBUGGING_VERBOSE = true;

	/**
	 * @access public
	 * @var long
	 * @static
	 */
	public static long SLEEP_DELAY = 100000L;
}
