package com.teya.tiny_ledger.service;

import com.teya.tiny_ledger.exception.AccountAlreadyExistsException;
import com.teya.tiny_ledger.model.Account;
import com.teya.tiny_ledger.repository.AccountRepository;
import com.teya.tiny_ledger.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    private AccountServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new AccountServiceImpl(accountRepository);
    }

    @Test
    void createAccount_returnsNewAccount_withZeroBalance() throws AccountAlreadyExistsException {
        Account request = new Account();
        request.setEmail("user@example.com");
        Account saved = new Account("user@example.com", BigDecimal.ZERO);
        when(accountRepository.findAccount("user@example.com")).thenReturn(Optional.empty());
        when(accountRepository.saveAccount(any())).thenReturn(saved);

        Account result = service.createAccount(request);

        assertEquals("user@example.com", result.getEmail());
        assertEquals(BigDecimal.ZERO, result.getBalance());
    }

    @Test
    void createAccount_throwsAccountAlreadyExistsException_whenEmailTaken() {
        Account request = new Account();
        request.setEmail("user@example.com");
        when(accountRepository.findAccount("user@example.com"))
                .thenReturn(Optional.of(new Account("user@example.com", BigDecimal.ZERO)));

        assertThrows(AccountAlreadyExistsException.class, () -> service.createAccount(request));
    }

    @Test
    void getBalance_returnsAccount_whenExists() throws AccountNotFoundException {
        Account account = new Account("user@example.com", BigDecimal.TEN);
        when(accountRepository.findAccount("user@example.com")).thenReturn(Optional.of(account));

        Account result = service.getBalance("user@example.com");

        assertEquals(BigDecimal.TEN, result.getBalance());
    }

    @Test
    void getBalance_throwsAccountNotFoundException_whenAccountDoesNotExist() {
        when(accountRepository.findAccount("user@example.com")).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> service.getBalance("user@example.com"));
    }
}
