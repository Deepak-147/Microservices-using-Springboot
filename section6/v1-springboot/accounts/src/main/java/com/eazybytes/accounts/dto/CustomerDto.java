package com.eazybytes.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Author: ldeepak
 * Customer Data Transfer Object (DTO) to encapsulate customer information.
 */

/**
 * Lombok @Data annotation generates boilerplate code such as getters, setters, toString, equals, and hashCode methods.
 * @Schema annotation from Swagger is used to provide metadata for API documentation.
 */
@Data
@Schema(
		name = "Customer",
		description = "Schema to hold Customer and Account information"
)
public class CustomerDto {

	@Schema(
			description = "Customer Name",
			example = "John Doe"
	)
	@NotEmpty(message = "Name cannot be null or empty")
	@Size(min = 5, max = 30, message = "Name should be between 5 to 30 characters")
	private String name;

	@Schema(
			description = "Customer Email Address",
			example = "test@example.com"
	)
	@NotEmpty(message = "Email cannot be null or empty")
	@Email(message = "Email should be valid")
	private String email;

	@Schema(
			description = "Customer Mobile number",
			example = "9784319436"
	)
	@Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be a 10-digit number")
	private String mobileNumber;

	@Schema(
			description = "Customer Account details"
	)
	private AccountsDto AccountsDto;
}
