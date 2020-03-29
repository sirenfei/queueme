var stompClient = null;

function setConnected(connected) {

    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

$(function () {
    connect();

    function disconnect() {
        var queueUser = $.cookie('queueUser');
        stompClient.send("/app/leave/stock", {}, JSON.stringify({'name': queueUser}));
    
        if (stompClient !== null) {
            stompClient.disconnect();
        }
        setConnected(false);
        $.removeCookie('queueUser');
        console.log("Disconnected");
    }

    function sendName(timeStamp) {
        var username = $("#name").val() + "-" + timeStamp
        $.cookie('queueUser', username, { expires: 1 });
        stompClient.send("/app/join/stock", {}, JSON.stringify({'name': username}));
    }

    function connect() {
        var socket = new SockJS('/gs-guide-websocket');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            setConnected(true);
            console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/greetings/stock', function (greeting) {
                showGreeting(JSON.parse(greeting.body));
            });

            var queueUser = $.cookie('queueUser');
            if(queueUser) {
                stompClient.send("/app/status/stock", {}, JSON.stringify());
                $("#user-form").hide();
                $("#key").show();
                $("#join").hide();
                $("#leave").show();
            }
        });
    }

    $("#join").click(function(){
        sendName(Math.floor(Math.random()*100000000));
        $("#user-form").hide();
        $("#key").show();
        $("#join").hide();
        $("#leave").show();
    });

    $("#leave").click(function(){
        var queueUser = $.cookie('queueUser');
        disconnect();
        console.log(queueUser)
        $("#user-form").show();
        $("#key").hide();
        $("#join").show();
        $("#leave").hide();
    });



    
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });


    function showGreeting(message) {
        var queueUser = $.cookie('queueUser'); 
        $.each(message,function(i,n) {
            if(n == queueUser) {
                $("#people_ahead").html(i + " people ahead");
            }
        });

    }

});