package com.teya.tiny_ledger.controller;

import com.teya.tiny_ledger.exception.AccountAlreadyExistsException;
import com.teya.tiny_ledger.model.Account;
import com.teya.tiny_ledger.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;


    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(
            @RequestBody Account createAccountRequest
    ) throws AccountAlreadyExistsException {
        Account account = accountService.createAccount(createAccountRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(account);
    }

    @GetMapping("/{accountEmail}/balance")
    public ResponseEntity<Account> getBalance(
            @PathVariable String accountEmail
    ) throws AccountNotFoundException {
        // in real world I would map it to some DTO with enough info to see the balance
        Account account = accountService.getBalance(accountEmail);
        return ResponseEntity.ok(account);
    }
}
