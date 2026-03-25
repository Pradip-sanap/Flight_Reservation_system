package com.flight.dto;

import com.flight.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountResponse {
    private Integer accountId;
    private String message;
    private Double balance;
    private AccountType accountType;
}
