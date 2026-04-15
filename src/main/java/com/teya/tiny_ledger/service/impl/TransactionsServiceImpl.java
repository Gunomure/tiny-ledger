package com.teya.tiny_ledger.service.impl;

import com.teya.tiny_ledger.exception.InsufficientFundsException;
import com.teya.tiny_ledger.model.Account;
import com.teya.tiny_ledger.model.Transaction;
import com.teya.tiny_ledger.model.TransactionType;
import com.teya.tiny_ledger.repository.AccountRepository;
import com.teya.tiny_ledger.repository.TransactionsHistoryRepository;
import com.teya.tiny_ledger.service.TransactionsService;
import com.teya.tiny_ledger.exception.BadRequestException;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionsServiceImpl implements TransactionsService {
    private final AccountRepository accountRepository;
    private final TransactionsHistoryRepository transactionsHistoryRepository;

    public TransactionsServiceImpl(AccountRepository accountRepository, TransactionsHistoryRepository transactionsHistoryRepository) {
        this.accountRepository = accountRepository;
        this.transactionsHistoryRepository = transactionsHistoryRepository;
    }

    @Override
    public Account withdraw(String accountEmail, BigDecimal amount) throws AccountNotFoundException, InsufficientFundsException, BadRequestException {
        checkIncomingAmount(amount);

        Account account = getAccount(accountEmail);

        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(accountEmail);
        }

        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.saveAccount(account);

        // negate amount for history so we can just sum them up to get correct balance
        addTransactionHistory(accountEmail, amount.negate(), TransactionType.WITHDRAW);
        return account;
    }

    @Override
    public Account deposit(String accountEmail, BigDecimal amount) throws AccountNotFoundException, BadRequestException {
        checkIncomingAmount(amount);
        Account account = getAccount(accountEmail);

        account.setBalance(account.getBalance().add(amount));
        accountRepository.saveAccount(account);

        addTransactionHistory(accountEmail, amount, TransactionType.DEPOSIT);
        return  account;
    }

    @Override
    public List<Transaction> getTransactionHistory(String accountEmail) throws AccountNotFoundException {
        // to check if the account exists to fail fast. Otherwise, empty list will be the same for not existing and existing without transactions accounts
        getAccount(accountEmail);
        return transactionsHistoryRepository.findTransactionstHistory(accountEmail);
    }

    /**
     * We receive only positive amount of money for both operations: withdraw and deposit
     * @param amount
     * @throws BadRequestException
     */
    private void checkIncomingAmount(BigDecimal amount) throws BadRequestException {
        if (amount.signum() < 0) {
            throw new BadRequestException("Amount must be greater than or equal to zero");
        }
    }

    private Account getAccount(String accountEmail) throws AccountNotFoundException {
        Optional<Account> account = accountRepository.findAccount(accountEmail);
        if (account.isEmpty()) {
            throw new AccountNotFoundException(accountEmail);
        }

        return account.get();
    }

    /**
     * To history creation I would have a separate service and create a facade where call withdraw and then save history
     * Due to simplification, I keep all logic here
     * @param accountEmail
     * @param amount
     * @param transactionType
     */
    private void addTransactionHistory(String accountEmail, BigDecimal amount, TransactionType transactionType) {
        Transaction transactionHistory = new Transaction(accountEmail, amount, transactionType);
        transactionsHistoryRepository.addTransactionHistory(transactionHistory);
    }
}
