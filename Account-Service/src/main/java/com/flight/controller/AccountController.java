package com.flight.controller;

import com.flight.dto.AccountDto;
import com.flight.dto.AccountResponse;
import com.flight.model.Account;
import com.flight.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final  AccountService accountService;

    @PostMapping()
    public ResponseEntity<AccountResponse> createAccount(@RequestBody AccountDto accountDto){
        log.info("Received request to create account for userId={}", accountDto.getUserId());
        AccountResponse account = accountService.createAccount(accountDto);

        log.info("Account successfully created for userId={}", accountDto.getUserId());
        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }

    @GetMapping("/{accountId}")
    public  ResponseEntity<Account> getAccountById(@PathVariable Integer accountId){
        log.info("Received request to get account by Id={}", accountId);
        Account accountById = accountService.getAccountById(accountId);
        log.info("Request successful for accountId={}", accountId);
        return new ResponseEntity<>(accountById, HttpStatus.OK);
    }

    @GetMapping("")
    public  ResponseEntity<List<Account>> pagination(@RequestParam("pageNo") int pageNo, @RequestParam("totalElementInPage") int totalElementInPage){
        return new ResponseEntity<>(accountService.pagination(pageNo, totalElementInPage), HttpStatus.OK);
    }

    @GetMapping("/all")
    public  ResponseEntity<List<Account>> getAllAccounts(){
        return new ResponseEntity<>(accountService.getAllAccounts(), HttpStatus.OK);
    }

    @GetMapping("/count")
    public  ResponseEntity<Long> getCountOfAccounts(){
        return new ResponseEntity<>(accountService.getTotalAccounts(), HttpStatus.OK);
    }

    @GetMapping("/check")
    public ResponseEntity<String> isAccountExists(@RequestParam int accountId){
        return new ResponseEntity<>(accountService.isAccountExists(accountId), HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<String> deleteAccountById(@RequestParam int accountId){
        boolean isDeleted = accountService.deleteById(accountId);
        if (isDeleted) {
            return ResponseEntity.noContent().build(); // Return 204 No Content
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
        }
    }

    @DeleteMapping("/multiple")
    public ResponseEntity<Map<String, String>> deleteMultipleAccounts(@RequestBody List<Integer> accountList){
        System.out.println(accountList.toString());
        Map<String, String> result = accountService.deleteMultipleAccounts(accountList);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/withdraw")
    public ResponseEntity<Double> withdraw(@RequestParam Integer userId, @RequestParam Double amount){
        System.out.println(userId+ "  ----- "+ amount);
        Double withdrawAmount = accountService.withdraw(userId, amount);
        return ResponseEntity.ok(withdrawAmount);
    }

    @PutMapping("/deposit")
    public ResponseEntity<Double> deposit(@RequestParam Integer userId, @RequestParam Double amount){
        System.out.println(userId+ "  ----- "+ amount);
        Double deposited = accountService.deposit(userId, amount);
        return ResponseEntity.ok(deposited);
    }


}
