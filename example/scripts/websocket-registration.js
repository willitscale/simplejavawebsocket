function SocketClient( url )
{
	this.url = url;
	this.init();
};

SocketClient.prototype.url = null;
SocketClient.prototype.webSocket = null;

SocketClient.prototype.init = function()
{
	this.reconnect();
};

SocketClient.prototype.reconnect = function()
{
	this.disconnect();
	this.connect();
};

SocketClient.prototype.connect = function()
{
	this.webSocket = new WebSocket( this.url );
	this.bindListeners();
};

SocketClient.prototype.bindListeners = function()
{
	var handler = this;

	this.webSocket.onopen = function (event)
	{
		console.log( event );
		handler.send( 'test' );
	};
	
	this.webSocket.onmessage = function( event )
	{
		console.log( event );
	};
	
	this.webSocket.onclose = function( event )
	{
		console.log( event );
	};
	
	this.webSocket.error = function( event )
	{
		console.log( event );
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
	console.log( 'Sending : ' + data );
	this.webSocket.send( data );
};
