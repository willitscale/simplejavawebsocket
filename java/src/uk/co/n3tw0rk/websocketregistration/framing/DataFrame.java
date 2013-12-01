package uk.co.n3tw0rk.websocketregistration.framing;

import uk.co.n3tw0rk.websocketregistration.wrappers.WSRAbstraction;

public abstract class DataFrame extends WSRAbstraction
{
	public final static byte DATA_FRAME_FIN_RSV_OPCODE_BYTE = 0x0;
	public final static byte DATA_FRAME_MASK_PAYLOAD_BYTE = 0x01;
	public final static byte DATA_FRAME_EXTENDED_PAYLOAD_FIRST_BYTE = 0x2;
	public final static byte DATA_FRAME_EXTENDED_PAYLOAD_SECOND_BYTE = 0x3;
	public final static byte DATA_FRAME_MASKING_KEY = 0x4;
	public final static byte DATA_FRAME_MASKING_KEY_CONTINUED = 0x5;
	public final static byte DATA_FRAME_PAYLOAD_DATA = 0x6;

	protected final static int ONE_BIT = 0x1;
	protected final static int FOUR_BITS = 0xF;
	protected final static int SEVEN_BITS = 0x7F;

	protected final static int SEVEN_BIT_PAYLOAD = 0x7D;
	protected final static int TWO_BYTE_EXTENDED_PAYLOAD = 0x7E;
	protected final static int EIGHT_BYTE_EXTENDED_PAYLOAD = 0x7F;

	protected int FIN;
	protected int RSV1;
	protected int RSV2;
	protected int RSV3;
	protected int OP_CODE;
	protected int MASK;
	protected int MASKING_KEY;

	protected int PAYLOAD;
	protected long PAYLOAD_EXTENDED;
	
	protected byte buildingDataSet = DATA_FRAME_FIN_RSV_OPCODE_BYTE;
	
	protected StringBuilder stringBuilder = null;
	
	public DataFrame(){}
	
	public abstract void setFrameData( int data );

	public boolean isFinal()
	{
		return ( 0x1 == this.FIN );
	}
	
	public int getOPCode()
	{
		return this.OP_CODE;
	}
}
