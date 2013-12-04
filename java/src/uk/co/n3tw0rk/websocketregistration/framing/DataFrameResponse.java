package uk.co.n3tw0rk.websocketregistration.framing;

import uk.co.n3tw0rk.websocketregistration.utils.Utils;

public class DataFrameResponse extends DataFrame
{
	private int[] dataFrame;
	
	public DataFrameResponse()
	{
		this.payloadData = new StringBuilder();
		this.setBaseData();
		this.generateMaskingKey();
	}
	
	public void setBaseData()
	{
		this.FIN = ONE_BIT;
		this.RSV1 = NULL_BIT;
		this.RSV2 = NULL_BIT;
		this.RSV3 = NULL_BIT;
	}

	private void generateMaskingKey()
	{
		this.MASK = ONE_BIT;

		for( int i = 0; i < this.MASKING_KEY.length; i++ )
			this.MASKING_KEY[ i ] = Utils.generateMaskKeyPart();
	}

	public void setPayload( String payload )
	{
		this.setPayload( payload, OP_CODE_TEXT );
	}

	public void setPayload( String payload, byte op )
	{
		this.PAYLOAD = payload.length();
		this.payloadData = new StringBuilder( payload );

		this.OP_CODE = op;
	}

	public int[] getDataFrame()
	{
		this.buildDataFrame();
		return this.dataFrame;
	}
	
	public void buildDataFrame()
	{
		int packetLength = this.buildPacketLength();

		this.dataFrame = new int[ packetLength ];

		int byteIterator = 0;
		byteIterator = this.buildBaseByte( byteIterator );
		console( "i = " + byteIterator );
		byteIterator = this.buildMaskPayloadBytes( byteIterator );
		console( "i = " + byteIterator );
		byteIterator = this.buildPayloadDataBytes( byteIterator );
		console( "i = " + byteIterator );
	}
	
	public int buildBaseByte( int i )
	{
		this.dataFrame[ i ] = ( int ) ( this.dataFrame[ i ] | ( this.FIN << 7 ) );
		this.dataFrame[ i ] = ( int ) ( this.dataFrame[ i ] | ( this.RSV1 << 6 ) );
		this.dataFrame[ i ] = ( int ) ( this.dataFrame[ i ] | ( this.RSV2 << 5 ) );
		this.dataFrame[ i ] = ( int ) ( this.dataFrame[ i ] | ( this.RSV3 << 4 ) );
		this.dataFrame[ i ] = ( int ) ( this.dataFrame[ i ] | this.OP_CODE );
		
		return ( i + 1 );
	}
	
	public int buildMaskPayloadBytes( int i )
	{
		int payload = 0;
		
		long extendedPayloadDouble = 0;
		long extendedPayloadOctal = 0;

		if( SMALL_PAYLOAD >= this.PAYLOAD )
		{
			payload = ( int ) this.PAYLOAD;
		}
		else if( MEDIUM_PAYLOAD >= this.PAYLOAD )
		{
			extendedPayloadDouble = this.PAYLOAD;
			payload = DataFrame.TWO_BYTE_EXTENDED_PAYLOAD;
		}
		else
		{
			extendedPayloadOctal = this.PAYLOAD;
			payload = DataFrame.EIGHT_BYTE_EXTENDED_PAYLOAD;
		}

		this.dataFrame[ i ] = ( int ) ( this.dataFrame[ i ] | ( this.MASK << 7 ) );
		this.dataFrame[ i ] = ( int ) ( this.dataFrame[ i++ ] | payload );
		
		if( 0 < extendedPayloadDouble )
		{
			this.dataFrame[ i++ ] = ( int )( ( this.PAYLOAD >> 8 ) & 0xFFL );
			this.dataFrame[ i++ ] = ( int )( this.PAYLOAD & 0xFFL );
		}
		else if( 0 < extendedPayloadOctal )
		{
			this.dataFrame[ i++ ] = ( int )( ( this.PAYLOAD >> 56 ) & 0xFFL );
			this.dataFrame[ i++ ] = ( int )( ( this.PAYLOAD >> 48 ) & 0xFFL );
			this.dataFrame[ i++ ] = ( int )( ( this.PAYLOAD >> 40 ) & 0xFFL );
			this.dataFrame[ i++ ] = ( int )( ( this.PAYLOAD >> 32 ) & 0xFFL );
			this.dataFrame[ i++ ] = ( int )( ( this.PAYLOAD >> 24 ) & 0xFFL );
			this.dataFrame[ i++ ] = ( int )( ( this.PAYLOAD >> 16 ) & 0xFFL );
			this.dataFrame[ i++ ] = ( int )( ( this.PAYLOAD >> 8 ) & 0xFFL );
			this.dataFrame[ i++ ] = ( int )( this.PAYLOAD & 0xFFL );
		}
	
		if( ONE_BIT == this.MASK )
		{
			this.dataFrame[ i++ ] = ( int ) this.MASKING_KEY[ 0 ];
			this.dataFrame[ i++ ] = ( int ) this.MASKING_KEY[ 1 ];
			this.dataFrame[ i++ ] = ( int ) this.MASKING_KEY[ 2 ];
			this.dataFrame[ i++ ] = ( int ) this.MASKING_KEY[ 3 ];
		}

		return i;
	}
	
	public int buildPayloadDataBytes( int i )
	{
		byte[] payloadData = Utils.stringConvert( this.payloadData.toString() );

		for( int j = 0; j < payloadData.length; j++ )
			this.dataFrame[ i++ ] = this.mask( payloadData[ j ], j );

		return i;
	}
	
	public int buildPacketLength()
	{
		int dataFrameLength = 2;

		if( SMALL_PAYLOAD >= this.PAYLOAD ){}
		else if( MEDIUM_PAYLOAD > this.PAYLOAD )
			dataFrameLength += 2;
		else
			dataFrameLength += 8;
		
		dataFrameLength += ( int ) this.PAYLOAD;
		
		if( ONE_BIT == this.MASK )
			dataFrameLength += 4;

		return dataFrameLength;
	}
	
}
