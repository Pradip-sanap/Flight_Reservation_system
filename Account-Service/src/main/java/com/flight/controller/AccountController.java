package com.flight.controller;

import com.flight.dto.AccountDto;
import com.flight.dto.AccountResponse;
import com.flight.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final  AccountService accountService;

    @PostMapping()
    public ResponseEntity<AccountResponse> createAccount(@RequestBody AccountDto accountDto){
        AccountResponse account = accountService.createAccount(accountDto);
        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }

//    Double withdraw(Integer userId, Double amount);
//    Double deposit(Integer userId, Double amount);
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
