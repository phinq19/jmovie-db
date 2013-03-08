$(document).ready(function() {
	/* start status timer */
	window.setInterval(function() {
		loadStatus()
	}, 1000);
});

function loadStatus(){
	$.ajax({
		type: "POST",
		url: "/ajax.html?action=getStatus",
		data: { 'action' : 'test'}
	}).done(function( msg ) {
		var message = $.trim(msg);
		if(message != ''){
			$('#status').html(message);
		} else {
			$('#status').html('No Status');
		}
		
	});
}
