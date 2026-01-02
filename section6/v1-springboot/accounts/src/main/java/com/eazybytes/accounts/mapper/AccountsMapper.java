package com.eazybytes.accounts.mapper;

import com.eazybytes.accounts.dto.AccountsDto;
import com.eazybytes.accounts.entity.Accounts;

/**
 * Author: ldeepak
 * AccountsMapper is responsible for mapping between Accounts entity and AccountsDto.
 */

public class AccountsMapper {

	// Map Accounts entity to AccountsDto
	public static AccountsDto mapToAccountsDto(Accounts accounts, AccountsDto accountsDto) {
		accountsDto.setAccountNumber(accounts.getAccountNumber());
		accountsDto.setAccountType(accounts.getAccountType());
		accountsDto.setBranchAddress(accounts.getBranchAddress());
		return accountsDto;
	}

	// Map AccountsDto to Accounts entity
	public static Accounts mapToAccounts(AccountsDto accountsDto, Accounts accounts) {
		accounts.setAccountNumber(accountsDto.getAccountNumber());
		accounts.setAccountType(accountsDto.getAccountType());
		accounts.setBranchAddress(accountsDto.getBranchAddress());
		return accounts;
	}
}
