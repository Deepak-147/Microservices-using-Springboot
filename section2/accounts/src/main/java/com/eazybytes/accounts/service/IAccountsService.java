package com.eazybytes.accounts.service;

import com.eazybytes.accounts.dto.CustomerDto;

/**
 * Author: ldeepak
 */
public interface IAccountsService {

	/**
	 *
	 * @param customerDto
	 */
	void createAccount(CustomerDto customerDto);

	/**
	 *
	 * @param mobileNumber
	 * @return Account details based on the given mobile number
	 */
	CustomerDto fetchAccount(String mobileNumber);

	/**
	 *
	 * @param customerDto
	 * @return boolean indicating success or failure of the update operation
	 */
	boolean updateAccount(CustomerDto customerDto);

	/**
	 *
	 * @param mobileNumber
	 * @return boolean indicating success or failure of the delete operation
	 */
	boolean deleteAccount(String mobileNumber);
}
