package uk.co.n3tw0rk.websocketregistration.framing;

import uk.co.n3tw0rk.websocketregistration.wrappers.WSRAbstraction;

public abstract class DataFrame extends WSRAbstraction
{

	public final static byte DATA_FRAME_HEADER = 0x0;
	public final static byte DATA_FRAME_MASK_PAYLOAD_DATA = 0x01;
	public final static byte DATA_FRAME_EXTENDED_PAYLOAD = 0x2;
	public final static byte DATA_FRAME_MASKING_KEY = 0x4;
	public final static byte DATA_FRAME_MASKING_KEY_CONTINUED = 0x8;
	public final static byte DATA_FRAME_PAYLOAD_DATA = 0xF;

	protected int FIN;
	protected int RSV1;
	protected int RSV2;
	protected int RSV3;
	protected int OP_CODE;
	protected int MASK;
	protected int MASKING_KEY;

	protected int PAYLOAD;
	protected long PAYLOAD_EXTENDED;
	
	protected byte buildingDataSet = DATA_FRAME_HEADER;
	
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
