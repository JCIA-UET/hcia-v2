$(document).ready(function() {
	$(".signin-message a").click(function(){
		console.log("signin Click");
		$("#register-form").animate({height: "toggle", opacity: "toggle"}, "slow");
		$("#login-form").animate({height: "toggle", opacity: "toggle"}, "slow");
	});
	$(".create-message a").click(function(){
		console.log("signin Click");
		$("#register-form").animate({height: "toggle", opacity: "toggle"}, "slow");
		$("#login-form").animate({height: "toggle", opacity: "toggle"}, "slow");
	});
});