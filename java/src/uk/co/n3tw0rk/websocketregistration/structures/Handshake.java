package uk.co.n3tw0rk.websocketregistration.structures;

import java.util.HashMap;

import uk.co.n3tw0rk.websocketregistration.exceptions.HandshakeException;
import uk.co.n3tw0rk.websocketregistration.wrappers.Abstraction;

public abstract class Handshake extends Abstraction
{
	protected static String HEADER_EOL = "\r\n";
	
	protected String uri = null;

	protected byte[] response;
	
	protected boolean established = false;

	protected HashMap<String,String> headers;

	public Handshake( HashMap<String,String> headers, String uri ) throws HandshakeException
	{
		this.uri = uri;
		this.headers = headers;
		this.validate();
	}
	
	public boolean isEstablished()
	{
		return this.established;
	}
	
	protected abstract void buildResponse();
	
	protected abstract void validate() throws HandshakeException;

	public abstract byte[] getResponse();

	public abstract String getURI();
}
