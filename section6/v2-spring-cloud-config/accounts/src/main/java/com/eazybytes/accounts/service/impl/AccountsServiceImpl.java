package com.eazybytes.accounts.service.impl;

import com.eazybytes.accounts.constants.AccountsConstants;
import com.eazybytes.accounts.dto.AccountsDto;
import com.eazybytes.accounts.dto.CustomerDto;
import com.eazybytes.accounts.entity.Accounts;
import com.eazybytes.accounts.entity.Customer;
import com.eazybytes.accounts.exception.CustomerAlreadyExistsException;
import com.eazybytes.accounts.exception.ResourceNotFoundException;
import com.eazybytes.accounts.mapper.AccountsMapper;
import com.eazybytes.accounts.mapper.CustomerMapper;
import com.eazybytes.accounts.repository.AccountsRepository;
import com.eazybytes.accounts.repository.CustomerRepository;
import com.eazybytes.accounts.service.IAccountsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

/**
 * Author: ldeepak
 */

/**
 * @Service annotation indicates that this class is a service component in the Spring framework.
 * It is a specialization of the @Component annotation and is used to define business logic and services
 */
@Service
@AllArgsConstructor
public class AccountsServiceImpl implements IAccountsService {

	/**
	 * Notice no @Autowired annotation on the constructor
	 * Spring Boot automatically detects constructors with parameters and uses them for dependency injection.
	 * If a class has only one constructor, Spring will use it to inject the required beans, even if you don’t annotate it with @Autowired.
	 * This is called constructor-based dependency injection and is the recommended approach in modern Spring applications.
	 *
	 * Here we have used Lombok's @AllArgsConstructor to generate a constructor with parameters for all fields,
	 * and Spring Boot will use that constructor to inject the AccountsRepository and CustomerRepository bean.
	 */
	private AccountsRepository accountsRepository;
	private CustomerRepository customerRepository;

	/**
	 * @param customerDto
	 */
	@Override
	public void createAccount(CustomerDto customerDto) {

		// From customerDto create a customer entity
		Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());

		// Check if customer with the same mobile number already exists
		Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());

		if (optionalCustomer.isPresent()) {
			throw new CustomerAlreadyExistsException("Customer with mobile number " + customerDto.getMobileNumber() + " already exists.");
		}

		// Save the customer entity
		Customer savedCustomer = customerRepository.save(customer);

		// After saving the customer, create a new account for the customer
		accountsRepository.save(createNewAccount(savedCustomer));
	}

	/**
	 *
	 * @param customer
	 * @return New account details
	 */
	private Accounts createNewAccount(Customer customer) {
		Accounts newAccount = new Accounts();
		newAccount.setCustomerId(customer.getCustomerId());
		long randomAccNumber = 1000000000L + new Random().nextInt(900000000);

		newAccount.setAccountNumber(randomAccNumber);
		newAccount.setAccountType(AccountsConstants.SAVINGS);
		newAccount.setBranchAddress(AccountsConstants.ADDRESS);
		return newAccount;
	}

	/**
	 * @param mobileNumber
	 * @return Account details based on the given mobile number
	 */
	@Override
	public CustomerDto fetchAccount(String mobileNumber) {

		// First find the customer by mobile number
		Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
				() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
		);

		// Then find the account by customer id
		Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
				() -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
		);

		// Aggregating customer and account details into CustomerDto
		CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
		AccountsDto accountsDto = AccountsMapper.mapToAccountsDto(accounts, new AccountsDto());

		customerDto.setAccountsDto(accountsDto);

		return customerDto;
	}

	/**
	 * @param customerDto
	 * @return boolean indicating success or failure of the update operation
	 */
	@Override
	public boolean updateAccount(CustomerDto customerDto) {
		boolean isUpdated = false;
		AccountsDto accountsDto = customerDto.getAccountsDto();
		if (accountsDto != null ) {
			Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
					() -> new ResourceNotFoundException("Account", "AccountNumber", accountsDto.getAccountNumber().toString())
			);
			Accounts updatedAccount = AccountsMapper.mapToAccounts(accountsDto, accounts);
			accounts = accountsRepository.save(updatedAccount);

			Long customerId = accounts.getCustomerId();
			Customer customer = customerRepository.findById(customerId).orElseThrow(
					() -> new ResourceNotFoundException("Customer", "CustomerID", customerId.toString())
			);
			Customer updatedCustomer = CustomerMapper.mapToCustomer(customerDto,customer);
			customerRepository.save(updatedCustomer);
			isUpdated = true;
		}
		return  isUpdated;
	}

	/**
	 * @param mobileNumber
	 * @return boolean indicating success or failure of the delete operation
	 */
	@Override
	public boolean deleteAccount(String mobileNumber) {
		Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
				() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
		);
		accountsRepository.deleteByCustomerId(customer.getCustomerId());
		customerRepository.deleteById(customer.getCustomerId());
		return true;
	}


}
