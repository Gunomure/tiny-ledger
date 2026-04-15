package com.teya.tiny_ledger.service;

import com.teya.tiny_ledger.exception.AccountAlreadyExistsException;
import com.teya.tiny_ledger.model.Account;

import javax.security.auth.login.AccountNotFoundException;

public interface AccountService {

    Account createAccount(Account account) throws AccountAlreadyExistsException;

    Account getBalance(String accountEmail) throws AccountNotFoundException;
}
