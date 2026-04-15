package com.teya.tiny_ledger.repository;

import com.teya.tiny_ledger.model.Account;
import com.teya.tiny_ledger.repository.impl.AccountRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AccountRepositoryTest {
    private AccountRepository repository;

    @BeforeEach
    void setUp() {
        repository = new AccountRepositoryImpl();
    }

    @Test
    void findAccount_returnsEmpty_whenAccountDoesNotExist() {
        Optional<Account> result = repository.findAccount("unknown@example.com");

        assertTrue(result.isEmpty());
    }

    @Test
    void saveAccount_storesAccount_andCanBeRetrieved() {
        Account account = new Account("user@example.com", BigDecimal.TEN);

        repository.saveAccount(account);
        Optional<Account> result = repository.findAccount("user@example.com");

        assertTrue(result.isPresent());
        assertEquals("user@example.com", result.get().getEmail());
        assertEquals(BigDecimal.TEN, result.get().getBalance());
    }

    @Test
    void saveAccount_overwritesExistingAccount() {
        repository.saveAccount(new Account("user@example.com", BigDecimal.TEN));
        repository.saveAccount(new Account("user@example.com", BigDecimal.ONE));

        Optional<Account> result = repository.findAccount("user@example.com");

        assertTrue(result.isPresent());
        assertEquals(BigDecimal.ONE, result.get().getBalance());
    }
}
