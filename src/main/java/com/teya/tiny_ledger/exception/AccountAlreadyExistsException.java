package com.teya.tiny_ledger.exception;

public class AccountAlreadyExistsException extends Exception {
    public AccountAlreadyExistsException(String accountEmail) {
        super("Account " + accountEmail + " already exists");
    }
}
