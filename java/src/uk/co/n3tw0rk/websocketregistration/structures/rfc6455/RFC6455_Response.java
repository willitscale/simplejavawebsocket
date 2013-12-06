package uk.co.n3tw0rk.websocketregistration.structures.rfc6455;

import java.nio.ByteBuffer;

import uk.co.n3tw0rk.websocketregistration.framing.DataFrameRequest;
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
		
		this.dataFrameResponse.setPayload( dataFrame.getPayload(), dataFrame.getOPCode() );

		return this.dataFrameResponse.getDataFrame();
	}
}
