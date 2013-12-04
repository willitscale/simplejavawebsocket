Date.prototype.getAtom = function()
{
	return 	this.getFullYear() + '-' + 
		this.getMonthZero() + '-' + 
		this.getDateZero() + 'T' + 
		this.getHoursZero() + ':' + 
		this.getMinutesZero() + ':' + 
		this.getSecondsZero() + 'Z';
};

Date.prototype.getTimeStamp = function()
{
	return 	this.getFullYear() + '-' + 
		this.getMonthZero() + '-' + 
		this.getDateZero() + ' ' + 
		this.getHoursZero() + ':' + 
		this.getMinutesZero() + ':' + 
		this.getSecondsZero();
};

Date.prototype.getMonthZero = function()
{
	return this.padZero( 1 + ( 1 * this.getMonth() ) );
};

Date.prototype.getDateZero = function()
{
	return this.padZero( this.getDate() );
};

Date.prototype.getHoursZero = function()
{
	return this.padZero( this.getHours() );
};

Date.prototype.getMinutesZero = function()
{
	return this.padZero( this.getMinutes() );
};

Date.prototype.getSecondsZero = function()
{
	return this.padZero( this.getSeconds() );
};

Date.prototype.padZero = function( value )
{
	var value = ( 1 * value );

	if( 10 < value )
		return value;

	return '0' + value;
};
