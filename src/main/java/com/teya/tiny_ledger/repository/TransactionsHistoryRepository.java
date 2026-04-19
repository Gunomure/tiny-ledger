package com.teya.tiny_ledger.repository;

import com.teya.tiny_ledger.model.Transaction;

import java.util.List;

public interface TransactionsHistoryRepository {

    List<Transaction> findTransactionstHistory(Integer accountId);

    void addTransactionHistory(Transaction transactionHistory);
}
