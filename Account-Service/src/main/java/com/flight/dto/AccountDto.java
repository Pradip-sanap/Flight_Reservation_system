package com.flight.dto;

import com.flight.enums.AccountType;
import lombok.Data;

@Data
public class AccountDto {
    private Integer userId;
    private Double balance;
    private AccountType accountType;
}
