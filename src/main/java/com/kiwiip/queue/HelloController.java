package com.kiwiip.queue;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {

	@RequestMapping("/greeting")
	public String index() {
		return "Greetings from Spring Boot!";
	}

    @GetMapping("/")
	public String greeting() {
		return "index";
    }
    
}