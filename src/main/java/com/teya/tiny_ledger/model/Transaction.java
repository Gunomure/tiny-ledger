package com.teya.tiny_ledger.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class Transaction {
    private String accountEmail;
    private BigDecimal amount;
    private TransactionType type;
}
