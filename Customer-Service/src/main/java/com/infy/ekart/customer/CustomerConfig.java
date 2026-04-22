package com.infy.ekart.customer;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class CustomerConfig {
    
//	@Bean 
//	// Make this as load-balanced rest template
//	@LoadBalanced
//	public RestTemplate restTemplate() {
//		return new RestTemplate();
//	}
	
	@Bean	
	public WebClient webClient() {
		return WebClient.builder().build();
	}
	
}
