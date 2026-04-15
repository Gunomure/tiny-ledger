package com.teya.tiny_ledger.repository.impl;

import com.teya.tiny_ledger.model.Account;
import com.teya.tiny_ledger.repository.AccountRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class AccountRepositoryImpl implements AccountRepository {
    private final Map<String, Account> accounts = new HashMap<>();

    @Override
    public Optional<Account> findAccount(String email) {
        return Optional.ofNullable(accounts.get(email));
    }

    @Override
    public Account saveAccount(Account account) {
        accounts.put(account.getEmail(), account);
        return account;
    }
}
