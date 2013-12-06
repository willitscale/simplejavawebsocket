package uk.co.n3tw0rk.websocketregistration.framing;

/**
 * Data Frame
 * 
 * 	Abstract structure for sending and receiving framing data packets.
 * @package uk.co.n3tw0rk.websocketregistration.framing
 * @version 0.1
 * @access public
 * @interface
 * @author James Lockhart <james@n3tw0rk.co.uk>
 * @see http://tools.ietf.org/html/rfc6455#section-5.2
 */
public interface DataFrame
{
	public final static int NULL_BIT = 0x0;
	public final static int ONE_BIT = 0x1;
	public final static int FOUR_BITS = 0xF;
	public final static int SEVEN_BITS = 0x7F;
	public final static int ONE_BYTE = 0xFF;
	

	public boolean isFinal();
	public byte getOPCode();
	public boolean isMasked();
	public String getPayload();
}
