package com.flight.service.Impl;

import com.flight.dto.AccountDto;
import com.flight.dto.AccountResponse;
import com.flight.exception.*;
import com.flight.model.Account;
import com.flight.repository.AccountRepository;
import com.flight.service.AccountService;
import com.flight.service.feignClients.UserServiceFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserServiceFeignClient userServiceFeignClient;

    @Value("${jwt.token}")
    private String token;

    @Override
    public AccountResponse createAccount(AccountDto accountDto) {
        Integer userId = accountDto.getUserId();
        log.debug("Validating user with userId={}", userId);
        Boolean existingUser = userServiceFeignClient.validateUser(userId, token);
        log.debug("Users validation response for userId={} is {}", userId, existingUser);

        if (!existingUser) {
            log.error("Invalid user details for userId={}", userId);
            throw new InvalidUserDetailsException("User details are not valid");
        }

        log.debug("Checking if account already exists for userId={}", userId);
        Optional<Account> accountObj = accountRepository.findByUserId(userId);

        if (accountObj.isPresent()) {
            log.warn("Account already exists for userId={}", userId);
            throw new UserAccountAlreadyPresentException("User already have account");
        }

        log.debug("Creating account entity for userId={}", userId);
        Account account = mapToEntity(accountDto);
        Account saved = accountRepository.save(account);
        log.info("Account created successfully with accountId={} for userId={}", saved.getAccountId(), userId);

        return mapToResponse(saved);
    }

    public Account getAccountById(Integer accountId) {
        log.debug("Finding account by account id={}", accountId);
        return accountRepository.findById(accountId).orElseThrow(() -> {
            log.error("No account found for account id={}", accountId);
            throw new AccountNotFoundException("Account details not found");
        });
    }



    public List<Account> pagination(int pageNo, int totalElementInPage) {
        log.debug("Getting all accounts for Page with pageNo={}, and totalElementInPage={}", pageNo, totalElementInPage);
        Page<Account> page = accountRepository.findAll(PageRequest.of(pageNo, totalElementInPage, Sort.by("balance").ascending()));
        return page.getContent();
    }

    public List<Account> getAllAccounts() {
        log.debug("Getting all Accounts details");
        return accountRepository.findAll();
    }

    public Long getTotalAccounts() {
        log.debug("Inside AccountService, getting total accounts");
        return accountRepository.count();
    }

    public String isAccountExists(Integer accountId) {
        log.debug("Checking if account exists for accountId={}", accountId);
        if (accountRepository.existsById(accountId)) {
            log.info("Account exists for accountId={}", accountId);
            return "Exist";
        } else {
            log.error("Account not found for accountId={}", accountId);
            throw new AccountNotFoundException("Account details not found");
        }
    }

    public boolean deleteById(Integer accountId) {
        log.info("Request received to delete account with accountId={}", accountId);
        if (isAccountExists(accountId).equals("Exist")) {
            log.debug("Deleting account with accountId={}", accountId);
            accountRepository.deleteById(accountId);
            log.info("Account successfully deleted for accountId={}", accountId);
            return true;
        }

        log.warn("Account does not exist, so no account deleted for accountId={}", accountId);
        return false;
    }

    public Map<String, String> deleteMultipleAccounts(List<Integer> accountList) {
        log.info("Received request to delete multiple accounts. Total count={}", accountList.size());
        List<Integer> success = new ArrayList<>();
        List<Integer> failure = new ArrayList<>();
        accountList.forEach(accountId -> {
            if (accountRepository.existsById(accountId)) {
                log.debug("Account exists for accountId={}", accountId);
                success.add(accountId);
            } else {
                log.warn("Account not found. accountId={}", accountId);
                failure.add(accountId);
            }
        });

        if (!success.isEmpty()) {
            log.info("Deleting {} accounts", success.size());
            accountRepository.deleteAllByIdInBatch(success);
            log.info("Successfully deleted accountIds={}", success);
        }

        if (failure.isEmpty()) {
            log.info("Deleted total {} accounts", success.size());
            return Map.of("status", "All records successfully deleted");
        } else if (failure.size() == accountList.size()) {
            log.error("None of the provided accountIds were found. accountIds={}", accountList);
            return Map.of("Status", "All given AccountID details not found. Not record deleted.");
        } else {
            log.warn("Partial deletion completed. successCount={}, failureCount={}", success.size(), failure.size());
            return Map.of("status", "Multiple records deleted successfully, But Some are not found.", "SuccessIDs", success.toString(), "UnsuccessfulIDs", failure.toString());
        }

    }

    @Override
    public Double withdraw(Integer userId, Double amount) {
        log.info("Withdrawal request received for userId={} amount={}", userId, amount);
        Account accountObj = accountRepository.findByUserId(userId).orElseThrow(() ->
                                                        new AccountNotFoundException("Bank Account details not found for user"));
        if (amount < 0) {
            log.error("Invalid withdrawal amount={} for userId={}", amount, userId);
            throw new InvalidAmountException("Withdrawal amount cannot be negative");
        } else if (accountObj.getBalance() < amount) {
            log.warn("Insufficient funds for userId={} ", userId);
            throw new InsufficientFundException("Insufficient balance in account");
        }

        Double withdrawAmount = accountObj.getBalance() - amount;
        accountObj.setBalance(withdrawAmount);
        Account saved = accountRepository.save(accountObj);
        log.info("Withdrawal successful");
        return saved.getBalance();
    }

    @Override
    public Double deposit(Integer userId, Double amount) {
        log.info("Deposit request received for userId={} amount={}", userId, amount);
        Account accountObj = accountRepository.findByUserId(userId).orElseThrow(() ->
                                                        new AccountNotFoundException("User not created Bank Account"));
        Double depositAmount = accountObj.getAccountId() + amount;
        accountObj.setBalance(depositAmount);
        Account saved = accountRepository.save(accountObj);
        log.info("Depositing amount={} for userId={} is successful", amount, userId);
        return saved.getBalance();
    }

    private Account mapToEntity(AccountDto accountDto) {
        Account accountObj = new Account();
        accountObj.setUserId(accountDto.getUserId());
        accountObj.setBalance(accountDto.getBalance());
        accountObj.setAccountType(accountDto.getAccountType());
        return accountObj;
    }

    private AccountResponse mapToResponse(Account saved) {
        return new AccountResponse(saved.getAccountId(), "Account created Successfully for userId:" + saved.getUserId(), saved.getBalance(), saved.getAccountType());
    }

}
