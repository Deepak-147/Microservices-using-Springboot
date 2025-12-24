package com.eazybytes.accounts.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Author: ldeepak
 */
@Entity
@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor
public class Customer extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long customerId;

	private String name;

	private String email;

	private String mobileNumber;
}
