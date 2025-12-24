package com.eazybytes.accounts;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


/**
 * @EnableJpaAuditing is a Spring annotation that enables JPA Auditing in your application.
 * It allows Spring Data JPA to automatically populate auditing fields like created date, last modified date, created by, and last modified by in your entities when you use annotations such as @CreatedDate, @LastModifiedDate, @CreatedBy, and @LastModifiedBy.
 * This helps track who made changes to your data and when, without manual intervention.
 */
@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@OpenAPIDefinition(
		info = @Info(
				title = "Accounts Microservice API Documentation",
				version = "1.0",
				description = "API documentation for the Accounts Microservice",
				contact = @Contact(
						name = "Deepak Laxkar",
						email = "deepaklaxkar11@gmail.com",
						url = "www.google.com"
				),
				license = @License(
						name = "Apache 2.0",
						url = "http://www.apache.org/licenses/LICENSE-2.0.html"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "Accounts Microservice Wiki Documentation",
				url = "www.google.com"
		)
)
public class AccountsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountsApplication.class, args);
	}

}
