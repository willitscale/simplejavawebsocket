package uk.co.n3tw0rk.websocketregistration.framing.rfc6455;

import uk.co.n3tw0rk.websocketregistration.framing.DataFrame;

import uk.co.n3tw0rk.websocketregistration.wrappers.Abstraction;

/**
 * Data Frame
 * 
 * 	Abstract structure for sending and receiving framing data packets.
 * 
 * 	------------
 * 
 *	Base Framing Protocol
 *
 *		This wire format for the data transfer part is described by the ABNF
 *		[RFC5234] given in detail in this section.  (Note that, unlike in
 *		other sections of this document, the ABNF in this section is
 *		operating on groups of bits.  The length of each group of bits is
 *		indicated in a comment.  When encoded on the wire, the most
 *		significant bit is the leftmost in the ABNF).  A high-level overview
 *		of the framing is given in the following figure.  In a case of
 *		conflict between the figure below and the ABNF specified later in
 *		this section, the figure is authoritative.
 *		
 *		0                   1                   2                   3
 *		0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 *		+-+-+-+-+-------+-+-------------+-------------------------------+
 *		|F|R|R|R| opcode|M| Payload len |    Extended payload length    |
 *		|I|S|S|S|  (4)  |A|     (7)     |             (16/64)           |
 *		|N|V|V|V|       |S|             |   (if payload len==126/127)   |
 *		| |1|2|3|       |K|             |                               |
 *		+-+-+-+-+-------+-+-------------+ - - - - - - - - - - - - - - - +
 *		|     Extended payload length continued, if payload len == 127  |
 *		+ - - - - - - - - - - - - - - - +-------------------------------+
 *		|                               |Masking-key, if MASK set to 1  |
 *		+-------------------------------+-------------------------------+
 *		| Masking-key (continued)       |          Payload Data         |
 *		+-------------------------------- - - - - - - - - - - - - - - - +
 *		:                     Payload Data continued ...                :
 *		+ - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - +
 *		|                     Payload Data continued ...                |
 *		+---------------------------------------------------------------+
 *
 * @package uk.co.n3tw0rk.websocketregistration.framing
 * @version 0.1
 * @access public
 * @abstract
 * @author James Lockhart <james@n3tw0rk.co.uk>
 * @see http://tools.ietf.org/html/rfc6455#section-5.2
 */
public abstract class RFC6455_DataFrame extends Abstraction implements DataFrame 
{
	/**
	 * To Do :
	 *  
	 * 		-	Fragment Data Frames
	 * @see http://tools.ietf.org/html/rfc6455#section-5.4
	 *  
	 * 		-	Leave extensibility for additional operations
	 * @see http://tools.ietf.org/html/rfc6455#section-5.5
	 * @see http://tools.ietf.org/html/rfc6455#section-5.8
	 */


	/**
	 *
	 *	frame-payload-length	= ( %x00-7D )
	 *		/ ( %x7E frame-payload-length-16 )
	 *		/ ( %x7F frame-payload-length-63 )
	 *		; 7, 7+16, or 7+64 bits in length,
	 *		; respectively
	 *
	 *	frame-payload-length-16 = %x0000-FFFF 
	 *		; 16 bits in length
	 *	frame-payload-length-63 = %x0000000000000000-7FFFFFFFFFFFFFFF
	 *		; 64 bits in length
	 */
	
	public final static long SMALL_PAYLOAD = 0x7DL;
	public final static long MEDIUM_PAYLOAD = 0xFFFFL;
	public final static long LARGE_PAYLOAD = 0x7FFFFFFFFFFFFFFFL;

	public final static int SEVEN_BIT_PAYLOAD = 0x7D;
	public final static int TWO_BYTE_EXTENDED_PAYLOAD = 0x7E;
	public final static int EIGHT_BYTE_EXTENDED_PAYLOAD = 0x7F;

	/**
	 *	Payload length:  7 bits, 7+16 bits, or 7+64 bits
	 *
	 *		The length of the "Payload data", in bytes: if 0-125, that is the
	 *		payload length.  If 126, the following 2 bytes interpreted as a
	 *		16-bit unsigned integer are the payload length.  If 127, the
	 *		following 8 bytes interpreted as a 64-bit unsigned integer (the
	 *		most significant bit MUST be 0) are the payload length.  Multibyte
	 *		length quantities are expressed in network byte order.  Note that
	 *		in all cases, the minimal number of bytes MUST be used to encode
	 *		the length, for example, the length of a 124-byte-long string
	 *		can't be encoded as the sequence 126, 0, 124.  The payload length
	 *		is the length of the "Extension data" + the length of the
	 *		"Application data".  The length of the "Extension data" may be
	 *		zero, in which case the payload length is the length of the
	 *		"Application data".
	 */
	protected long PAYLOAD;

	/**
	 *	FIN:  1 bit
	 *
	 *		Indicates that this is the final fragment in a message.  
	 *		The first fragment MAY also be the final fragment.
	 */
	protected byte FIN;
	
	/**
	 *	RSV1, RSV2, RSV3:  1 bit each
	 *
	 *		MUST be 0 unless an extension is negotiated that defines meanings
	 *		for non-zero values.  If a nonzero value is received and none of
	 *		the negotiated extensions defines the meaning of such a nonzero
	 *		value, the receiving endpoint MUST _Fail the WebSocket
	 *		Connection_.
	 */
	protected byte RSV1;
	protected byte RSV2;
	protected byte RSV3;

	/**
	 *	Opcode:  4 bits
	 *
	 *		Defines the interpretation of the "Payload data".  If an unknown
	 *		opcode is received, the receiving endpoint MUST _Fail the
	 *		WebSocket Connection_.  The following values are defined.
	 *			%x0 denotes a continuation frame
	 *			%x1 denotes a text frame
	 *			%x2 denotes a binary frame
	 *			%x3-7 are reserved for further non-control frames
	 *			%x8 denotes a connection close
	 *			%x9 denotes a ping
	 *			%xA denotes a pong
	 *			%xB-F are reserved for further control frames
	 */
	protected byte OP_CODE;

	public final static byte OP_CODE_CONTINUATION_FRAME = 0x01;
	public final static byte OP_CODE_TEXT = 0x01;
	public final static byte OP_CODE_BINARY = 0x02;

	// Unused
	public final static byte OP_CODE_NON_CONTROL_3 = 0x03;
	public final static byte OP_CODE_NON_CONTROL_4 = 0x04;
	public final static byte OP_CODE_NON_CONTROL_5 = 0x05;
	public final static byte OP_CODE_NON_CONTROL_6 = 0x06;
	public final static byte OP_CODE_NON_CONTROL_7 = 0x07;
	
	public final static byte OP_CODE_CLOSE = 0x08;
	public final static byte OP_CODE_PING = 0x09;
	public final static byte OP_CODE_PONG = 0x0A;

	// Unused
	public final static byte OP_CODE_CONTROL_B = 0x0B;
	public final static byte OP_CODE_CONTROL_C = 0x0C;
	public final static byte OP_CODE_CONTROL_D = 0x0D;
	public final static byte OP_CODE_CONTROL_E = 0x0E;
	public final static byte OP_CODE_CONTROL_F = 0x0F;

	/**
	 *	Mask:  1 bit
	 *		Defines whether the "Payload data" is masked.  If set to 1, a
	 * 		masking key is present in masking-key, and this is used to unmask
	 * 		the "Payload data" as per Section 5.3.  All frames sent from
	 * 		client to server have this bit set to 1.
	 */
	protected byte MASK;
	
	/**
	 *	Masking-key:  0 or 4 bytes
	 *
	 *		All frames sent from the client to the server are masked by a
	 *		32-bit value that is contained within the frame.  This field is
	 *		present if the mask bit is set to 1 and is absent if the mask bit
	 *		is set to 0.  See Section 5.3 for further information on client-
	 *		to-server masking.
	 */
	public final static int MAX_MASK_BYTES = 0x4;

	protected int[] MASKING_KEY = new int[ MAX_MASK_BYTES ];

	/**
	 *	Payload data:  (x+y) bytes
	 *	
	 *		The "Payload data" is defined as "Extension data" concatenated
	 *		with "Application data".
	 *	
	 *	Extension data:  x bytes
	 *	
	 *		The "Extension data" is 0 bytes unless an extension has been
	 *		negotiated.  Any extension MUST specify the length of the
	 *		"Extension data", or how that length may be calculated, and how
	 *		the extension use MUST be negotiated during the opening handshake.
	 *		If present, the "Extension data" is included in the total payload
	 *		length.
	 *	
	 *	Application data:  y bytes
	 *		
	 *		Arbitrary "Application data", taking up the remainder of the frame
	 *		after any "Extension data".  The length of the "Application data"
	 *		is equal to the payload length minus the length of the "Extension
	 *		data".
	 */
	protected StringBuilder payloadData = null;

	public RFC6455_DataFrame(){}

	public boolean isFinal()
	{
		return ( 0x1 == this.FIN );
	}

	public byte getOPCode()
	{
		return this.OP_CODE;
	}

	public boolean isMasked()
	{
		return( RFC6455_DataFrame.ONE_BIT == this.MASK );
	}
	
	public String getPayload()
	{
		if( null == this.payloadData )
			return null;
		return this.payloadData.toString();
	}

	public int mask( int data, int offset )
	{
		return ( data ^ this.MASKING_KEY[ ( offset % RFC6455_DataFrame.MAX_MASK_BYTES ) ] );
	}
}
