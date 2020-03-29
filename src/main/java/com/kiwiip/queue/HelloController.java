package com.kiwiip.queue;

import java.util.ArrayList;
import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class HelloController {

    List<String> queue = new ArrayList<String>();

    @MessageMapping("/join")
	@SendTo("/topic/greetings")
	public List<String> join(HelloMessage message) throws Exception {
        queue.add(message.getName());
		return queue;
    }
    
    @MessageMapping("/leave")
	@SendTo("/topic/greetings")
	public  List<String> leave(HelloMessage message) throws Exception {
        queue.remove(message.getName());
		return queue;
	}

	@MessageMapping("/status")
	@SendTo("/topic/greetings")
	public  List<String> status() {
		return queue;
	}
}