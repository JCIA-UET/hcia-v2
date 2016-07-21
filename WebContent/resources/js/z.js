$(document).ready(function() {
	$(".nav>li ul").hide();
	$('.tree-toggle').click(function() {
		$(this).children('ul').slideToggle(200); // Hides if shown, shows if
	});
	
	/*$('a[data-toggle="tab"]').on('shown.bs.tab', function(e) {
		var url = new String(e.target);
		var comp = url.split('#');

		if (comp[1] == 'foreign') {
			$('#settings').css('bottom','-'+$(window).height()+'px');            
	        var bottom = $('#settings').offset().bottom;
	        $("#settings").css({bottom:bottom}).animate({"bottom":"0px"}, "10");
		}

		if (comp[1] == 'columns') {
			$('#settings').css('bottom','-'+$(window).height()+'px');            
	        var bottom = $('#settings').offset().bottom;
	        $("#settings").css({bottom:bottom}).animate({"bottom":"0px"}, "10");
		}
	});*/

	$('#xml-trigger').click(function() {
		$('#xmlModal').modal('show');
		return false;
	});

	$('#zip-trigger').click(function() {
		$('#zipModal').modal('show');
		return false;
	});

	$(".dropdown").hover(function() {
		$('.dropdown-menu', this).stop(true, true).fadeIn("fast");
	}, function() {
		$('.dropdown-menu', this).stop(true, true).fadeOut("fast");
	});
});
