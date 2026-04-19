package com.teya.tiny_ledger.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private int id;
    private String email;
    private BigDecimal balance;

    public Account(String email, BigDecimal balance) {
        this.email = email;
        this.balance = balance;
    }
}
