package uk.co.n3tw0rk.websocketregistration.events;

public class EventRegistration extends Event
{
	@Override
	public String event( String data, String session )
	{
		return "";
	}

	@Override
	public String event( String data )
	{
		return this.event( data, null );
	}	

}
