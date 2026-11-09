# Java ATM Interface — OIBSIP Java Development Task 3

A console-based ATM simulation built in Java using object-oriented design.
Users authenticate with a User ID and PIN, then perform withdrawals,
deposits, transfers, and view their transaction history — all backed by
in-memory data structures.

## Objective

Simulate the core functionality of a real ATM: secure login, balance-aware
transactions, and a running transaction log, built using proper OOP
separation of concerns.

## Tech Stack

- Java (JDK 17+), console application
- No external libraries — pure `java.util` / `java.time`

## Class Design

| Class | Responsibility |
|---|---|
| `Main` | Entry point. Seeds the `Bank` with demo accounts and launches the ATM session. |
| `Bank` | Owns all accounts. Handles authentication and cross-account transfers. |
| `Account` | Represents one customer's account — balance, PIN, and its own transaction history. |
| `Transaction` | Immutable record of a single transaction (type, amount, resulting balance, timestamp). |
| `ATM` | Drives the console UI: login flow, main menu, and each transaction's user interaction. |

This keeps banking *state* (Account, Bank) fully separate from the
*presentation* layer (ATM), and each transaction is captured as its own
object rather than a loose string, which is what makes the Transaction
History screen possible.

## Features

- Login with User ID + PIN; **access denied after 3 incorrect attempts**
- Main menu:
  1. Transaction History — full log for the session, most recent first
  2. Withdraw — validates sufficient balance before deducting
  3. Deposit — credits the account instantly
  4. Transfer — moves funds to another account by ID, with balance and
     existence checks
  5. Quit — ends the session with a goodbye message
- **Insufficient Funds** message shown on any withdrawal/transfer that
  would overdraw the account (balance is never allowed to go negative)
- All transactions stored in an `ArrayList<Transaction>` inside each
  `Account` and rendered in the Transaction History screen
- Input validation on amounts (rejects non-numeric or non-positive values)

## Demo Accounts

The app seeds two accounts on startup so you can log in immediately:

| User ID | PIN | Holder | Opening Balance |
|---|---|---|---|
| `1001` | `1234` | Alice Sharma | ₹5000.00 |
| `1002` | `5678` | Bob Verma | ₹2000.00 |

Use account `1002` as a transfer recipient when testing the Transfer
feature from account `1001`, and vice versa.

## How to Run

```bash
cd src
javac *.java
java Main
```

Then follow the on-screen prompts:

```
Enter User ID: 1001
Enter PIN: 1234
```

## Sample Session

```
========================================
        WELCOME TO JAVA ATM
========================================

Enter User ID: 1001
Enter PIN: 1234

Login successful. Welcome, Alice Sharma!

----------------------------------------
Current Balance: ₹5000.00
1. Transaction History
2. Withdraw
3. Deposit
4. Transfer
5. Quit
Choose an option: 2
Enter amount to withdraw: ₹1500
Withdrawal successful. New balance: ₹3500.00

----------------------------------------
Current Balance: ₹3500.00
...
Choose an option: 4
Enter recipient Account ID: 1002
Enter amount to transfer: ₹500
Transfer successful. New balance: ₹3000.00

...
Choose an option: 1

----- Transaction History -----
[2026-09-25 10:14:02] TRANSFER_OUT  ₹500.00     Balance: ₹3000.00    (to 1002)
[2026-09-25 10:12:47] WITHDRAW      ₹1500.00    Balance: ₹3500.00
```

## Design Notes

- **Encapsulation**: `Account` never exposes its `balance` field directly —
  all mutation happens through `deposit()`, `withdraw()`, and the
  transfer-record methods, so balance can never be corrupted from outside.
- **Insufficient funds check** happens *before* any state change, so a
  failed withdrawal or transfer leaves the account untouched.
- **Transfers** are mediated by `Bank` rather than `Account`, since a
  transfer touches two accounts and `Account` shouldn't need to know about
  other accounts to do its job.
- This is an in-memory simulation — no database or file persistence.
  Data resets each time the program restarts, which matches the scope of
  the task brief.

## Author

Built as part of the Oasis Infobyte Summer Internship Program (SIP) —
Java Development track, Task 3.
