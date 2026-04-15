package com.teya.tiny_ledger.repository.impl;

import com.teya.tiny_ledger.model.Transaction;
import com.teya.tiny_ledger.repository.TransactionsHistoryRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionsHistoryRepositoryImpl implements TransactionsHistoryRepository {
    private static final List<Transaction> transactions = new ArrayList<>();

    @Override
    public List<Transaction> findTransactionstHistory(String accountEmail) {
        return transactions.stream()
                .filter(transaction -> transaction.getAccountEmail().equals(accountEmail))
                .toList();
    }

    @Override
    public void addTransactionHistory(Transaction transactionHistory) {
        transactions.add(transactionHistory);
    }
}
