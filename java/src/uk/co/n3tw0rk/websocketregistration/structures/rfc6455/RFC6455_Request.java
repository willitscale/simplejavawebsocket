package uk.co.n3tw0rk.websocketregistration.structures.rfc6455;

import uk.co.n3tw0rk.websocketregistration.framing.rfc6455.RFC6455_DataFrameRequest;
import uk.co.n3tw0rk.websocketregistration.structures.Request;

public class RFC6455_Request extends Request
{
	
	public RFC6455_Request()
	{
		this.flush();
	}

	@Override
	public void setData( int data )
	{
		console( "<< " + data );
		this.dataFrame.setFrameData( data );
	}

	@Override
	public void flush()
	{
		if( null == this.dataFrame || this.dataFrame.isFinal() )
			this.dataFrame = new RFC6455_DataFrameRequest();
	}

}
