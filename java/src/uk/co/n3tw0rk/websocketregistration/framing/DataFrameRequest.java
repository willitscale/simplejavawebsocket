package uk.co.n3tw0rk.websocketregistration.framing;

public class DataFrameRequest extends DataFrame
{
	public final static byte DATA_NULL = 0xF;
	
	public final static byte DATA_FRAME_FIN_RSV_OPCODE_BYTE = 0x1;
	public final static byte DATA_FRAME_MASK_PAYLOAD_BYTE = 0x02;
	public final static byte DATA_FRAME_EXTENDED_PAYLOAD = 0x3;
	public final static byte DATA_FRAME_MASKING_KEY = 0x4;
	public final static byte DATA_FRAME_MASKING_KEY_CONTINUED = 0x5;
	public final static byte DATA_FRAME_PAYLOAD_DATA = 0x6;
	
	private byte buildingDataSet = DATA_FRAME_FIN_RSV_OPCODE_BYTE;
	private int maskBytes = 0;
	private int payloadExtended = 0;

	public DataFrameRequest()
	{
		this.payloadData = new StringBuilder();
	}

	public void setFrameData( int data )
	{
		switch( this.buildingDataSet )
		{
			case DATA_FRAME_FIN_RSV_OPCODE_BYTE :
			{
				this.finRsvOpcode( data );
				break;
			}
			case DATA_FRAME_MASK_PAYLOAD_BYTE :
			{
				this.maskPayload( data );
				break;
			}
			case DATA_FRAME_EXTENDED_PAYLOAD :
			{
				this.extendedPayload( data );
				break;
			}
			case DATA_FRAME_MASKING_KEY :
			{
				this.buildMaskingKey( data );
				break;
			}
			case DATA_FRAME_PAYLOAD_DATA :
			{
				this.buildingPayloadData( data );
				break;
			}
			case DATA_NULL :
			{
				break;
			}
		}
	}

	private void finRsvOpcode( int data )
	{
		this.FIN = ( byte ) ( ( data >> 7 ) & ONE_BIT );
		this.RSV1 = ( byte ) ( ( data >> 6 ) & ONE_BIT );
		this.RSV2 = ( byte ) ( ( data << 5 ) & ONE_BIT );
		this.RSV3 = ( byte ) ( ( data << 4 ) & ONE_BIT );
		this.OP_CODE = ( byte ) ( data & FOUR_BITS );

		if( OP_CODE_CLOSE == this.OP_CODE )
			this.buildingDataSet = DATA_NULL;
		else
			this.buildingDataSet = DATA_FRAME_MASK_PAYLOAD_BYTE;
	}

	private void maskPayload( int data )
	{
		this.MASK = ( byte ) ( ( data >> 7 ) & ONE_BIT );
		this.PAYLOAD = ( data & SEVEN_BITS );
		
		if( TWO_BYTE_EXTENDED_PAYLOAD == this.PAYLOAD )
		{
			this.buildingDataSet = DATA_FRAME_EXTENDED_PAYLOAD;
			this.payloadExtended = 2;
			this.PAYLOAD = 0;
		}
		else if( EIGHT_BYTE_EXTENDED_PAYLOAD == this.PAYLOAD )
		{
			this.buildingDataSet = DATA_FRAME_EXTENDED_PAYLOAD;
			this.payloadExtended = 8;
			this.PAYLOAD = 0;
		}
		else if( ONE_BIT == this.MASK )
			this.buildingDataSet = DATA_FRAME_MASKING_KEY;
		else
			this.buildingDataSet = DATA_FRAME_PAYLOAD_DATA;
	}
	
	private void extendedPayload( int data )
	{
		long tmpData = ( long ) data;
		this.PAYLOAD = ( tmpData << ( 8 * this.payloadExtended-- ) );

		if( 0 <= this.maskBytes )
			this.buildingDataSet = DATA_FRAME_PAYLOAD_DATA;
		else if( ONE_BIT == this.MASK )
			this.buildingDataSet = DATA_FRAME_MASKING_KEY;
		else
			this.buildingDataSet = DATA_FRAME_PAYLOAD_DATA;
	}

	private void buildMaskingKey( int data )
	{
		this.MASKING_KEY[ this.maskBytes++ ] = data;

		if( MAX_MASK_BYTES == this.maskBytes )
			this.buildingDataSet = DATA_FRAME_PAYLOAD_DATA;
	}

	private void buildingPayloadData( int data )
	{
		if( ONE_BIT == this.MASK )
			data = this.mask( data, this.payloadData.length() );

		this.payloadData.append( ( char ) data );
	}

}
