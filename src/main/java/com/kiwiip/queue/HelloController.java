package com.kiwiip.queue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class HelloController {

    Map<String,List<String>> mapQueue = new HashMap<String,List<String>>();

    @MessageMapping("/join/{stock}")
	@SendTo("/topic/greetings/{stock}")
	public List<String> join(@DestinationVariable String stock, HelloMessage message) {
		return addQueue(stock, message.getName());
    }
    
    @MessageMapping("/leave/{stock}")
	@SendTo("/topic/greetings/{stock}")
	public  List<String> leave(@DestinationVariable String stock, HelloMessage message) {
		return removeQueue(stock, message.getName());
	}

	@MessageMapping("/status/{stock}")
	@SendTo("/topic/greetings/{stock}")
	public  List<String> status(@DestinationVariable String stock) {
		return statusQueue(stock);
	}

	List<String> addQueue(String stock, String username){
		List<String> queue = mapQueue.get(stock);
		if(queue == null) {
			queue = new ArrayList<String>();
		}
		queue.add(username);
		return queue;
	}

	List<String> removeQueue(String stock, String username){
		List<String> queue = mapQueue.get(stock);
		if(queue == null) {
			return new ArrayList<String>();
		}
		queue.remove(username);
		return queue;
	}

	List<String> statusQueue(String stock){
		List<String> queue = mapQueue.get(stock);
		return queue;
	}
}