package uk.co.n3tw0rk.websocketregistration.structures.rfc6455;

import java.nio.ByteBuffer;

import uk.co.n3tw0rk.websocketregistration.framing.DataFrame;
import uk.co.n3tw0rk.websocketregistration.framing.DataFrameRequest;
import uk.co.n3tw0rk.websocketregistration.framing.rfc6455.RFC6455_DataFrame;
import uk.co.n3tw0rk.websocketregistration.framing.rfc6455.RFC6455_DataFrameResponse;
import uk.co.n3tw0rk.websocketregistration.structures.Request;
import uk.co.n3tw0rk.websocketregistration.structures.Response;

public class RFC6455_Response extends Response
{
	@Override
	public ByteBuffer process( Request request )
	{
		// Need a frame for every response
		this.dataFrameResponse = new RFC6455_DataFrameResponse();
		
		DataFrameRequest dataFrame = request.getDataFrame();
		
		// Server must never mask a response
		this.dataFrameResponse.setMasked( false );

		switch( dataFrame.getOPCode() )
		{
			case RFC6455_DataFrame.OP_CODE_TEXT : 
			{
				this.dataFrameResponse.setOPCode( RFC6455_DataFrame.OP_CODE_TEXT );
				this.dataFrameResponse.setPayload( dataFrame.getPayload() );
				break;
			}
			
			case RFC6455_DataFrame.OP_CODE_BINARY : 
			{
				this.dataFrameResponse.setOPCode( RFC6455_DataFrame.OP_CODE_BINARY );
				this.dataFrameResponse.setPayload( dataFrame.getPayload() );
				break;
			}
			
			case RFC6455_DataFrame.OP_CODE_CLOSE : 
			{
				this.dataFrameResponse.setOPCode( RFC6455_DataFrame.OP_CODE_CLOSE );
				this.dataFrameResponse.setStatusCode( RFC6455_DataFrame.STATUS_CODE_NORMAL_CLOSURE );
				this.dataFrameResponse.setPayload( "Closed by client" );
				this.closedSent = true;
				break;
			}
			
			case RFC6455_DataFrame.OP_CODE_PING : 
			{
				this.dataFrameResponse.setOPCode( RFC6455_DataFrame.OP_CODE_PONG );
				this.dataFrameResponse.setPayload( dataFrame.getPayload() );
				break;
			}

			case RFC6455_DataFrame.OP_CODE_PONG : 
			{
				this.dataFrameResponse.setOPCode( RFC6455_DataFrame.OP_CODE_PING );
				this.dataFrameResponse.setPayload( dataFrame.getPayload() );
				break;
			}

			case RFC6455_DataFrame.OP_CODE_CONTINUATION_FRAME : 
			{
				// Do nothing
				return null;
			}
		}

		return this.dataFrameResponse.getDataFrame();
	}
	
	public boolean isClosed()
	{
		return this.closedSent;
	}
}