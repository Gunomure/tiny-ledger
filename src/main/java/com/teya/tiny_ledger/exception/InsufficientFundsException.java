package com.teya.tiny_ledger.exception;

public class InsufficientFundsException extends Exception {
    public InsufficientFundsException(Integer accountId) {
        super("Insufficient funds for account with id=" + accountId);
    }
}
