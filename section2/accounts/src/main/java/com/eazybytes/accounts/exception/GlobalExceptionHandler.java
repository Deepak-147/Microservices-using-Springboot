package com.eazybytes.accounts.exception;

import com.eazybytes.accounts.dto.ErrorResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: ldeepak
 * Global Exception handler for handling custom exceptions across the application
 */

/**
 * The @ControllerAdvice annotation in Spring is used to define a global component that applies to all controllers in your application.
 * It promotes centralized and consistent handling of cross-cutting concerns, so you don’t have to repeat it in every controller, like exception handling...
 * It acts as a global interceptor for controller methods, allowing you to define common behavior that applies to multiple controllers.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		Map<String, String> validationErrors = new HashMap<>();
		List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();

		validationErrorList.forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String validationMsg = error.getDefaultMessage();
			validationErrors.put(fieldName, validationMsg);
		});
		return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Catch CustomerAlreadyExistsException exceptions thrown by any controller method and return a structured error response
	 */
	@ExceptionHandler(CustomerAlreadyExistsException.class)
	public ResponseEntity<ErrorResponseDto> handleCustomerAlreadyExistsException(CustomerAlreadyExistsException exception, WebRequest webRequest) {

		// Creates an ErrorResponseDto containing the API path, HTTP status, error message, and timestamp.
		ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
				webRequest.getDescription(false), // for api path
				HttpStatus.BAD_REQUEST,
				exception.getMessage(),
				LocalDateTime.now()
		);
		return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Catch ResourceNotFoundException exceptions thrown by any controller method and return a structured error response
	 */
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest webRequest) {

		// Creates an ErrorResponseDto containing the API path, HTTP status, error message, and timestamp.
		ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
				webRequest.getDescription(false), // for api path
				HttpStatus.NOT_FOUND,
				exception.getMessage(),
				LocalDateTime.now()
		);
		return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_FOUND);
	}

	/**
	 * Catch any exception thrown by any controller method and return a structured error response
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponseDto> handleGlobalException(Exception exception, WebRequest webRequest) {

		// Creates an ErrorResponseDto containing the API path, HTTP status, error message, and timestamp.
		ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
				webRequest.getDescription(false), // for api path
				HttpStatus.INTERNAL_SERVER_ERROR,
				exception.getMessage(),
				LocalDateTime.now()
		);
		return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
