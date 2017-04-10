$( document ).on( "pageinit", "#home-page", function() {
    $( document ).on( "swipeleft swiperight", "#demo-page", function( e ) {
        // We check if there is no open panel on the page because otherwise
        // a swipe to close the left panel would also open the right panel (and v.v.).
        // We do this by checking the data that the framework stores on the page element (panel: open).
        if ( $.mobile.activePage.jqmData( "panel" ) !== "open" ) {
            if ( e.type === "swipeleft"  ) {
                $( "#right-panel" ).panel( "open" );
            } else if ( e.type === "swiperight" ) {
                $( "#left-panel" ).panel( "open" );
            }
        }
    });
});

$( document ).ready(function() {
	
$.mobile.defaultPageTransition = 'pop';

$(".logout").on("click", function(){
 	var cookies = document.cookie.split(";");

    for (var i = 0; i < cookies.length; i++) {
    	var cookie = cookies[i];
    	var eqPos = cookie.indexOf("=");
    	var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
    	document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
    }
    
    location.href = "#home-page-register";
});

authenticated= getCookie("auth");

//hard coded user name and password
$( "#submitPassword" ).click(function() {
	
	if($('#firstpassword').val()==$('#repassword').val())
		{
		setCookie('auth',true,5);
		sendData();
		
		$( "#successPopup" ).popup("open");
    	
    	setTimeout(function(){ 
    		window.location = "Intellify.html";
		}, 4000);
		}
	else
		{
		$( "#errorPopup" ).popup("open");
		}
});

$( "#register" ).click(function() {
	if(!$('#username').val()){
		$( "#errorPopupUserName" ).popup("open");
	}
	else if(!$('#password').val()){
		$( "#successPopupPassWord" ).popup("open");
	}else{
		setCookie('auth',true,5);
		loginData();
		window.location="Intellify.html#home-page";
	}
	
});

$("#new-user-tap").on("click", function(){
	$(".old-user").hide();
	$(".new-user").show();
});

$("#old-user-tap").on("click", function(){
	$(".new-user").hide();
	$(".old-user").show();
});
	
function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    var expires = "expires="+d.toUTCString();
    document.cookie = cname + "=" + cvalue + "; " + expires ;
} 

function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1);
        if (c.indexOf(name) == 0) return c.substring(name.length,c.length);
    }
    return "";
}

$(".help-submit").on("click", function() {
	sendSupportEmail();
	setTimeout(function(){ 
		
	}, 2000);
});

var addcard = "<div class=\"form-child\"><label for=\"select-custom-20\" class=\"bold\">Card Number</label>"+
"<input type=\"text\" name=\"card-number\" placeholder=\"Enter Card Number\" class=\"card-number\" />"+
"<label for=\"select-custom-20\" class=\"bold label-margin\">First Name</label>"+
"<input type=\"text\" name=\"first-name\" placeholder=\"Enter First Name\" class=\"first-name\" />"+
"<label for=\"select-custom-20\" class=\"bold label-margin\">Last Name</label>"+
"<input type=\"text\" name=\"last-name\" placeholder=\"Enter Last Name\" class=\"last-name\" />"+
"<label for=\"select-custom-20\" class=\"bold label-margin\">Valid Thru</label>"+
"<input type=\"text\" name=\"valid-thru\" placeholder=\"MM/YY\" class=\"valid-thru\" />"+
"<label for=\"select-custom-20\" class=\"bold label-margin\">CVV</label>"+
"<input type=\"text\" name=\"cvv\" placeholder=\"Enter CVV\" class=\"cvv\" />"+
"<div class=\"save-form\">"+
	"<button type=\"submit\" data-theme=\"b\" name=\"sace\" value=\"submit-value\" class=\"save-form\">SAVE</button>"+
"</div></div>";

$(".add-card, .add-image").on("click", function(){
	$(".parent-driver").append(addcard).trigger('create');	
});


$( ".invite-friend" ).click(function() {
	$("#email-friend-popup").dialog();
	$.mobile.changePage('#email-friend-popup', 'flip', true, true);
	/*if($.isNumeric($('.invite').val())){
		var url="sendmail?email=null"+"&mobile="+$('.invite').val()+"&flag=true";
	}
	else{
		var url="sendmail?email="+$('.invite').val()+"&mobile=null&flag=true";
	}
	var jqxhr = $.get( url, function() {
		$("#email-friend-popup").dialog();
		$.mobile.changePage('#email-friend-popup', 'flip', true, true);
		setTimeout(function(){ 
			
			}, 2000);
		});*/
});

$(".email-friend-ok").on("click",function(){
	window.location = '#home-page';	
});

$(".help-ok").on("click",function(){
	window.location = '#home-page';
});

$(document).on("pageinit",function(){
	
	var listvariable ="<div class=\"child \">"+
	"<div class=\"child-99 text-align bold\">"+
	"<div class=\"child-70\">Amex</div>"+
	"<div class=\"child-30\">Restaurant</div>"+
	"<div class=\"child-30\"><a href=\"#book-confirm\" class=\"button\" data-rel=\"dialog\" data-transition=\"flip\">$100</a></div>"+
	"<div class=\"child-100 text-align\">"+
	"</div></div></div>"+
	"<div class=\"child \">"+
	"<div class=\"child-99 text-align bold\">"+
	"<div class=\"child-70\">Chase</div>"+
	"<div class=\"child-30\">Retail</div>"+
	"<div class=\"child-30\"><a href=\"#book-confirm\" class=\"button\" data-rel=\"dialog\" data-transition=\"flip\">$50</a></div>"+
	"<div class=\"child-100 text-align\">"+
	"</div></div></div>"+
	"<div class=\"child \">"+
	"<div class=\"child-99 text-align bold\">"+
	"<div class=\"child-70\">BOFA</div>"+
	"<div class=\"child-30\">Clothing</div>"+
	"<div class=\"child-30\"><a href=\"#book-confirm\" class=\"button\" data-rel=\"dialog\" data-transition=\"flip\">$150</a></div>"+
	"<div class=\"child-100 text-align\">"+
	"</div></div></div>";
	
	$(".parent-list").append(listvariable + listvariable + listvariable + listvariable);	
});

$("#paid-tap").on("click", function(){
	
	var listvariable ="<div class=\"child \">"+
	"<div class=\"child-99 text-align bold\">"+
	"<div class=\"child-70\">Amex</div>"+
	"<div class=\"child-30\">Restaurant</div>"+
	"<div class=\"child-30\"><a href=\"#book-confirm\" class=\"button\" data-rel=\"dialog\" data-transition=\"flip\">$100</a></div>"+
	"<div class=\"child-100 text-align\">"+
	"</div></div></div>"+
	"<div class=\"child \">"+
	"<div class=\"child-99 text-align bold\">"+
	"<div class=\"child-70\">Chase</div>"+
	"<div class=\"child-30\">Retail</div>"+
	"<div class=\"child-30\"><a href=\"#book-confirm\" class=\"button\" data-rel=\"dialog\" data-transition=\"flip\">$50</a></div>"+
	"<div class=\"child-100 text-align\">"+
	"</div></div></div>"+
	"<div class=\"child \">"+
	"<div class=\"child-99 text-align bold\">"+
	"<div class=\"child-70\">BOFA</div>"+
	"<div class=\"child-30\">Clothing</div>"+
	"<div class=\"child-30\"><a href=\"#book-confirm\" class=\"button\" data-rel=\"dialog\" data-transition=\"flip\">$150</a></div>"+
	"<div class=\"child-100 text-align\">"+
	"</div></div></div>";
	$("#loadingdemo").show();
	setTimeout(function(){$("#loadingdemo").hide(); }, 1000);
	$(".parent-list").empty().append(listvariable+listvariable+listvariable);
});

$(".go-btn").on("click",function(){
	var listvariable ="<div class=\"child \">"+
	"<div class=\"child-99 text-align bold\">"+
	"<div class=\"child-70\">Amex</div>"+
	"<div class=\"child-30\">Restaurant</div>"+
	"<div class=\"child-30\"><a href=\"#book-confirm\" class=\"button\" data-rel=\"dialog\" data-transition=\"flip\">$100</a></div>"+
	"<div class=\"child-100 text-align\">"+
	"</div></div></div>";
	$("#loadingdemo").show();
	setTimeout(function(){$("#loadingdemo").hide(); }, 1000);
	
	$(".parent-list").empty().append(listvariable);
	
});

});

function sendData(){
	
	var inputData = '{"userName":"'+$("#username-new").val()+'","password":"'+$("#firstpassword").val()+'","intellifyKey":"'+$("#key").val()+'"}';
		
	$.ajax({
	    url : 'http://192.168.43.30:8080/IntellifyWebServices/service/registerUser',
	    type : "POST",
	    accepts : "application/json",
	    data : inputData,
	    dataType : "json",
	    headers : {
	    Accept : "application/json"
	   },
	     error : function(data) {
		     
	    },
	    contentType : "application/json; charset=utf-8",
	    
	    // Populate data in accordian
	    success : function(data) {
	    	
	        }
	});
	}

function loginData(){
	
	var inputData = '{"userName":"'+$("#username").val()+'","password":"'+$("#password").val()+'"}';
		
	$.ajax({
	    url : 'http://192.168.43.30:8080/IntellifyWebServices/service/loginUser',
	    type : "POST",
	    accepts : "application/json",
	    data : inputData,
	    dataType : "json",
	    headers : {
	    Accept : "application/json"
	   },
	     error : function(data) {
	    	
	    },
	    contentType : "application/json; charset=utf-8",
	    
	    // Populate data in accordian
	    success : function(data) {
	    	
	        }
	});
	}

function sendSupportEmail(){
	$("#help-popup").dialog();
	$.mobile.changePage('#help-popup', 'flip', true, true);
	/*var url="sendmail?email=intellifyamex@gmail.com&mobile=4049924507&flag=none";
	$.get( url, function() {
		
		
		});*/
}

