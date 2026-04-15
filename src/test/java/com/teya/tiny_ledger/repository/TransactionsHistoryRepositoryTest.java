package com.teya.tiny_ledger.repository;

import com.teya.tiny_ledger.model.Transaction;
import com.teya.tiny_ledger.model.TransactionType;
import com.teya.tiny_ledger.repository.impl.TransactionsHistoryRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TransactionsHistoryRepositoryTest {
    private TransactionsHistoryRepository repository;

    @BeforeEach
    public void setup() {
        repository = new TransactionsHistoryRepositoryImpl();
    }

    @Test
    void findAccountHistory_returnsEmptyList_whenNoTransactionsExist() {
        List<Transaction> result = repository.findTransactionstHistory("user@example.com");

        assertTrue(result.isEmpty());
    }

    @Test
    void addTransactionHistory_storesTransaction_andCanBeRetrieved() {
        Transaction transaction = new Transaction("user@example.com", BigDecimal.TEN, TransactionType.DEPOSIT);

        repository.addTransactionHistory(transaction);
        List<Transaction> result = repository.findTransactionstHistory("user@example.com");

        assertEquals(1, result.size());
        assertEquals(BigDecimal.TEN, result.get(0).getAmount());
        assertEquals(TransactionType.DEPOSIT, result.get(0).getType());
    }

    @Test
    void findAccountHistory_returnsOnlyTransactionsForGivenEmail() {
        repository.addTransactionHistory(new Transaction("user@example.com", BigDecimal.TEN, TransactionType.DEPOSIT));
        repository.addTransactionHistory(new Transaction("other@example.com", BigDecimal.ONE, TransactionType.DEPOSIT));

        List<Transaction> result = repository.findTransactionstHistory("user@example.com");

        assertEquals(1, result.size());
        assertEquals("user@example.com", result.get(0).getAccountEmail());
    }

    @Test
    void findAccountHistory_returnsAllTransactionsForGivenEmail() {
        repository.addTransactionHistory(new Transaction("user@example.com", BigDecimal.TEN, TransactionType.DEPOSIT));
        repository.addTransactionHistory(new Transaction("user@example.com", BigDecimal.ONE, TransactionType.WITHDRAW));

        List<Transaction> result = repository.findTransactionstHistory("user@example.com");

        assertEquals(2, result.size());
    }
}
