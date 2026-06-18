package com.eazybytes.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class GatewayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}

	/**
	 * Custom route configuration for the gateway server. We want routes to have prefix /eazybank
	 *
	 * Gateway needs RouteLocator-based route definitions.
	 * We provide custom routes for the gateway server by returning a RouteLocator bean from this method.
	 * On startup, Gateway picks up and uses our bean automatically.
	 *
	 * The gateway will route requests to the appropriate microservices based on the path.
	 * For example, requests to /eazybank/accounts/** will be routed to the ACCOUNTS microservice
	 *
	 * @Bean annotation indicates that this method returns a bean that should be managed by the Spring container.
	 * In this case, it returns a RouteLocator bean that defines the routes for the gateway server.
	 *
	 * .rewritePath filter is used to modify the incoming request path before it is forwarded to the target microservice. It uses a regular expression to capture the segment of the path after /eazybank/accounts/ and rewrites the path to remove the /eazybank/accounts prefix. This allows the gateway to forward the request to the appropriate endpoint in the ACCOUNTS microservice without the extra prefix.
	 * .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()) adds a custom response header to the response, which can be useful for debugging and monitoring purposes. It provides how much time the gateway server took to process the request and send the response back to the client.
	 * .circuitBreaker() is used to implement the circuit breaker pattern, which helps to prevent cascading failures in a microservices architecture.
	 * .setFallbackUri() specifies the URI to which the request should be forwarded if the circuit breaker is open (i.e., if the target microservice is unavailable or experiencing issues)
	 */
	@Bean
	public RouteLocator eazyBankRouteConfig(RouteLocatorBuilder routeLocatorBuilder) {
		return routeLocatorBuilder.routes()
				.route(p -> p
						.path("/eazybank/accounts/**")
						.filters( f -> f.rewritePath("/eazybank/accounts/(?<segment>.*)","/${segment}")
						.addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
						.circuitBreaker(config -> config.setName("accountsCircuitBreaker")
						.setFallbackUri("forward:/contactSupport")))
						.uri("lb://ACCOUNTS"))
				.route(p -> p
						.path("/eazybank/loans/**")
						.filters( f -> f.rewritePath("/eazybank/loans/(?<segment>.*)","/${segment}")
						.addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
						.uri("lb://LOANS"))
				.route(p -> p
						.path("/eazybank/cards/**")
						.filters( f -> f.rewritePath("/eazybank/cards/(?<segment>.*)","/${segment}")
						.addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
						.uri("lb://CARDS")).build();
	}

}