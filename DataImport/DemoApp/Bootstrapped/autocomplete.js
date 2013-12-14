$( document ).ready(function() {
// Global Variables
i = $(".autocomplete");

// Open Socket
// Constants
SOCKJS_PROXY = 'http://134.93.130.181:80';

// REM:
// 'sockjsproxy' has to be started before the script can be used.
// use '-v' to turn on verbose logging of received messages.

var sock = new SockJS(SOCKJS_PROXY);

sock.onopen = function() {
// Say hello to the server
console.log('Socket open');
sock.send("Started Session");
};

// register AC for input field #tags
i.autocomplete({
delay: 0, // no delay
source: function(request, ac_response) {
console.log("AC REQ:" + request.term);

sock.send(request.term);

sock.onmessage = function(e) {
DATA = JSON.parse(e.data)
ac_data = $.map(DATA.suggestionList, function(suggestItem) {
return { label: suggestItem.key.replace("_"," "), 
         value: suggestItem.key.replace("_"," ") }
});

ac_response(ac_data);
};
}
});
});