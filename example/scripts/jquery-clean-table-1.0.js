/**
 *
 *
 */
( function( $ ) {

	var CleanDataTable = function( obj ) {

		obj.initialise = function() { 

			this.setStyle();
			this.wrapDataContents( this );

			return this;

		};
		
		obj.setStyle = function() {

			$( this ).addClass( "cdt__style" );
			return this;
		};
		
		obj.wrapDataContents = function( inObj ){

			$( inObj ).each( function() {
				
				var tag = $( this ).context.tagName;
				if( 'TH' == tag || 'TD' == tag ) {
					$( this ).html( '<p>' + $( this ).html() + '</p>' );
				} else {
					obj.wrapDataContents( $( this ).children() );
				}

			} );

			return this;
		};
		
		return obj;
	};

	$.fn.cleanDataTable = function() {
		return ( new CleanDataTable( this ) ).initialise();
	};

}( jQuery ) );