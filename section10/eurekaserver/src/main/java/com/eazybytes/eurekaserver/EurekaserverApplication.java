package com.eazybytes.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

// Eureka Server is a service registry that allows microservices to register themselves and discover other services.
// It provides a central location for managing service instances and their metadata, enabling dynamic service discovery and load balancing in a microservices architecture.
// The @EnableEurekaServer annotation is used to enable the Eureka Server functionality in a Spring Boot application.
@SpringBootApplication
@EnableEurekaServer
public class EurekaserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaserverApplication.class, args);
	}

}
