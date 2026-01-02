package com.eazybytes.accounts.dto;

/**
 * Author: ldeepak
 */

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

/**
 * A record class in Java is a special type of class introduced in Java 16 to model immutable data carriers.
 * It automatically provides implementations for equals(), hashCode(), toString(), and accessors for its fields, reducing boilerplate code.
 * All fields are final and private.
 * Once created, the state of a record cannot be changed.
 *
 * During the application startup, Spring Boot scans for classes annotated with @ConfigurationProperties.
 * It binds the properties defined in the external configuration files (like application.properties or application.yml) to the fields of the annotated class based on the specified prefix.
 * In this case, properties with the prefix "accounts" will be mapped to the fields of the AccountsContactInfoDto record.
 *
 * Multiple key value pairs can be mapped to a Map field, and multiple values can be mapped to a List field.
 * For example, the contactDetails field is a Map<String, String> that can hold multiple key-value pairs,
 * and the onCallSupport field is a List<String> that can hold multiple string values.
 */
@ConfigurationProperties(prefix = "accounts")
public record AccountsContactInfoDto(String message, Map<String, String> contactDetails, List<String> onCallSupport) {
}
