package com.teya.tiny_ledger.service.impl;

import com.teya.tiny_ledger.exception.AccountAlreadyExistsException;
import com.teya.tiny_ledger.exception.AccountNotFoundException;
import com.teya.tiny_ledger.model.Account;
import com.teya.tiny_ledger.repository.AccountRepository;
import com.teya.tiny_ledger.service.AccountService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Receives account data from endpoint and saves it.
     *
     * @param account - due to simplification I use Account class, but usually we have DTO for it
     * @return
     * @throws AccountAlreadyExistsException - If account with given email exists
     */
    @Override
    public Account createAccount(Account account) throws AccountAlreadyExistsException {
        Optional<Account> accountOpt = accountRepository.findAccountByEmail(account.getEmail());
        if (accountOpt.isPresent()) {
            throw new AccountAlreadyExistsException(account.getEmail());
        }
        account.setBalance(BigDecimal.ZERO);

        return accountRepository.createAccount(account);
    }

    /**
     * Find account by id (in real world it might be UUID).
     *
     * @param accountId
     * @return
     */
    @Override
    public Account getBalance(Integer accountId) throws AccountNotFoundException {
        Optional<Account> accountOpt = accountRepository.findAccountById(accountId);
        if (accountOpt.isEmpty()) {
            throw new AccountNotFoundException(accountId);
        }

        return accountOpt.get();
    }
}
