package com.teya.tiny_ledger.repository;

import com.teya.tiny_ledger.model.Account;

import java.util.Optional;

public interface AccountRepository {

    Account createAccount(Account account);

    Account updateAccount(Account accountToUpdate);

    Optional<Account> findAccountById(Integer accountId);

    Optional<Account> findAccountByEmail(String email);
}
