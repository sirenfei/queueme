var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}



function disconnect() {

    stompClient.send("/app/leave", {}, JSON.stringify({'name': $("#name").val()}));

    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/app/join", {}, JSON.stringify({'name': $("#name").val()}));
}



$(function () {

    function connect() {
        var socket = new SockJS('/gs-guide-websocket');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            setConnected(true);
            console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/greetings', function (greeting) {
                showGreeting(JSON.parse(greeting.body));
            });
        });
    }

    $("form").on('submit', function (e) {
        e.preventDefault();
    });

    connect();
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });


    function showGreeting(message) {
        $("#greetings").empty();
        $.each(message,function(i,n) {
            $("#greetings").append("<tr><td>"+ n + " You are No." + i + "</td></tr>");
            console.log(i+" > "+n);
        });
    }

});