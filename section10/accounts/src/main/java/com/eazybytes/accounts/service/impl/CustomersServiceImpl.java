package com.eazybytes.accounts.service.impl;

import com.eazybytes.accounts.dto.AccountsDto;
import com.eazybytes.accounts.dto.CardsDto;
import com.eazybytes.accounts.dto.CustomerDetailsDto;
import com.eazybytes.accounts.dto.LoansDto;
import com.eazybytes.accounts.entity.Accounts;
import com.eazybytes.accounts.entity.Customer;
import com.eazybytes.accounts.exception.ResourceNotFoundException;
import com.eazybytes.accounts.mapper.AccountsMapper;
import com.eazybytes.accounts.mapper.CustomerMapper;
import com.eazybytes.accounts.repository.AccountsRepository;
import com.eazybytes.accounts.repository.CustomerRepository;
import com.eazybytes.accounts.service.ICustomersService;
import com.eazybytes.accounts.service.client.CardsFeignClient;
import com.eazybytes.accounts.service.client.LoansFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Author: ldeepak
 */
@Service
@AllArgsConstructor
public class CustomersServiceImpl implements ICustomersService {

	private AccountsRepository accountsRepository;
	private CustomerRepository customerRepository;
	private LoansFeignClient loansFeignClient;
	private CardsFeignClient cardsFeignClient;

	/**
	 *
	 * @param mobileNumber
	 * @return Customer details based on the given mobile number
	 */
	@Override
	public CustomerDetailsDto fetchCustomerDetails(String mobileNumber, String correlationId) {

		// First find the customer by mobile number
		Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
				() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
		);

		// Then find the account by customer id
		Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
				() -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
		);

		AccountsDto accountsDto = AccountsMapper.mapToAccountsDto(accounts, new AccountsDto());

		CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
		customerDetailsDto.setAccountsDto(accountsDto);

		// Fetching loans and cards details using Feign clients
		ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoanDetails(correlationId, mobileNumber);
		if (null != loansDtoResponseEntity) {
			customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());
		}

		ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(correlationId, mobileNumber);
		if (null != cardsDtoResponseEntity) {
			customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());
		}

		return customerDetailsDto;
	}
}
