function SocketClient()
{
//	this.url = url;
//	this.init();
};

SocketClient.prototype.url = null;
SocketClient.prototype.webSocket = null;

SocketClient.prototype.setURL = function( url )
{
	this.url = url;
};

SocketClient.prototype.init = function()
{
	this.reconnect();
};

SocketClient.prototype.reconnect = function()
{
	this.disconnect();
	this.connect();
};

SocketClient.prototype.connectURL = function( url )
{
	this.url = url;
	this.webSocket = new WebSocket( this.url );
	this.bindListeners();
};

SocketClient.prototype.connect = function()
{
	this.webSocket = new WebSocket( this.url );
	this.bindListeners();
};

SocketClient.prototype.bindListeners = function()
{
	var handler = this;

	this.webSocket.onopen = function( event )
	{
		console.log( event );
		handler.dataLog( 'Connected to : ' + handler.url );
		
	//	handler.send( "1234" );
	};
	
	this.webSocket.onmessage = function( event )
	{
		console.log( event );
		handler.dataLog( 'Message Received : ' + event.data );
	};
	
	this.webSocket.onclose = function( event )
	{
		console.log( event );
		handler.dataLog( 'Disconnected : Code - ' + event.code + ' Reason : ' + event.reason );
	};
	
	this.webSocket.onerror = function( event )
	{
		console.log( event );
		handler.dataLog( 'Error' );
	};
};

SocketClient.prototype.disconnect = function()
{
	if( null != this.webSocket )
		this.webSocket.close();
	this.webSocket = null;
};

SocketClient.prototype.send = function( data )
{
	this.dataLog( 'Sending : ' + data );
	this.webSocket.send( data );
};

SocketClient.prototype.dataLog = function( message )
{
	$( '#log-table tbody' ).append( '<tr><td><p>' + ( new Date() ).getTimeStamp() + '::' + message + '</p></td></tr>' );
};

SocketClient.prototype.clearLog = function()
{
	$( '#log-table tbody' ).html( '' );
};
