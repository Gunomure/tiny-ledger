package com.teya.tiny_ledger.service;

import com.teya.tiny_ledger.exception.AccountNotFoundException;
import com.teya.tiny_ledger.exception.InsufficientFundsException;
import com.teya.tiny_ledger.model.Account;
import com.teya.tiny_ledger.model.Transaction;
import com.teya.tiny_ledger.exception.BadRequestException;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionsService {

    Account withdraw(Integer accountId, BigDecimal amount) throws InsufficientFundsException, BadRequestException, AccountNotFoundException;

    Account deposit(Integer accountId, BigDecimal amount) throws BadRequestException, AccountNotFoundException;

    List<Transaction> getTransactionHistory(Integer accountId) throws AccountNotFoundException;
}
