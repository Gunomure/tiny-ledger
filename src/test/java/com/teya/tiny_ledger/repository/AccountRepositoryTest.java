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
    void findAccountById_returnsEmpty_whenAccountDoesNotExist() {
        Optional<Account> result = repository.findAccountById(999);

        assertTrue(result.isEmpty());
    }

    @Test
    void findAccountByEmail_returnsEmpty_whenAccountDoesNotExist() {
        Optional<Account> result = repository.findAccountByEmail("nobody@example.com");

        assertTrue(result.isEmpty());
    }

    @Test
    void createAccount_storesAccount_andCanBeRetrievedByEmail() {
        Account account = new Account("user@example.com", BigDecimal.TEN);

        repository.createAccount(account);
        Optional<Account> result = repository.findAccountByEmail("user@example.com");

        assertTrue(result.isPresent());
        assertEquals("user@example.com", result.get().getEmail());
        assertEquals(BigDecimal.TEN, result.get().getBalance());
    }

    @Test
    void createAccount_storesAccount_andCanBeRetrievedById() {
        Account account = new Account("user@example.com", BigDecimal.TEN);

        Account saved = repository.createAccount(account);
        Optional<Account> result = repository.findAccountById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals("user@example.com", result.get().getEmail());
    }
}
