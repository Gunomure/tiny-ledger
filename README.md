# Tiny Ledger

A simple in-memory ledger REST API built with Spring Boot. Supports account creation, balance checks, deposits, withdrawals, and transaction history.

## Assumptions

- **No currency** — balances and amounts are plain decimal numbers with no currency attached.
- **Email as user identifier** — email is used as the unique account ID for easier manual testing. In real life this should be a generated UUID.
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
  "email": "alice@example.com",
  "balance": 0
}
```

**Response `201 Created`:**
```json
{
  "email": "alice@example.com",
  "balance": 0
}
```

---

### Get Balance

```
GET /account/{accountEmail}/balance
```

**Response `200 OK`:**
```json
{
  "email": "alice@example.com",
  "balance": 150.00
}
```

---

### Deposit

```
POST /account/{accountEmail}/deposit
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
  "email": "alice@example.com",
  "balance": 100.00
}
```

---

### Withdraw

```
POST /account/{accountEmail}/withdraw
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
  "email": "alice@example.com",
  "balance": 50.00
}
```

---

### Get Transaction History

```
GET /account/{accountEmail}/history
```

**Response `200 OK`:**
```json
[
  {
    "accountEmail": "alice@example.com",
    "amount": 100.00,
    "type": "DEPOSIT"
  },
  {
    "accountEmail": "alice@example.com",
    "amount": 50.00,
    "type": "WITHDRAW"
  }
]
```

---

## Error Responses

| Scenario | HTTP Status |
|---|---|
| Account with that email already exists | `409 Conflict` |
| Account not found | `404 Not Found` |
| Insufficient funds on withdrawal | `400 Bad Request` |
| Negative amount provided | `400 Bad Request` |
