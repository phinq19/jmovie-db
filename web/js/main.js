var interval = 1000;
var statusReloadErrorCounter = 0;
var statusReloadId;

$(document).ready(function() {
	/* start status timer */
	resetInterval(interval);
	addAutocompleteSearchBox();
	
	
	addBrowseJS();
	addSettingsActions();
});

function addBrowseJS(){
	$('.keyList').each(function(){
		var tabs = $(this).find('li');
		var tables = $(this).nextAll('table');
		tables.filter(':first').show();
		tabs.filter(':first').addClass('active');
	
		$(this).find('li').click(function(){
		    var key = $(this).find('a').html();
		    tabs.removeClass('active');
		    $(this).addClass('active');
		    tables.hide();
		    tables.filter('.key_' + key).show();
		});
	});
}

function addSettingsActions(){
	if($('#searchSettings').length){
		$('#searchSettings').find('select').each(function(){
			$(this).change(function(){
				$(this).parents('form:first').submit();
			});
		});
	}
}

function addAutocompleteSearchBox(){
	if($('#searchStr').length){
	
		function split(val) {
			return val.split(/,\s*/);
		}
		function extractLast(term) {
			return split(term).pop();
		}
	
		var cache = {};
		$('#searchStr').autocomplete({
			minLength: 2,
			multiple : true,
			multipleSeparator : ', ',
			source: function( request, response ) {
				var term = request.term;
				if ( term in cache ) {
					response( cache[ term ] );
					return;
				}
				$.getJSON("json.html?action=autocomplete", request, function( data, status, xhr ) {
					cache[term] = data;
					response(data);
				});
			}
		});
	}
}

function resetInterval(inInterval){
	if(typeof(statusReloadId) != 'undefined'){
		clearInterval(statusReloadId);
	}
	interval = inInterval;
	statusReloadId = window.setInterval(function() {
		loadStatus()
	}, inInterval);
}

function loadStatus(){
	$.ajax({
		type: "POST",
		url: "/ajax.html?action=getStatus",
		data: { 'action' : 'test'},
		timeout : interval-10,
		success: function( msg ) {
			statusReloadErrorCounter = 0;
			var message = $.trim(msg);
			if(message != ''){
				$('#status').html(message);
			}
			if(interval != 1000){
				resetInterval(1000);
			}
		},
		error: function( XMLHttpRequest, textStatus, errorThrown ) {
			if(XMLHttpRequest.readyState == 0 && XMLHttpRequest.status == 0){
				statusReloadErrorCounter++;
				$('#status').html('<p>No connection. JMovieDB seems to be offline.' + (statusReloadErrorCounter > 1 ? ' Tried ' + statusReloadErrorCounter + ' times.' : '') + '</p>');
				if(interval != 10000){
					resetInterval(10000);
				}
				
				if(statusReloadErrorCounter >= 10){
					if(interval != 60000){
						resetInterval(60000);
					}
				}
			}
		},
	});
}
