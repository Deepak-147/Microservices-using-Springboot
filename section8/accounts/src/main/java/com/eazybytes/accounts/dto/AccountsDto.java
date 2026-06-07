package com.eazybytes.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Author: ldeepak
 * Accounts Data Transfer Object (DTO) to encapsulate account information.
 */

/**
 * Lombok @Data annotation generates boilerplate code such as getters, setters, toString, equals, and hashCode methods.
 * @Schema annotation from Swagger is used to provide metadata for API documentation.
 */
@Data
@Schema(
		name = "Accounts",
		description = "Schema to hold Account information"
)
public class AccountsDto {

	@Schema(
			description = "Account number of the customer",
			example = "1234567890"
	)
	@NotEmpty(message = "Account Number cannot be null or empty")
	@Pattern(regexp = "(^$|[0-9]{10})", message = "Account number must be a 10-digit number")
	private Long accountNumber;

	@Schema(
			description = "Account type of the customer",
			example = "Savings"
	)
	@NotEmpty(message = "Account Type cannot be null or empty")
	private String accountType;

	@Schema(
			description = "Branch address of the customer",
			example = "123 Main St, Springfield, USA"
	)
	@NotEmpty(message = "Branch Address cannot be null or empty")
	private String branchAddress;
}
