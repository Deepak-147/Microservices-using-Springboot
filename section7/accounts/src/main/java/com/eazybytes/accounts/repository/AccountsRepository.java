package com.eazybytes.accounts.repository;

import com.eazybytes.accounts.entity.Accounts;
import com.eazybytes.accounts.entity.Customer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
public interface AccountsRepository extends JpaRepository<Accounts, Long> {

	/**
	 * 	findById is default method provided by JpaRepository to find an entity by its primary key.
	 * 	To find an account by its customer id, we define a custom method following Spring Data JPA naming conventions.
	 *
	 *  Method to find an account by its customer id
	 *  Returns an Optional containing the Account if found, or empty if not found.
	 *  JPA will automatically implement this method based on the method name. It follows the naming convention "findBy" followed by the field name "CustomerId".
	 */
	Optional<Accounts> findByCustomerId(Long customerId);

	/**
	 * Method to delete an account by its customer id.
	 * The @Modifying annotation is used to indicate that the query is an update or delete operation.
	 * The @Transactional annotation ensures that the operation is executed within a database transaction. This ensures that the delete operation is atomic and consistent. If any part of the operation fails, the entire transaction can be rolled back to maintain data integrity.
	 */
	@Transactional
	@Modifying
	void deleteByCustomerId(Long customerId);
}
