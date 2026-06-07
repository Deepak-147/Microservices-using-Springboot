package com.eazybytes.accounts.mapper;

import com.eazybytes.accounts.dto.CustomerDetailsDto;
import com.eazybytes.accounts.dto.CustomerDto;
import com.eazybytes.accounts.entity.Customer;

/**
 * Author: ldeepak
 * CustomerMapper is responsible for mapping between Customer entity and CustomerDto.
 */
public class CustomerMapper {

	// Map Customer entity to CustomerDto
	public static CustomerDto mapToCustomerDto(Customer customer, CustomerDto customerDto) {
		customerDto.setName(customer.getName());
		customerDto.setEmail(customer.getEmail());
		customerDto.setMobileNumber(customer.getMobileNumber());
		return customerDto;
	}

	// Map Customer entity to CustomerDetailsDto
	public static CustomerDetailsDto mapToCustomerDetailsDto(Customer customer, CustomerDetailsDto customerDetailsDto) {
		customerDetailsDto.setName(customer.getName());
		customerDetailsDto.setEmail(customer.getEmail());
		customerDetailsDto.setMobileNumber(customer.getMobileNumber());
		return customerDetailsDto;
	}

	// Map CustomerDto to Customer entity
	public static Customer mapToCustomer(CustomerDto customerDto, Customer customer) {
		customer.setName(customerDto.getName());
		customer.setEmail(customerDto.getEmail());
		customer.setMobileNumber(customerDto.getMobileNumber());
		return customer;
	}
}
