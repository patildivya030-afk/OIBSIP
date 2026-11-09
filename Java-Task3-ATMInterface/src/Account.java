import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a single bank account: identity, PIN, balance and its
 * own transaction history.
 */
public class Account {

    private final String accountId;
    private final String holderName;
    private String pin;
    private double balance;
    private final List<Transaction> history;

    public Account(String accountId, String holderName, String pin, double openingBalance) {
        this.accountId = accountId;
        this.holderName = holderName;
        this.pin = pin;
        this.balance = openingBalance;
        this.history = new ArrayList<>();
    }

    public String getAccountId() {
        return accountId;
    }

    public String getHolderName() {
        return holderName;
    }

    public double getBalance() {
        return balance;
    }

    public boolean checkPin(String enteredPin) {
        return pin.equals(enteredPin);
    }

    public void changePin(String newPin) {
        this.pin = newPin;
    }

    /** Adds funds to the account and logs the transaction. */
    public void deposit(double amount) {
        balance += amount;
        history.add(new Transaction("DEPOSIT", amount, balance, null));
    }

    /**
     * Attempts to withdraw funds. Returns false (no state change) if the
     * balance is insufficient.
     */
    public boolean withdraw(double amount) {
        if (amount > balance) {
            return false;
        }
        balance -= amount;
        history.add(new Transaction("WITHDRAW", amount, balance, null));
        return true;
    }

    /** Records an outgoing transfer once the Bank has verified funds. */
    public void recordTransferOut(double amount, String toAccountId) {
        balance -= amount;
        history.add(new Transaction("TRANSFER_OUT", amount, balance, "to " + toAccountId));
    }

    /** Records an incoming transfer credited from another account. */
    public void recordTransferIn(double amount, String fromAccountId) {
        balance += amount;
        history.add(new Transaction("TRANSFER_IN", amount, balance, "from " + fromAccountId));
    }

    public boolean hasSufficientBalance(double amount) {
        return balance >= amount;
    }

    /** Returns the transaction history, most recent first. */
    public List<Transaction> getHistory() {
        List<Transaction> copy = new ArrayList<>(history);
        Collections.reverse(copy);
        return copy;
    }
}
