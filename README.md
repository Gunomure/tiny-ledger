# Tiny Ledger

A simple in-memory ledger REST API built with Spring Boot. Supports account creation, balance checks, deposits, withdrawals, and transaction history.

## Assumptions

- **No currency** — balances and amounts are plain decimal numbers with no currency attached.
- **Integer ID as account identifier** — accounts are assigned an auto-incremented integer ID on creation. In real life this should be a generated UUID.
- **No separate DTOs** — the same `Account` and `Transaction` classes are used for requests, responses, and internal logic. In real life each layer would have its own entity, request DTO, and response DTO with dedicated mappers between them.

---

## Requirements

- Java 25
- Maven

## Running the Application

```bash
mvn spring-boot:run
```

The server starts on `http://localhost:8080` by default.

---

## API Endpoints

For ready-to-run HTTP requests see [requests.http](requests.http).

### Create Account

```
POST /account/create
```

**Request body:**
```json
{
  "email": "alice@example.com"
}
```

**Response `201 Created`:**
```json
{
  "id": 1,
  "email": "alice@example.com",
  "balance": 0
}
```

---

### Get Balance

```
GET /account/{accountId}/balance
```

**Response `200 OK`:**
```json
{
  "id": 1,
  "email": "alice@example.com",
  "balance": 150.00
}
```

---

### Deposit

```
POST /account/{accountId}/deposit
```

**Request body:**
```json
{
  "amount": 100.00
}
```

**Response `200 OK`:**
```json
{
  "id": 1,
  "email": "alice@example.com",
  "balance": 100.00
}
```

---

### Withdraw

```
POST /account/{accountId}/withdraw
```

**Request body:**
```json
{
  "amount": 50.00
}
```

**Response `200 OK`:**
```json
{
  "id": 1,
  "email": "alice@example.com",
  "balance": 50.00
}
```

---

### Get Transaction History

```
GET /account/{accountId}/history
```

**Response `200 OK`:**
```json
[
  {
    "accountId": 1,
    "amount": 100.00,
    "type": "DEPOSIT"
  },
  {
    "accountId": 1,
    "amount": -50.00,
    "type": "WITHDRAW"
  }
]
```

> Note: withdraw amounts are stored as negative values so the history can be summed to derive the balance.

---

## Error Responses

| Scenario | HTTP Status |
|---|---|
| Account with that email already exists | `409 Conflict` |
| Account not found | `404 Not Found` |
| Insufficient funds on withdrawal | `400 Bad Request` |
| Negative amount provided | `400 Bad Request` |
