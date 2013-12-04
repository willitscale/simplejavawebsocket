package uk.co.n3tw0rk.websocketregistration.framing;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.Charset;

import uk.co.n3tw0rk.websocketregistration.utils.Utils;

public class DataFrameResponse extends DataFrame
{
	private IntBuffer intBuffer;
	
	private final static int MAX_HEADER = 28;
	
	private byte[] payloadDataBytes;
	
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
		this.payloadDataBytes = Utils.stringConvert( payload );

		this.PAYLOAD = this.payloadDataBytes.length;

		this.OP_CODE = op;
	}

	public IntBuffer getDataFrame()
	{
		this.buildDataFrame();

		this.intBuffer.flip();

		return this.intBuffer;
	}
	
	public void buildDataFrame()
	{
		this.intBuffer = IntBuffer.allocate( ( int ) this.PAYLOAD + DataFrameResponse.MAX_HEADER );

		this.buildBaseByte();

		this.buildMaskPayloadBytes();
		
		this.buildPayloadDataBytes();
	}
	
	public void buildBaseByte()
	{
		int tmpByte = 0x0;

		tmpByte = ( int ) ( tmpByte | ( this.FIN << 7 ) );
		tmpByte = ( int ) ( tmpByte | ( this.RSV1 << 6 ) );
		tmpByte = ( int ) ( tmpByte | ( this.RSV2 << 5 ) );
		tmpByte = ( int ) ( tmpByte | ( this.RSV3 << 4 ) );
		tmpByte = ( int ) ( tmpByte | this.OP_CODE );

		this.intBuffer.put( tmpByte );
	}
	
	public void buildMaskPayloadBytes()
	{
		int tmpByte = 0x0;

		tmpByte = ( int ) ( tmpByte | ( this.MASK << 7 ) );

		console( "payload " + this.PAYLOAD );
		
		if( SMALL_PAYLOAD < this.PAYLOAD )
		{
			tmpByte = ( int ) ( tmpByte | DataFrame.TWO_BYTE_EXTENDED_PAYLOAD );
			
			this.intBuffer.put( tmpByte );
			this.intBuffer.put( ( int )( ( this.PAYLOAD >> 8 ) & 0xFF ) );
			this.intBuffer.put( ( int )( this.PAYLOAD & 0xFF ) );
		}
		else if( MEDIUM_PAYLOAD < this.PAYLOAD )
		{
			tmpByte = ( int ) ( tmpByte | DataFrame.EIGHT_BYTE_EXTENDED_PAYLOAD );
			
			this.intBuffer.put( tmpByte );
			this.intBuffer.put(( int )( ( this.PAYLOAD >> 56 ) & 0xFF ) );
			this.intBuffer.put( ( int )( ( this.PAYLOAD >> 48 ) & 0xFF ) );
			this.intBuffer.put( ( int )( ( this.PAYLOAD >> 40 ) & 0xFF ) );
			this.intBuffer.put( ( int )( ( this.PAYLOAD >> 32 ) & 0xFF ) );
			this.intBuffer.put( ( int )( ( this.PAYLOAD >> 24 ) & 0xFF ) );
			this.intBuffer.put( ( int )( ( this.PAYLOAD >> 16 ) & 0xFF ) );
			this.intBuffer.put( ( int )( ( this.PAYLOAD >> 8 ) & 0xFF ) );
			this.intBuffer.put( ( int )( this.PAYLOAD & 0xFF ) );
		}
		else
		{
			tmpByte = ( int ) ( tmpByte | this.PAYLOAD );
			this.intBuffer.put( tmpByte );
		}

		if( ONE_BIT == this.MASK )
		{
			this.intBuffer.put( ( int ) this.MASKING_KEY[ 0 ] );
			this.intBuffer.put( ( int ) this.MASKING_KEY[ 1 ] );
			this.intBuffer.put( ( int ) this.MASKING_KEY[ 2 ] );
			this.intBuffer.put( ( int ) this.MASKING_KEY[ 3 ] );
		}

	}
	
	public void buildPayloadDataBytes()
	{
		for( int j = 0; j < this.payloadDataBytes.length; j++ )
		{
			if( this.isMasked() )
				this.intBuffer.put( ( int ) this.mask( this.payloadDataBytes[ j ], j ) );
			else
				this.intBuffer.put( this.payloadDataBytes[ j ] );
		}
	}

}
