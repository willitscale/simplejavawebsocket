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

	this.webSocket.onopen = function (e)
	{
		console.log( e );
		handler.send( 'test' );
	};
	
	this.webSocket.onmessage = function( e )
	{
		console.log( e );
	};
	
	this.webSocket.onclose = function( e )
	{
		console.log("closed - code " + e.code + ", reason " + e.reason);
		console.log( e );
	};
	
	this.webSocket.error = function( e )
	{
		console.log("closed - code " + e.code + ", reason " + e.reason);
		console.log( e );
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
