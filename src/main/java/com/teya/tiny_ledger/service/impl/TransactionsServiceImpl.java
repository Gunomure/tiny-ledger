package com.teya.tiny_ledger.service.impl;

import com.teya.tiny_ledger.exception.AccountNotFoundException;
import com.teya.tiny_ledger.exception.BadRequestException;
import com.teya.tiny_ledger.exception.InsufficientFundsException;
import com.teya.tiny_ledger.model.Account;
import com.teya.tiny_ledger.model.Transaction;
import com.teya.tiny_ledger.model.TransactionType;
import com.teya.tiny_ledger.repository.AccountRepository;
import com.teya.tiny_ledger.repository.TransactionsHistoryRepository;
import com.teya.tiny_ledger.service.TransactionsService;
import org.springframework.stereotype.Service;

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
    public Account withdraw(Integer accountId, BigDecimal amount) throws AccountNotFoundException, InsufficientFundsException, BadRequestException {
        checkIncomingAmount(amount);

        Account account = getAccount(accountId);

        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(accountId);
        }

        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.updateAccount(account);

        // negate amount for history so we can just sum them up to get correct balance
        addTransactionHistory(accountId, amount.negate(), TransactionType.WITHDRAW);
        return account;
    }

    @Override
    public Account deposit(Integer accountId, BigDecimal amount) throws BadRequestException, AccountNotFoundException {
        checkIncomingAmount(amount);
        Account account = getAccount(accountId);

        account.setBalance(account.getBalance().add(amount));
        accountRepository.updateAccount(account);

        addTransactionHistory(accountId, amount, TransactionType.DEPOSIT);
        return account;
    }

    @Override
    public List<Transaction> getTransactionHistory(Integer accountId) throws AccountNotFoundException {
        // to check if the account exists to fail fast. Otherwise, empty list will be the same for not existing and existing without transactions accounts
        getAccount(accountId);
        return transactionsHistoryRepository.findTransactionstHistory(accountId);
    }

    /**
     * We receive only positive amount of money for both operations: withdraw and deposit
     *
     * @param amount
     * @throws BadRequestException
     */
    private void checkIncomingAmount(BigDecimal amount) throws BadRequestException {
        if (amount.signum() < 0) {
            throw new BadRequestException("Amount must be greater than or equal to zero");
        }
    }

    private Account getAccount(Integer accountId) throws AccountNotFoundException {
        Optional<Account> account = accountRepository.findAccountById(accountId);
        if (account.isEmpty()) {
            throw new AccountNotFoundException(accountId);
        }

        return account.get();
    }

    /**
     * To history creation I would have a separate service and create a facade where call withdraw and then save history
     * Due to simplification, I keep all logic here
     *
     * @param accountId
     * @param amount
     * @param transactionType
     */
    private void addTransactionHistory(Integer accountId, BigDecimal amount, TransactionType transactionType) {
        Transaction transactionHistory = new Transaction(accountId, amount, transactionType);
        transactionsHistoryRepository.addTransactionHistory(transactionHistory);
    }
}
