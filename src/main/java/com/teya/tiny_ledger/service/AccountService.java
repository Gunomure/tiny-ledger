package com.teya.tiny_ledger.service;

import com.teya.tiny_ledger.exception.AccountAlreadyExistsException;
import com.teya.tiny_ledger.exception.AccountNotFoundException;
import com.teya.tiny_ledger.model.Account;

public interface AccountService {

    Account createAccount(Account account) throws AccountAlreadyExistsException;

    Account getBalance(Integer accountId) throws AccountNotFoundException;
}
