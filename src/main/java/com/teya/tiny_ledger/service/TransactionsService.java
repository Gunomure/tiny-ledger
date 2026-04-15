package com.teya.tiny_ledger.service;

import com.teya.tiny_ledger.exception.InsufficientFundsException;
import com.teya.tiny_ledger.model.Account;
import com.teya.tiny_ledger.model.Transaction;
import com.teya.tiny_ledger.exception.BadRequestException;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.List;

public interface TransactionsService {

    Account withdraw(String accountEmail, BigDecimal amount) throws AccountNotFoundException, InsufficientFundsException, BadRequestException;

    Account deposit(String accountEmail, BigDecimal amount) throws AccountNotFoundException, BadRequestException;

    List<Transaction> getTransactionHistory(String accountEmail) throws AccountNotFoundException;
}
