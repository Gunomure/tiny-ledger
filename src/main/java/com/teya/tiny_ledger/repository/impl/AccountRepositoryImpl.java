package com.teya.tiny_ledger.repository.impl;

import com.teya.tiny_ledger.model.Account;
import com.teya.tiny_ledger.repository.AccountRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class AccountRepositoryImpl implements AccountRepository {
    private final List<Account> accounts = new ArrayList<>();
    private static int lastId = 1;

    @Override
    public Optional<Account> findAccountById(Integer accountId) {
        return accounts.stream()
                .filter(account -> Objects.equals(account.getId(), accountId))
                .findFirst();
    }

    @Override
    public Optional<Account> findAccountByEmail(String email) {
        return accounts.stream()
                .filter(account -> account.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public Account createAccount(Account account) {
        account.setId(lastId++);
        accounts.add(account);
        return account;
    }

    public Account updateAccount(Account accountToUpdate) {
        Optional<Account> accountById = findAccountById(accountToUpdate.getId());
        accountById.ifPresent(account -> account = accountToUpdate);

        return accountToUpdate;
    }
}
