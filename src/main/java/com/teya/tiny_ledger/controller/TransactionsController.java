package com.teya.tiny_ledger.controller;

import com.teya.tiny_ledger.exception.InsufficientFundsException;
import com.teya.tiny_ledger.model.Account;
import com.teya.tiny_ledger.model.Transaction;
import com.teya.tiny_ledger.service.TransactionsService;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/account/{accountEmail}")
public class TransactionsController {

    private final TransactionsService transactionsService;

    public TransactionsController(TransactionsService transactionsService) {
        this.transactionsService = transactionsService;
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Account> withdraw(
            @PathVariable String accountEmail,
            @RequestBody Transaction request
    ) throws AccountNotFoundException, InsufficientFundsException, BadRequestException {
        Account updatedAccount = transactionsService.withdraw(accountEmail, request.getAmount());
        return ResponseEntity.ok(updatedAccount);
    }

    @PostMapping("/deposit")
    public ResponseEntity<Account> deposit(
            @PathVariable String accountEmail,
            @RequestBody Transaction request
    ) throws AccountNotFoundException, BadRequestException {
        Account updatedAccount = transactionsService.deposit(accountEmail, request.getAmount());
        return ResponseEntity.ok(updatedAccount);
    }

    @GetMapping("/history")
    public ResponseEntity<List<Transaction>> getTransactionsHistory(
            @PathVariable String accountEmail
    ) throws AccountNotFoundException {
        List<Transaction> transactionHistories = transactionsService.getTransactionHistory(accountEmail);

        return ResponseEntity.ok(transactionHistories);
    }
}
