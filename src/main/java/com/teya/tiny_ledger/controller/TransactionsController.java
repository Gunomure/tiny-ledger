package com.teya.tiny_ledger.controller;

import com.teya.tiny_ledger.exception.AccountNotFoundException;
import com.teya.tiny_ledger.exception.InsufficientFundsException;
import com.teya.tiny_ledger.model.Account;
import com.teya.tiny_ledger.model.Transaction;
import com.teya.tiny_ledger.service.TransactionsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account/{accountId}")
public class TransactionsController {

    private final TransactionsService transactionsService;

    public TransactionsController(TransactionsService transactionsService) {
        this.transactionsService = transactionsService;
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Account> withdraw(
            @PathVariable Integer accountId,
            @RequestBody Transaction request
    ) throws InsufficientFundsException, AccountNotFoundException {
        Account updatedAccount = transactionsService.withdraw(accountId, request.getAmount());
        return ResponseEntity.ok(updatedAccount);
    }

    @PostMapping("/deposit")
    public ResponseEntity<Account> deposit(
            @PathVariable Integer accountId,
            @RequestBody Transaction request
    ) throws AccountNotFoundException {
        Account updatedAccount = transactionsService.deposit(accountId, request.getAmount());
        return ResponseEntity.ok(updatedAccount);
    }

    @GetMapping("/history")
    public ResponseEntity<List<Transaction>> getTransactionsHistory(
            @PathVariable Integer accountId
    ) throws AccountNotFoundException {
        List<Transaction> transactionHistories = transactionsService.getTransactionHistory(accountId);

        return ResponseEntity.ok(transactionHistories);
    }
}
