package com.teya.tiny_ledger.exception;

public class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String accountEmail) {
        super("Insufficient funds for account " + accountEmail);
    }
}
