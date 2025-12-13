package com.eazybytes.accounts.dto;

import io.micrometer.core.ipc.http.HttpSender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * Author: ldeepak
 */

@Data @AllArgsConstructor
@Schema(
		name = "ErrorResponse",
		description = "Schema to hold error response information"
)
public class ErrorResponseDto {

	@Schema(
			description = "API Path invoked by the client"
	)
	private String apiPath;

	@Schema(
			description = "Error code returned to the client"
	)
	private HttpStatus errorCode;

	@Schema(
			description = "Error message returned to the client"
	)
	private String errorMessage;

	@Schema(
			description = "Error timestamp"
	)
	private LocalDateTime errorTime;
}
