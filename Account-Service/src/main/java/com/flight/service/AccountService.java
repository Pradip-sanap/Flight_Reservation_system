package com.flight.service;

import com.flight.dto.AccountDto;
import com.flight.dto.AccountResponse;
import com.flight.model.Account;

import java.util.List;
import java.util.Map;

public interface AccountService {



    AccountResponse createAccount(AccountDto accountDto);

    List<Account> getAllAccounts();

    Account getAccountById(Integer accoundId);

    Long getTotalAccounts();

    String isAccountExists(Integer accountId);

    boolean deleteById(Integer accountId);

    Map<String, String> deleteMultipleAccounts(List<Integer> accountList);

    List<Account> pagination(int pageNo, int totalElementInPage);

    Double withdraw(Integer userId, Double amount);

    Double deposit(Integer userId, Double amount);
}
