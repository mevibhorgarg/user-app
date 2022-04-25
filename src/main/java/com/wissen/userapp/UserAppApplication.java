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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SpringBootApplication
@RestController
@RibbonClient(name = "tinyurl", configuration = RibbonConfiguration.class)
public class UserAppApplication {


	@Lazy
	@Autowired
	RestTemplate template;
	public static final String tinyUrl= "http://tinyurl/urls/tinyurl";
	public static final String redirectShortUrl= "http://tinyurl/urls/longurl/";

	@PostMapping("/tinyurl")
	public UrlResponse createTinyUrl(@RequestBody UrlRequest urlRequest){

		String url = tinyUrl;
		UrlResponse object = template.postForObject(url, urlRequest, UrlResponse.class);
		return object;
	}

	@GetMapping("/longurl/{shorturl}")
	public ResponseEntity<?> getLongUrl(@PathVariable("shorturl") String shortUrl, HttpServletResponse response) throws IOException {
		String url = redirectShortUrl+ shortUrl;
		template.getForObject(url, null, String.class);
		return null;
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
