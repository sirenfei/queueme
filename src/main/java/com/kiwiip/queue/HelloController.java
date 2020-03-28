package com.kiwiip.queue;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.HtmlUtils;

@Controller
public class HelloController {

	// @RequestMapping("/greeting")
	// public String index() {
	// 	return "Greetings from Spring Boot!";
	// }

    // @GetMapping("/")
	// public String greeting() {
	// 	return "index";
    // }
    
    // @MessageMapping("/news")
    // @SendTo("/topic/news")
    // public String broadcastNews(@Payload String message) {
    //     return message;
    // }

    @MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public Greeting greeting(HelloMessage message) throws Exception {
		Thread.sleep(1000); // simulated delay
		return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
	}
}