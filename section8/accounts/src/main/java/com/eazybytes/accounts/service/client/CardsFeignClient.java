package com.eazybytes.accounts.service.client;

import com.eazybytes.accounts.dto.CardsDto;
import jakarta.validation.constraints.Pattern;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Author: ldeepak
 */

// @FeignClient(name = "cards") This annotation is used to declare a Feign client in a Spring Boot application.
// It indicates that this interface is a Feign client and specifies the name of the service it will communicate with, which in this case is "cards". The Feign client will be used to make HTTP requests to the "cards" service, allowing for easy integration and communication between microservices in a distributed system.
// CardsFeignClient will connect to the Eureka server to find the instance of cards microservice and then it will call the API below.
@FeignClient(name = "cards")
public interface CardsFeignClient {

	// Abstract method to fetch card details based on the provided mobile number. The method is annotated with @GetMapping, indicating that it will handle HTTP GET requests to the specified endpoint ("/api/fetch") and consume JSON data.
	// This method is copied from cards controller to fetch the card details from cards microservice based on the mobile number provided in the request parameter.
	// CardsDto is also copied from cards microservice to accounts microservice to use it in the response of this method.
	@GetMapping(value = "/api/fetch", consumes = "application/json")
	public ResponseEntity<CardsDto> fetchCardDetails(@RequestParam String mobileNumber);
}
