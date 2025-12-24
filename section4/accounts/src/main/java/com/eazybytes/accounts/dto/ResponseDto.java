package com.eazybytes.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Author: ldeepak
 */

@Data @AllArgsConstructor
@Schema(
		name = "Response",
		description = "Schema to hold success response information"
)
public class ResponseDto {

	@Schema(
			description = "Status code of the response"
	)
	private String statusCode;

	@Schema(
			description = "Status message of the response"
	)
	private String statusMsg;
}
