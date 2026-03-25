package com.flight.service.Impl;

import com.flight.dto.AccountDto;
import com.flight.dto.AccountResponse;
import com.flight.exception.InsufficientFundException;
import com.flight.exception.InvalidAmountException;
import com.flight.exception.UserNotFoundException;
import com.flight.model.Account;
import com.flight.repository.AccountRepository;
import com.flight.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public AccountResponse createAccount(AccountDto accountDto) {
        Account accountObj = new Account();
        accountObj.setUserId(accountDto.getUserId());
        accountObj.setBalance(accountDto.getBalance());
        accountObj.setAccountType(accountDto.getAccountType());
        Account saved = accountRepository.save(accountObj);

        return mapToResponse(saved);
    }

    AccountResponse mapToResponse(Account saved){
        return new AccountResponse(
                saved.getAccountId(),
                "Account created Successfully for userId:" + saved.getUserId(),
                saved.getBalance(),
                saved.getAccountType()
        );
    }

    @Override
    public Double withdraw(Integer userId, Double amount) {
        Account accountObj = accountRepository.findByUserId(userId).orElseThrow(()-> new
                                                                    UserNotFoundException("User does not have Account with userId:"+ userId));
        if(amount < 0){
            throw new InvalidAmountException("Withdrawal amount cannot be negative");
        }else if(accountObj.getBalance() < amount){
            throw new InsufficientFundException("Insufficient balance in account");
        }

        accountObj.setBalance(accountObj.getBalance() - amount);
        Account saved = accountRepository.save(accountObj);
        return saved.getBalance();
    }

    @Override
    public Double deposit(Integer userId, Double amount) {
        Account accountObj = accountRepository.findByUserId(userId).orElseThrow(()-> new
                                                                        UserNotFoundException("User not found with userId:"+ userId));
        if(amount < 0){
            throw new InvalidAmountException("Deposit amount cannot be negative");
        }
        accountObj.setBalance(accountObj.getBalance() + amount);
        Account saved = accountRepository.save(accountObj);
        return saved.getBalance();

    }


}
