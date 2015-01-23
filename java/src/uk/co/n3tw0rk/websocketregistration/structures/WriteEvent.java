package uk.co.n3tw0rk.websocketregistration.structures;

public class WriteEvent
{
	public byte[] mBuffer;
	
	public WriteEvent()
	{
		mBuffer = new byte[ 0 ];
	}
	
	public WriteEvent( String event )
	{
		this.mBuffer = event.getBytes();
	}
	
	public WriteEvent( byte[] event )
	{
		this.mBuffer = event;
	}
}