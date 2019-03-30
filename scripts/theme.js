function init() {
	/*if (localStorage.getItem('theme') == "scripts/style_black.css") {
		$("#stylesheet").attr({href : "scripts/style_black.css"});
		console.log("Set to black");
	} else {
		localStorage.setItem('theme', 'scripts/style_white.css');
		console.log("Set to white");
	}*/
	if (localStorage.getItem('theme') == "scripts/style_black.css") {
		$("body").css("background-image", "url('../images/background.jpg')");
		$("body").css("background-color", "");
		$("h1").css("color", "white");
		$("h2").css("color", "white");
		$("h2").css("font-family", "arial");
		$("h3").css("color", "white");
		$("h4").css("color", "white");
		$("p").css("font-family", "arial");
		$("a").css("text-decoration", "");
		$("div.box").css("background", "rgba(255,255,255,.3)");
		$("div.box").css("border-color", "");
		$("div.box").css("border-width", "0px");
		$("div.box").css("box-shadow", "none");
		$(".footer a").css("color", "white");
		$("div.enterbutton").css("background", "rgba(74,14,27,0.8)");
		$("div.enterbutton").hover(function() {
			$(this).css("background", "rgba(74,14,27,0.5)"); 
		}, function() {
			$(this).css("background", "rgba(74,14,27,0.8)");
		});
		$("div.headerbutton").css("background", "rgba(74,14,27,0.8)");
		$("div.menubutton").css("background", "rgba(74,14,27,0.8)");
		$("div.menubutton").hover(function() {
			$(this).css("background", "rgba(74,14,27,0.5)");
		}, function() {
			$(this).css("background", "rgba(74,14,27,0.8)");
		});
		$("div.centerbutton").css("background", "rgba(74,14,27,0.8)");
		$("div.centerbutton").hover(function() {
			$(this).css("background", "rgba(74,14,27,0.5)");
		}, function() {
			$(this).css("background", "rgba(74,14,27,0.8)");
		});
		console.log("black");
	} else {
		$("body").css("background-image", "");
		$("body").css("background-color", "white");
		$("h1").css("color", "black");
		$("h2").css("color", "black");
		$("h2").css("font-family", "Fira Mono");
		/*$("#projects").css("color", "white");*/
		$("h3").css("color", "black");
		$("h4").css("color", "black");
		$("p").css("font-family", "Fira Mono");
		$("a").css("text-decoration", "underline");
		$("div.box").css("background", "transparent");
		$("div.box").css("border-color", "black");
		$("div.box").css("border-width", "1px");//;
		$("div.box").css("box-shadow", "10px 10px #888888");
		$(".footer a").css("color", "#202226");
		$("div.enterbutton").css("background", "rgba(122,126,197,0.8)");
		$("div.enterbutton").hover(function() {
			$(this).css("background", "rgba(122,126,197,0.5)");
		}, function() {
			$(this).css("background", "rgba(122,126,197,0.8)");
		});
		$("div.headerbutton").css("background", "rgba(122,126,197,0.8)");
		$("div.menubutton").css("background", "rgba(122,126,197,0.8)");
		$("div.menubutton").hover(function() {
			$(this).css("background", "rgba(122,126,197,0.5)");
		}, function() {
			$(this).css("background", "rgba(122,126,197,0.8)");
		});
		$("div.centerbutton").css("background", "rgba(122,126,197,0.8)");
		$("div.centerbutton").hover(function() {
			$(this).css("background", "rgba(122,126,197,0.5)");
		}, function() {
			$(this).css("background", "rgba(122,126,197,0.8)");
		});
	}
}

function change_theme() {
	if (localStorage.getItem('theme') == "scripts/style_white.css") {
		//$("#stylesheet").attr({href : "scripts/style_black.css"});
		localStorage.setItem('theme', 'scripts/style_black.css')
		console.log(localStorage.getItem('theme'));
	} else {
		//$("#stylesheet").attr({href : "scripts/style_white.css"});
		localStorage.setItem('theme', 'scripts/style_white.css')
	}
	init();		
}