package com.teya.tiny_ledger.exception;

public class AccountNotFoundException extends Exception {
    public AccountNotFoundException(String accountEmail) {
        super("Account " + accountEmail + " not found");
    }
}
