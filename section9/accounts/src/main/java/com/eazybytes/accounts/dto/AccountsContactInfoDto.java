package com.eazybytes.accounts.dto;

/**
 * Author: ldeepak
 */

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

/**
 * During the application startup, Spring Boot scans for classes annotated with @ConfigurationProperties.
 * It binds the properties defined in the external configuration files (like application.properties or application.yml) to the fields of the annotated class based on the specified prefix.
 * In this case, properties with the prefix "accounts" will be mapped to the fields of the AccountsContactInfoDto record.
 *
 * Multiple key value pairs can be mapped to a Map field, and multiple values can be mapped to a List field.
 * For example, the contactDetails field is a Map<String, String> that can hold multiple key-value pairs,
 * and the onCallSupport field is a List<String> that can hold multiple string values.
 */
@ConfigurationProperties(prefix = "accounts")
@Getter
@Setter
public class AccountsContactInfoDto {
	private String message;
	private Map<String, String> contactDetails;
	private List<String> onCallSupport;
}
