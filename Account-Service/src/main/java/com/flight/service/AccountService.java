package com.flight.service;

import com.flight.dto.AccountDto;
import com.flight.dto.AccountResponse;

public interface AccountService {
    AccountResponse createAccount(AccountDto accountDto);

    Double withdraw(Integer userId, Double amount);

    Double deposit(Integer userId, Double amount);
}
