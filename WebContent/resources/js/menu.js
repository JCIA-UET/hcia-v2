$(document).ready(function(){
	$(".menu").mouseenter(function(){
		$(".menu").animate({fontSize: '1.5em'})
	});
});
$(document).ready(function(){
	$(".menu").mouseleave(function(){
		$(".menu").animate({fontSize: '1em'})
	});
});

(function ($) {
function init() {
$('.easy-tree').EasyTree({
selectable: true,
deletable: false,
editable: false,
addable: false,
i18n: {

}
});
}

window.onload = init();
})(jQuery)
