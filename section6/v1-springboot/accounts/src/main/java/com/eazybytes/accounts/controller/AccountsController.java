package com.eazybytes.accounts.controller;

import com.eazybytes.accounts.constants.AccountsConstants;
import com.eazybytes.accounts.dto.AccountsContactInfoDto;
import com.eazybytes.accounts.dto.CustomerDto;
import com.eazybytes.accounts.dto.ErrorResponseDto;
import com.eazybytes.accounts.dto.ResponseDto;
import com.eazybytes.accounts.service.IAccountsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Author: ldeepak
 *  AccountsController is a REST controller that handles HTTP requests related to customer accounts.
 *  It provides endpoints for creating, fetching, updating, and deleting customer accounts.
 */

/**
 * @Tag annotation is used to group and describe the API endpoints in the OpenAPI documentation.
 * @RestController indicates that this class is a RESTful controller, and @RequestMapping sets the base path for all endpoints in this controller to "/api" and specifies that they produce JSON responses.
 * @Validated enables validation for method parameters in this controller. It is used for validating method parameters, typically primitive types or simple values (like @RequestParam).
 * @Valid is used for validating complex types (objects), such as DTOs passed in the request body (like @RequestBody).
 * @AllArgsConstructor is a Lombok annotation that generates a constructor with parameters for all fields
 */
@Tag(
	name = "Accounts API",
	description = "REST API for managing customer accounts"
)
@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class AccountsController {

	/**
	 * Notice no @Autowired annotation on the constructor
	 * Spring Boot automatically detects constructors with parameters and uses them for dependency injection.
	 * If a class has only one constructor, Spring will use it to inject the required beans, even if you don’t annotate it with @Autowired.
	 * This is called constructor-based dependency injection and is the recommended approach in modern Spring applications.
	 */
	private final IAccountsService accountsService;

	// Approach 1: Using @Value to inject build version from application properties
	@Value("${build.version}")
	private String buildVersion;

	// Approach 2: Using Environment interface to access system properties
	@Autowired
	private Environment environment;

	// Approach 3: Using AccountsContactInfoDto to access custom configuration properties
	// Useful when there are multiple related properties to be injected
	@Autowired
	private AccountsContactInfoDto accountsContactInfoDto;

	public AccountsController(IAccountsService accountsService) {
		this.accountsService = accountsService;
	}

	@Operation(
			summary = "Create a new customer and account",
			description = "Creates a new customer and account with the provided customer details."
	)
	@ApiResponses({
			@ApiResponse(
					responseCode = "201",
					description = "HTTP Status CREATED"
			),
			@ApiResponse(
					responseCode = "500",
					description = "HTTP Status Internal Server Error",
					content = @Content(
							schema = @Schema(implementation = ErrorResponseDto.class)
					)
			)
	})
	@PostMapping("/create")
	public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody CustomerDto customerDto) {

		accountsService.createAccount(customerDto);

		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(new ResponseDto(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201));
	}

	@Operation(
			summary = "Fetch customer and account details",
			description = "Retrieve customer and account details with given mobile number"
	)
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "HTTP Status OK"
			),
			@ApiResponse(
					responseCode = "500",
					description = "HTTP Status Internal Server Error",
					content = @Content(
							schema = @Schema(implementation = ErrorResponseDto.class)
					)
			)
	})
	@GetMapping("/fetch")
	public ResponseEntity<CustomerDto> fetchAccountDetails(@RequestParam
															   @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
															   String mobileNumber) {
		CustomerDto customerDto = accountsService.fetchAccount(mobileNumber);

		return ResponseEntity
				.status(HttpStatus.OK)
				.body(customerDto);
	}

	@Operation(
			summary = "Update customer and account details",
			description = "Update customer and account details with given customer details"
	)
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "HTTP 200 OK - Customer and Account details updated successfully"
			),
			@ApiResponse(
					responseCode = "417",
					description = "HTTP 417 Expectation Failed - Failed to update customer and account details",
						content = @Content(
								schema = @Schema(
										implementation = ErrorResponseDto.class
								)
						)
			),
			@ApiResponse(
					responseCode = "500",
					description = "HTTP Status Internal Server Error",
					content = @Content(
							schema = @Schema(implementation = ErrorResponseDto.class)
					)
			)
	})
	@PutMapping("/update")
	public ResponseEntity<ResponseDto> updateAccountDetails(@Valid @RequestBody CustomerDto customerDto) {
		boolean isUpdated = accountsService.updateAccount(customerDto);
		if (isUpdated) {
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
		} else {
			return ResponseEntity
					.status(HttpStatus.EXPECTATION_FAILED)
					.body(new ResponseDto(AccountsConstants.STATUS_417, AccountsConstants.MESSAGE_417_UPDATE));
		}
	}

	@Operation(
			summary = "Delete customer and account details",
			description = "Delete customer and account details with given customer details"
	)
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "HTTP 200 OK - Customer and Account details deleted successfully"
			),
			@ApiResponse(
					responseCode = "417",
					description = "HTTP 417 Expectation Failed - Failed to delete customer and account details",
					content = @Content(
							schema = @Schema(
									implementation = ErrorResponseDto.class
							)
					)
			),
			@ApiResponse(
					responseCode = "500",
					description = "HTTP Status Internal Server Error",
					content = @Content(
							schema = @Schema(implementation = ErrorResponseDto.class)
					)
			)
	})
	@DeleteMapping("/delete")
	public ResponseEntity<ResponseDto> deleteAccountDetails(@RequestParam
																@Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
																String mobileNumber) {
		boolean isDeleted = accountsService.deleteAccount(mobileNumber);
		if (isDeleted) {
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
		} else {
			return ResponseEntity
					.status(HttpStatus.EXPECTATION_FAILED)
					.body(new ResponseDto(AccountsConstants.STATUS_417, AccountsConstants.MESSAGE_417_DELETE));
		}
	}

	@Operation(
			summary = "Get build information",
			description = "Get the build information"
	)
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "HTTP Status OK"
			),
			@ApiResponse(
					responseCode = "500",
					description = "HTTP Status Internal Server Error",
					content = @Content(
							schema = @Schema(implementation = ErrorResponseDto.class)
					)
			)
	})
	@GetMapping("/build-info")
	public ResponseEntity<String> getBuildInfo() {
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(buildVersion);
	}

	@Operation(
			summary = "Get Java Version",
			description = "Get Java Version"
	)
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "HTTP Status OK"
			),
			@ApiResponse(
					responseCode = "500",
					description = "HTTP Status Internal Server Error",
					content = @Content(
							schema = @Schema(implementation = ErrorResponseDto.class)
					)
			)
	})
	@GetMapping("/java-version")
	public ResponseEntity<String> getJavaVersion() {
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(environment.getProperty("JAVA_HOME"));
	}

	@Operation(
			summary = "Get Contact Info",
			description = "Get Contact Info details"
	)
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "HTTP Status OK"
			),
			@ApiResponse(
					responseCode = "500",
					description = "HTTP Status Internal Server Error",
					content = @Content(
							schema = @Schema(implementation = ErrorResponseDto.class)
					)
			)
	})
	@GetMapping("/contact-info")
	public ResponseEntity<AccountsContactInfoDto> getContactInfo() {
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(accountsContactInfoDto);
	}
}
