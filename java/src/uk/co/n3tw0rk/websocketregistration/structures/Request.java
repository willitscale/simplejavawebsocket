package uk.co.n3tw0rk.websocketregistration.structures;

import uk.co.n3tw0rk.websocketregistration.framing.DataFrameRequest;
import uk.co.n3tw0rk.websocketregistration.wrappers.Abstraction;

public abstract class Request extends Abstraction
{
	protected DataFrameRequest dataFrame;
	
	public abstract void setData( int buffer );
	
	public DataFrameRequest getDataFrame()
	{
		return this.dataFrame;
	}
	
	public abstract void flush();
}
