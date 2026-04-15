package com.teya.tiny_ledger.service;

import com.teya.tiny_ledger.exception.InsufficientFundsException;
import com.teya.tiny_ledger.model.Account;
import com.teya.tiny_ledger.model.Transaction;
import com.teya.tiny_ledger.model.TransactionType;
import com.teya.tiny_ledger.repository.AccountRepository;
import com.teya.tiny_ledger.repository.TransactionsHistoryRepository;
import com.teya.tiny_ledger.service.impl.TransactionsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionsServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionsHistoryRepository accountHistoryRepository;

    private TransactionsServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new TransactionsServiceImpl(accountRepository, accountHistoryRepository);
    }

    @Test
    void deposit_increasesBalance_andReturnsNewBalance() throws AccountNotFoundException {
        Account account = new Account("user@example.com", BigDecimal.valueOf(100));
        when(accountRepository.findAccount("user@example.com")).thenReturn(Optional.of(account));
        when(accountRepository.saveAccount(any())).thenReturn(account);

        Account updatedAccount = service.deposit("user@example.com", BigDecimal.valueOf(50));

        assertEquals(BigDecimal.valueOf(150), updatedAccount.getBalance());
    }

    @Test
    void deposit_savesTransactionHistory() throws AccountNotFoundException {
        Account account = new Account("user@example.com", BigDecimal.valueOf(100));
        when(accountRepository.findAccount("user@example.com")).thenReturn(Optional.of(account));
        when(accountRepository.saveAccount(any())).thenReturn(account);

        service.deposit("user@example.com", BigDecimal.valueOf(50));

        verify(accountHistoryRepository).addTransactionHistory(
                argThat(t -> t.getType() == TransactionType.DEPOSIT
                        && t.getAmount().equals(BigDecimal.valueOf(50)))
        );
    }

    @Test
    void deposit_throwsAccountNotFoundException_whenAccountDoesNotExist() {
        when(accountRepository.findAccount("user@example.com")).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,
                () -> service.deposit("user@example.com", BigDecimal.valueOf(50)));
    }

    @Test
    void withdraw_decreasesBalance_andReturnsNewBalance() throws AccountNotFoundException, InsufficientFundsException {
        Account account = new Account("user@example.com", BigDecimal.valueOf(100));
        when(accountRepository.findAccount("user@example.com")).thenReturn(Optional.of(account));
        when(accountRepository.saveAccount(any())).thenReturn(account);

        Account updatedAccount = service.withdraw("user@example.com", BigDecimal.valueOf(40));

        assertEquals(BigDecimal.valueOf(60), updatedAccount.getBalance());
    }

    @Test
    void withdraw_savesTransactionHistory() throws AccountNotFoundException, InsufficientFundsException {
        Account account = new Account("user@example.com", BigDecimal.valueOf(100));
        when(accountRepository.findAccount("user@example.com")).thenReturn(Optional.of(account));
        when(accountRepository.saveAccount(any())).thenReturn(account);

        service.withdraw("user@example.com", BigDecimal.valueOf(40));

        verify(accountHistoryRepository).addTransactionHistory(
                argThat(t -> t.getType() == TransactionType.WITHDRAW
                        && t.getAmount().equals(BigDecimal.valueOf(-40)))
        );
    }

    @Test
    void withdraw_throwsInsufficientFundsException_whenBalanceTooLow() {
        Account account = new Account("user@example.com", BigDecimal.valueOf(30));
        when(accountRepository.findAccount("user@example.com")).thenReturn(Optional.of(account));

        assertThrows(InsufficientFundsException.class,
                () -> service.withdraw("user@example.com", BigDecimal.valueOf(50)));
    }

    @Test
    void withdraw_throwsAccountNotFoundException_whenAccountDoesNotExist() {
        when(accountRepository.findAccount("user@example.com")).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,
                () -> service.withdraw("user@example.com", BigDecimal.valueOf(50)));
    }

    @Test
    void getTransactionHistory_returnsHistoryFromRepository() throws AccountNotFoundException {
        Account account = new Account("user@example.com", BigDecimal.valueOf(30));
        when(accountRepository.findAccount("user@example.com")).thenReturn(Optional.of(account));

        List<Transaction> history = List.of(
                new Transaction("user@example.com", BigDecimal.TEN, TransactionType.DEPOSIT)
        );
        when(accountHistoryRepository.findTransactionstHistory("user@example.com")).thenReturn(history);

        List<Transaction> result = service.getTransactionHistory("user@example.com");

        assertEquals(1, result.size());
        assertEquals(TransactionType.DEPOSIT, result.get(0).getType());
    }
}
