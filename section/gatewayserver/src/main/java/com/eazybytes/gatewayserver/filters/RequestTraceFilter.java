package com.eazybytes.gatewayserver.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Author: ldeepak
 */

/**
 * @Order annotation is used to specify the order of execution of filters. The filter with the lowest order value will be executed first.
 *
 *
 */
@Order(1)
@Component
public class RequestTraceFilter implements GlobalFilter {

	private static final Logger logger = LoggerFactory.getLogger(RequestTraceFilter.class);

	@Autowired
	FilterUtility filterUtility;

	/**
	 *
	 * The filter method is called for every request that goes through the gateway. It checks if the correlation ID is present in the request headers.
	 * If it is present, it logs the correlation ID. If it is not present, it generates a new correlation ID and adds it to the request headers.
	 */
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
		if (isCorrelationIdPresent(requestHeaders)) {
			logger.debug("eazyBank-correlation-id found in RequestTraceFilter : {}",
					filterUtility.getCorrelationId(requestHeaders));
		} else {
			String correlationID = generateCorrelationId();
			exchange = filterUtility.setCorrelationId(exchange, correlationID);
			logger.debug("eazyBank-correlation-id generated in RequestTraceFilter : {}", correlationID);
		}
		return chain.filter(exchange);
	}

	private boolean isCorrelationIdPresent(HttpHeaders requestHeaders) {
		if (filterUtility.getCorrelationId(requestHeaders) != null) {
			return true;
		} else {
			return false;
		}
	}

	private String generateCorrelationId() {
		return java.util.UUID.randomUUID().toString();
	}

}