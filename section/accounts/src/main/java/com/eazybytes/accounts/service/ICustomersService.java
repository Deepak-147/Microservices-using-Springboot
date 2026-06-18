package com.eazybytes.accounts.service;

import com.eazybytes.accounts.dto.CustomerDetailsDto;

/**
 * Author: ldeepak
 */
public interface ICustomersService {

	/**
	 *
	 * @param mobileNumber
	 * @return Customer details based on the given mobile number
	 */
	CustomerDetailsDto fetchCustomerDetails(String mobileNumber, String correlationId);
}
