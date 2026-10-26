# Java Development — Task 3: ATM Interface

Console-based ATM simulation using OOP with 5 classes: `ATM`, `Account`, `Transaction`, `Bank`, `Main`.

## Features
- User ID + PIN login; access denied after 3 incorrect attempts
- Menu: 1) Transaction History 2) Withdraw 3) Deposit 4) Transfer 5) Quit
- Balance check before withdrawal/transfer → "Insufficient Funds"
- Transactions stored in an `ArrayList` and shown in history
- Encapsulation (private fields + getters)

## Demo accounts
`1001` / `1234` and `1002` / `0000`.

## Run
```bash
javac *.java
java Main
```

Author: Steven Manuel Rios
