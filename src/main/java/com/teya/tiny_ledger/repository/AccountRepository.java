package com.teya.tiny_ledger.repository;

import com.teya.tiny_ledger.model.Account;

import java.util.Optional;

public interface AccountRepository {

    Optional<Account> findAccount(String email);

    Account saveAccount(Account account);
}
