package uk.co.n3tw0rk.websocketregistration.framing;

public class DataFrameBuilder extends DataFrame
{

	@Override
	public void setFrameData( int data )
	{
		switch( this.buildingDataSet )
		{
			case DATA_FRAME_HEADER :
			{
				this.buildHeader( data );
				break;
			}
			case DATA_FRAME_MASK_PAYLOAD_DATA :
			{
				this.maskPayload( data );
				break;
			}
			case DATA_FRAME_EXTENDED_PAYLOAD :
			{
				this.buildExtendedPayload( data );
				break;
			}
			case DATA_FRAME_MASKING_KEY :
			{
				this.buildMaskingKey( data );
				break;
			}
		}
	}

	private void buildHeader( int data )
	{
		this.FIN = ( data & 0x1 );
		this.RSV1 = ( ( data >> 1 ) & 0x1 );
		this.RSV2 = ( ( data >> 2 ) & 0x1 );
		this.RSV3 = ( ( data >> 3 ) & 0x1 );
		this.OP_CODE = ( ( data >> 4 ) & 0xF );

		this.MASK = ( data & 0x1 );
		this.PAYLOAD = ( ( data >> 1 ) & 0x7F );

		this.buildingDataSet = DATA_FRAME_MASK_PAYLOAD_DATA;
	}
	
	private void maskPayload( int data )
	{

		if( 0x7F == this.PAYLOAD )
			this.buildingDataSet = DATA_FRAME_EXTENDED_PAYLOAD;
		else if( 0x1 == this.MASK )
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
	
	private void buildExtendedPayload( int data )
	{
		this.PAYLOAD_EXTENDED = data;
		
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

}
