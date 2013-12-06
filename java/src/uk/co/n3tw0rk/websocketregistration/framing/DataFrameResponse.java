package uk.co.n3tw0rk.websocketregistration.framing;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import uk.co.n3tw0rk.websocketregistration.utils.Utils;

public class DataFrameResponse extends DataFrame
{
	/**
	 * Max Header attribute
	 *
	 * 	Maximum Byte Buffer Header Size 
	 * 
	 * 	Based off : 
	 * 
	 * 		Packet Part				|		Max Size Bits
	 * 	--------------------------------------------
	 * 		FIN 					|			1
	 * 		RSV1 					|			1
	 * 		RSV2 					|			1
	 * 		RSV3 					|			1
	 * 		Opcode 					|			4
	 * 		Opcode 					|			1
	 * 		Payload length			|			7
	 * 		Payload length extended |			64
	 * 		Masking-key				|			32
	 * 	--------------------------------------------
	 * 		Total Bits 				|			112
	 * 		Total Bytes				|			14
	 * 
	 * @final
	 * @static
	 * @access private
	 * @var int
	 */
	private final static int MAX_HEADER = 28;
	
	/**
	 * Payload Data Bytes attribute
	 * 
	 * @access private
	 * @var byte[]
	 */
	private byte[] payloadDataBytes;

	/**
	 * Byte Buffer attribute
	 * 
	 * @access private
	 * @var ByteBuffer
	 */
	private ByteBuffer byteBuffer;
	
	/**
	 * Data Frame Response Constructor
	 * 
	 * @access public
	 * @constructor
	 */
	public DataFrameResponse()
	{
		this.payloadData = new StringBuilder();
		this.setBaseData();
	}

	/**
	 * Generate Masking Key Method
	 * 
	 * @access private
	 * @return void
	 */
	public void setMasking( boolean mask )
	{
		if( mask )
		{
			this.MASK = DataFrame.ONE_BIT;

			for( int i = 0; i < this.MASKING_KEY.length; i++ )
				this.MASKING_KEY[ i ] = Utils.generateMaskKeyPart();
		}
		else
		{
			this.MASK = DataFrame.NULL_BIT;
		}
	}
	
	/**
	 * Generate Masking Key Method
	 * 
	 * @access public
	 * @return void
	 */
	public void setBaseData()
	{
		this.setBaseData( DataFrame.ONE_BIT );
	}
	
	/**
	 * Generate Masking Key Method
	 * 
	 * @access public
	 * @param int finalFrame
	 * @return void
	 */
	public void setBaseData( int finalFrame )
	{
		this.FIN = ( byte ) finalFrame;
		this.RSV1 = ( byte ) DataFrame.NULL_BIT;
		this.RSV2 = ( byte ) DataFrame.NULL_BIT;
		this.RSV3 = ( byte ) DataFrame.NULL_BIT;
	}

	/**
	 * Set Payload Method
	 * 
	 * @access public
	 * @param String payload
	 * @return void
	 */
	public void setPayload( String payload )
	{
		this.setPayload( payload, OP_CODE_TEXT );
	}

	/**
	 * Set Payload Method
	 * 
	 * @access public
	 * @param String payload
	 * @param byte op
	 * @return void
	 */
	public void setPayload( String payloadData, byte op )
	{
		this.payloadDataBytes = Utils.stringConvert( payloadData );

		this.PAYLOAD = this.payloadDataBytes.length;

		this.OP_CODE = op;
	}

	/**
	 * Build Data Frame method
	 * 
	 * @access public
	 * @return ByteBuffer
	 */
	public ByteBuffer getDataFrame()
	{
		this.buildDataFrame();

		this.byteBuffer.flip();

		return this.byteBuffer;
	}

	/**
	 * Build Data Frame method
	 * 
	 * @access private
	 * @return void
	 */
	public void buildDataFrame()
	{
		this.byteBuffer = ByteBuffer.allocate( ( int ) this.PAYLOAD + DataFrameResponse.MAX_HEADER );

		this.buildBaseByte();

		this.buildMaskPayloadBytes();
		
		this.buildPayloadDataBytes();
	}

	/**
	 * Build Base Byte method
	 * 
	 * @access private
	 * @return void
	 */
	private void buildBaseByte()
	{
		int tmpByte = 0x0;

		tmpByte |= ( byte ) ( this.FIN << 7 );
		tmpByte |= ( byte ) ( this.RSV1 << 6 );
		tmpByte |= ( byte ) ( this.RSV2 << 5 );
		tmpByte |= ( byte ) ( this.RSV3 << 4 );
		tmpByte |= ( byte ) this.OP_CODE;

		this.putByte( tmpByte );
	}
	
	/**
	 * Build Mask Payload Bytes method
	 * 
	 * @access private
	 * @return void
	 */
	private void buildMaskPayloadBytes()
	{
		byte tmpByte = 0x0;

		tmpByte = ( byte ) ( tmpByte | ( this.MASK << 7 ) );
		
		if( SMALL_PAYLOAD < this.PAYLOAD )
		{
			tmpByte |= ( byte ) DataFrame.TWO_BYTE_EXTENDED_PAYLOAD;
			
			this.putByte( tmpByte );
			this.putByte( ( int )( ( this.PAYLOAD >> 8 ) & 0xFF ) );
			this.putByte( ( int )( this.PAYLOAD & 0xFF ) );
		}
		else if( MEDIUM_PAYLOAD < this.PAYLOAD )
		{
			tmpByte |= ( byte ) DataFrame.EIGHT_BYTE_EXTENDED_PAYLOAD;
			
			this.putByte( tmpByte );
			
			this.putByte( ( int )( ( this.PAYLOAD >> 56 ) & 0xFF ) );
			this.putByte( ( int )( ( this.PAYLOAD >> 48 ) & 0xFF ) );
			this.putByte( ( int )( ( this.PAYLOAD >> 40 ) & 0xFF ) );
			this.putByte( ( int )( ( this.PAYLOAD >> 32 ) & 0xFF ) );
			this.putByte( ( int )( ( this.PAYLOAD >> 24 ) & 0xFF ) );
			this.putByte( ( int )( ( this.PAYLOAD >> 16 ) & 0xFF ) );
			this.putByte( ( int )( ( this.PAYLOAD >> 8 ) & 0xFF ) );
			this.putByte( ( int )( this.PAYLOAD & 0xFF ) );
		}
		else
		{
			tmpByte |= ( byte ) this.PAYLOAD;
			this.putByte( tmpByte );
		}

		if( ONE_BIT == this.MASK )
		{
			this.putByte( this.MASKING_KEY[ 0 ] );
			this.putByte( this.MASKING_KEY[ 1 ] );
			this.putByte( this.MASKING_KEY[ 2 ] );
			this.putByte( this.MASKING_KEY[ 3 ] );
		}

	}
	
	/**
	 * Build Payload Data Bytes method
	 * 
	 * @access private
	 * @return void
	 */
	private void buildPayloadDataBytes()
	{
		for( int j = 0; j < this.payloadDataBytes.length; j++ )
		{
			if( this.isMasked() )
				this.putByte( this.mask( this.payloadDataBytes[ j ], j ) );
			else
				this.putByte( this.payloadDataBytes[ j ] );
		}
	}

	/**
	 * 	Put Byte method
	 * 
	 * @access private
	 * @param tmpByte
	 * @return void
	 */
	private void putByte( int tmpByte )
	{
		console( "byte >> " + ( byte ) tmpByte );

		this.byteBuffer.put( ( byte ) tmpByte );
		
		/*
		if( -128 <= tmpByte && 127 >= tmpByte )
		{
			console( "byte >> " + tmpByte );

			this.byteBuffer.put( ( byte ) tmpByte );
		}
		else if( -32768 <= tmpByte && 32767 >= tmpByte )
		{
			console( "short -- byte >> " + tmpByte );
			console( "short >> " + ( byte ) ( ( tmpByte >> 4 ) & 0xFF ) );
			console( "short >> " + ( byte ) ( tmpByte & 0xFF ) );

			this.byteBuffer.put( ( byte ) ( ( tmpByte >> 4 ) & 0xFF ) );
			this.byteBuffer.put( ( byte ) ( tmpByte & 0xFF ) );
		}
		else if( -2147483648 <= tmpByte && 2147483647 >= tmpByte )
		{
			console( "int" );
			this.byteBuffer.put( ( byte ) ( ( tmpByte >> 28 ) & 0xFF ) );
			this.byteBuffer.put( ( byte ) ( ( tmpByte >> 24 ) & 0xFF ) );
			this.byteBuffer.put( ( byte ) ( ( tmpByte >> 20 ) & 0xFF ) );
			this.byteBuffer.put( ( byte ) ( ( tmpByte >> 16 ) & 0xFF ) );
			this.byteBuffer.put( ( byte ) ( ( tmpByte >> 12 ) & 0xFF ) );
			this.byteBuffer.put( ( byte ) ( ( tmpByte >> 8 ) & 0xFF ) );
			this.byteBuffer.put( ( byte ) ( ( tmpByte >> 4 ) & 0xFF ) );
			this.byteBuffer.put( ( byte ) ( tmpByte & 0xFF ) );
		} */

	}

}
