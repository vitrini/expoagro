



var GeneratePinagem = function ()
{
	
	var mapQuery;
	var pinagens = [];
	var useOffset = false;


	var QUERY_FOR_INPUT_PIN_LBL = '#iptPinLbl';
	var QUERY_FOR_DWNLD_PIN_PLACES = '#downloadPinPlaces';

	var CSS_PIN_FORMAT = '#location_%s{top:%spx;left:%spx;}'
	var HTML_PIN_FORMAT = '<div id="location_%1$s" class="location">%1$s</div>'

	var PIN_HEIGHT = 0;
	var PIN_WIDTH = 0;

	var clickToGeneratePin = function( evt ) 
	{
		var inpt = $( QUERY_FOR_INPUT_PIN_LBL );

		var offsetX = evt.offsetX;
		var offsetY = evt.offsetY;
		var pageX = evt.pageX;
		var pageY = evt.pageY;

		var label = inpt.val();

		if  (label == "" || label == undefined)
		{
			return;
		} 

		var pin = 
		{
			offsetX : offsetX,
			offsetY : offsetY,
			pageX : pageX,
			pageY : pageY,
			label : label
		}

		var $map = $(mapQuery);

		var htmlPin = sprintf(HTML_PIN_FORMAT, label);
			
		$map.append( htmlPin );
		$map.find('.location').last().css('top', offsetY);
		$map.find('.location').last().css('left', offsetX);

		pinagens.push(pin);


	}

	var generatePinPlaces = function ()
	{
		
		var cssLines = '';
		var htmLines = '';

		for (var index in pinagens )
		{
			var pin = pinagens[index];

			var offsetX = pin.offsetX;
			var offsetY = pin.offsetY;
			var pageX = pin.pageX;
			var pageY = pin.pageY;
			var label = pin.label;

			var positionX = pageX;
			var positionY = pageY;

			if ( useOffset )
			{
				positionX = offsetX - PIN_WIDTH;
				positionY = offsetY - PIN_HEIGHT;
			}

			var htmlPin = sprintf(HTML_PIN_FORMAT, label);
			var cssPin = sprintf(CSS_PIN_FORMAT, label, positionY, positionX );
			
			htmLines += htmlPin + '\n';
			cssLines += cssPin + '\n';

		}

		console.log( htmLines );
		console.log( cssLines );

	}

	this.init = function (  mapQueryParam, useOffsetParam ) {
		
		mapQuery = mapQueryParam;	
		useOffset = useOffsetParam;
		var $map = $(mapQuery);


		$map.parent().append('Pin: <input type="text" id="iptPinLbl" placeholder="digite o label do pin">');	
		$map.parent().append('Download:<input type="submit" id="downloadPinPlaces" value="Download">');


		$map.unbind('click');
		$map.click( clickToGeneratePin );

		var $downloadPinPlaces = $map.parent().find( QUERY_FOR_DWNLD_PIN_PLACES );
		$downloadPinPlaces.click(generatePinPlaces);	

	}

	this.removeLastPin = function ()
	{
		var $map = $(mapQuery);

		pinagens.pop();
		$map.find('.location').last().remove();

	}

}


$(document).ready( 
	function()
	{
		var pinagem = new GeneratePinagem();

		var useOffset = true; 
		pinagem.init('#map', useOffset);

		$('body').keypress(function(e){
		    
			/* del key*/	
		    if(e.which == 127){
		        // Close my modal window
		        pinagem.removeLastPin();
		    }
		});

	}
);