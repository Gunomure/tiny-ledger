package com.teya.tiny_ledger.exception;

public class AccountNotFoundException extends Exception {

    public AccountNotFoundException(Integer accountId) {
        super("Account " + accountId + " not found");
    }
}
