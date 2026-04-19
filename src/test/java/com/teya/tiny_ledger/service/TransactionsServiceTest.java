package com.teya.tiny_ledger.service;

import com.teya.tiny_ledger.exception.AccountNotFoundException;
import com.teya.tiny_ledger.exception.BadRequestException;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    void deposit_increasesBalance_andReturnsNewBalance() throws AccountNotFoundException, BadRequestException {
        Account account = new Account("user@example.com", BigDecimal.valueOf(100));
        when(accountRepository.findAccountById(1)).thenReturn(Optional.of(account));

        Account updatedAccount = service.deposit(1, BigDecimal.valueOf(50));

        assertEquals(BigDecimal.valueOf(150), updatedAccount.getBalance());
    }

    @Test
    void deposit_savesTransactionHistory() throws AccountNotFoundException, BadRequestException {
        Account account = new Account("user@example.com", BigDecimal.valueOf(100));
        when(accountRepository.findAccountById(1)).thenReturn(Optional.of(account));

        service.deposit(1, BigDecimal.valueOf(50));

        verify(accountHistoryRepository).addTransactionHistory(
                argThat(t -> t.getType() == TransactionType.DEPOSIT
                        && t.getAmount().equals(BigDecimal.valueOf(50)))
        );
    }

    @Test
    void deposit_throwsAccountNotFoundException_whenAccountDoesNotExist() {
        when(accountRepository.findAccountById(1)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,
                () -> service.deposit(1, BigDecimal.valueOf(50)));
    }

    @Test
    void deposit_throwsBadRequestException_whenAmountIsNegative() {
        assertThrows(BadRequestException.class,
                () -> service.deposit(1, BigDecimal.valueOf(-50)));
    }

    @Test
    void withdraw_decreasesBalance_andReturnsNewBalance() throws AccountNotFoundException, InsufficientFundsException, BadRequestException {
        Account account = new Account("user@example.com", BigDecimal.valueOf(100));
        when(accountRepository.findAccountById(1)).thenReturn(Optional.of(account));

        Account updatedAccount = service.withdraw(1, BigDecimal.valueOf(40));

        assertEquals(BigDecimal.valueOf(60), updatedAccount.getBalance());
    }

    @Test
    void withdraw_savesTransactionHistory() throws AccountNotFoundException, InsufficientFundsException, BadRequestException {
        Account account = new Account("user@example.com", BigDecimal.valueOf(100));
        when(accountRepository.findAccountById(1)).thenReturn(Optional.of(account));

        service.withdraw(1, BigDecimal.valueOf(40));

        verify(accountHistoryRepository).addTransactionHistory(
                argThat(t -> t.getType() == TransactionType.WITHDRAW
                        && t.getAmount().equals(BigDecimal.valueOf(-40)))
        );
    }

    @Test
    void withdraw_throwsInsufficientFundsException_whenBalanceTooLow() {
        Account account = new Account("user@example.com", BigDecimal.valueOf(30));
        when(accountRepository.findAccountById(1)).thenReturn(Optional.of(account));

        assertThrows(InsufficientFundsException.class,
                () -> service.withdraw(1, BigDecimal.valueOf(50)));
    }

    @Test
    void withdraw_throwsAccountNotFoundException_whenAccountDoesNotExist() {
        when(accountRepository.findAccountById(1)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,
                () -> service.withdraw(1, BigDecimal.valueOf(50)));
    }

    @Test
    void withdraw_throwsBadRequestException_whenAmountIsNegative() {
        assertThrows(BadRequestException.class,
                () -> service.withdraw(1, BigDecimal.valueOf(-40)));
    }

    @Test
    void getTransactionHistory_returnsHistoryFromRepository() throws AccountNotFoundException {
        Account account = new Account("user@example.com", BigDecimal.valueOf(30));
        when(accountRepository.findAccountById(1)).thenReturn(Optional.of(account));

        List<Transaction> history = List.of(
                new Transaction(1, BigDecimal.TEN, TransactionType.DEPOSIT)
        );
        when(accountHistoryRepository.findTransactionstHistory(1)).thenReturn(history);

        List<Transaction> result = service.getTransactionHistory(1);

        assertEquals(1, result.size());
        assertEquals(TransactionType.DEPOSIT, result.get(0).getType());
    }
}
