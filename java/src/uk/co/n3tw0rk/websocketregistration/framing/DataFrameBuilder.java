package uk.co.n3tw0rk.websocketregistration.framing;

public class DataFrameBuilder extends DataFrame
{

	public DataFrameBuilder()
	{
		this.stringBuilder = new StringBuilder();
	}
	
	@Override
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
			case DATA_FRAME_EXTENDED_PAYLOAD_FIRST_BYTE :
			{
				this.extendedPayloadFirstByte( data );
				break;
			}
			case DATA_FRAME_EXTENDED_PAYLOAD_SECOND_BYTE :
			{
				this.extendedPayloadSecondByte( data );
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
		}
	}

	private void finRsvOpcode( int data )
	{
		this.FIN = ( data & ONE_BIT );
		this.RSV1 = ( ( data >> 1 ) & ONE_BIT );
		this.RSV2 = ( ( data >> 2 ) & ONE_BIT );
		this.RSV3 = ( ( data >> 3 ) & ONE_BIT );
		console( data >> 4 );
		this.OP_CODE = ( ( data >> 4 ) & FOUR_BITS );

		this.buildingDataSet = DATA_FRAME_MASK_PAYLOAD_BYTE;
	}

	private void maskPayload( int data )
	{

		this.MASK = ( data & ONE_BIT );
		this.PAYLOAD = ( ( data >> 1 ) & SEVEN_BITS );

		if( TWO_BYTE_EXTENDED_PAYLOAD == this.PAYLOAD )
			this.buildingDataSet = DATA_FRAME_EXTENDED_PAYLOAD_FIRST_BYTE;
		if( EIGHT_BYTE_EXTENDED_PAYLOAD == this.PAYLOAD )
			this.buildingDataSet = DATA_FRAME_EXTENDED_PAYLOAD_FIRST_BYTE;
		else if( ONE_BIT == this.MASK )
			this.buildingDataSet = DATA_FRAME_MASKING_KEY;
		else
			this.buildingDataSet = DATA_FRAME_PAYLOAD_DATA;

		this.console( "FIN : " + this.FIN );
		this.console( "RSV1 : " + this.RSV1 );
		this.console( "RSV2 : " + this.RSV2 );
		this.console( "RSV3 : " + this.RSV3 );
		this.console( "OP_CODE : " + this.OP_CODE );
		this.console( "MASK : " + this.MASK );
		this.console( "PAYLOAD : " + this.PAYLOAD );
		this.console( "----" );
	}
	
	private void extendedPayloadFirstByte( int data )
	{
		this.PAYLOAD_EXTENDED = data << 8;
		this.buildingDataSet = DATA_FRAME_EXTENDED_PAYLOAD_FIRST_BYTE;

		this.console( "PAYLOAD_EXTENDED : " + this.PAYLOAD_EXTENDED );
		this.console( "----" );
	}
	
	private void extendedPayloadSecondByte( int data )
	{
		this.PAYLOAD_EXTENDED = this.PAYLOAD_EXTENDED | data;
		
		if( 0x1 == this.MASK )
			this.buildingDataSet = DATA_FRAME_MASKING_KEY;
		else
			this.buildingDataSet = DATA_FRAME_PAYLOAD_DATA;

		this.console( "PAYLOAD_EXTENDED : " + this.PAYLOAD_EXTENDED );
		this.console( "----" );
	}
	
	private void buildMaskingKey( int data )
	{
		this.MASKING_KEY = data;

		this.buildingDataSet = DATA_FRAME_PAYLOAD_DATA;

		this.console( "MASKING_KEY : " + this.MASKING_KEY );
		this.console( "----" );
	}
	
	private byte[] byteBuffer;
	
	private void buildingPayloadData( int data )
	{
		
		console( data );
		//console( ( char ) data );
		//console( ( char ) ( data >> 2 ) );
		//console( ( char ) ( data << 2 ) );
		
		this.stringBuilder.append( ( char ) data );

	}
	
	public void getPayload()
	{
		if( 0 < this.stringBuilder.length() )
		{
			console( this.stringBuilder.length() );
			console( this.stringBuilder.toString() );
		}
	}

}
