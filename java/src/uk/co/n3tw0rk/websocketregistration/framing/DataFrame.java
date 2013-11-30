package uk.co.n3tw0rk.websocketregistration.framing;

public class DataFrame
{

	public final static byte DATA_FRAME_HEADER = 0x0;

	public final static byte DATA_FRAME_EXTENDED_PAYLOAD = 0x1;

	public final static byte DATA_FRAME_MASKING_KEY = 0x2;

	public final static byte DATA_FRAME_MASKING_KEY_CONTINUED = 0x4;

	public final static byte DATA_FRAME_PAYLOAD_DATA = 0x8;

	private byte FIN;
	private byte RSV1;
	private byte RSV2;
	private byte RSV3;
	private byte OP_CODE;
	private byte MASK;
	private byte MASKING_KEY;
	private short PAYLOAD;
	private byte PAYLOAD_EXTENDED;
	
	private byte buildingDataSet = DATA_FRAME_HEADER;
	
	public DataFrame(){}
	
	public void setFrameData( int data )
	{
		switch( this.buildingDataSet )
		{
			case DATA_FRAME_HEADER :
			{
				this.FIN = ( byte ) ( data & 0x1 );
				this.RSV1 = ( byte ) ( ( data >> 1 ) & 0x1 );
				this.RSV2 = ( byte ) ( ( data >> 2 ) & 0x1 );
				this.RSV3 = ( byte ) ( ( data >> 3 ) & 0x1 );
				this.OP_CODE = ( byte ) ( ( data >> 4 ) & 0xF );
				this.MASK = ( byte ) ( ( data >> 8 ) & 0x1 );
				this.PAYLOAD = ( short ) ( ( data >> 9 ) & 0xFFFF );
				
				if( 0x7F == this.PAYLOAD )
					this.buildingDataSet = DATA_FRAME_EXTENDED_PAYLOAD;
				else if( 0x1 == this.MASK )
					this.buildingDataSet = DATA_FRAME_MASKING_KEY;
				else
					this.buildingDataSet = DATA_FRAME_PAYLOAD_DATA;

				System.out.println( "FIN : " + this.FIN );
				System.out.println( "RSV1 : " + this.RSV1 );
				System.out.println( "RSV2 : " + this.RSV2 );
				System.out.println( "RSV3 : " + this.RSV3 );
				System.out.println( "OP_CODE : " + this.OP_CODE );
				System.out.println( "MASK : " + this.MASK );
				System.out.println( "PAYLOAD : " + this.PAYLOAD );
				System.out.println( "----" );
				
				break;
			}
			
			case DATA_FRAME_EXTENDED_PAYLOAD :
			{
				this.PAYLOAD_EXTENDED = ( byte ) data;
				
				if( 0x1 == this.MASK )
					this.buildingDataSet = DATA_FRAME_MASKING_KEY;
				else
					this.buildingDataSet = DATA_FRAME_PAYLOAD_DATA;

				System.out.println( "PAYLOAD_EXTENDED : " + this.PAYLOAD_EXTENDED );
				System.out.println( "----" );

				break;
			}
			
			case DATA_FRAME_MASKING_KEY :
			{
				this.MASKING_KEY = ( byte ) data;

				this.buildingDataSet = DATA_FRAME_PAYLOAD_DATA;

				System.out.println( "MASKING_KEY : " + this.MASKING_KEY );
				System.out.println( "----" );

				break;
			}
		}

	}

	public boolean isFinal()
	{
		return ( 0x1 == this.FIN );
	}
	
	public byte getOPCode()
	{
		return this.OP_CODE;
	}
}
