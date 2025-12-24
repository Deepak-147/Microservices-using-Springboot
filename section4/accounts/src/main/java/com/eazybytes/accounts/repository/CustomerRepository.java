package com.eazybytes.accounts.repository;

import com.eazybytes.accounts.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Author: ldeepak
 *
 * Repository interface for Customer entity operations.
 */

/**
 * The @Repository annotation in Spring marks a class as a Data Access Object (DAO) that interacts with the database.
 * Indicates that the class provides CRUD operations for a specific entity.
 * By extending JpaRepository, this interface inherits several methods for working with the entity.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	/**
	 * 	findById is default method provided by JpaRepository to find an entity by its primary key.
	 * 	To find a customer by their mobile number, we define a custom method following Spring Data JPA naming conventions.
	 *
	 *  Method to find a customer by their mobile number.
	 *  JPA will automatically implement this method based on the method name. It follows the naming convention "findBy" followed by the field name "MobileNumber".
	 *  Returns an Optional containing the Customer if found, or empty if not found.
	 *  Optional is a container object introduced in Java 8 that may or may not contain a non-null value.
	 *  It is used to avoid NullPointerException and to clearly indicate that a value might be absent. In repository methods, like findByMobileNumber, returning Optional<Customer> signals that the customer may not exist, and forces the caller to handle the possible absence of a value in a safe way.
	 */
	Optional<Customer> findByMobileNumber(String mobileNumber);
}
