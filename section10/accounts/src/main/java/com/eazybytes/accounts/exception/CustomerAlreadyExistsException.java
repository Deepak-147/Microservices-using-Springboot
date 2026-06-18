package com.eazybytes.accounts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Author: ldeepak
 */

// This annotation tells Spring that when this exception is thrown, the HTTP response should have a 400 Bad Request status.
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CustomerAlreadyExistsException extends RuntimeException {

	public CustomerAlreadyExistsException(String message) {
		super(message);
	}
}
