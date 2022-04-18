package com.wissen.userapp;

import com.wissen.userapp.model.UrlRequest;
import com.wissen.userapp.model.UrlResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RestController
@RibbonClient(name = "tinyurl", configuration = RibbonConfiguration.class)
public class UserAppApplication {


	@Lazy
	@Autowired
	RestTemplate template;

	@PostMapping("/tinyurl")
	public UrlResponse createTinyUrl(@RequestBody UrlRequest urlRequest){

		String url = "http://tinyurl/urls/tinyurl";
		UrlResponse object = template.postForObject(url, urlRequest, UrlResponse.class);
		return object;
	}

	public static void main(String[] args) {
		SpringApplication.run(UserAppApplication.class, args);
	}



	@Bean
	@LoadBalanced
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
}
