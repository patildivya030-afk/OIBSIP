import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single transaction performed on an Account.
 * Immutable record of what happened, when, and the resulting balance.
 */
public class Transaction {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String type;          // WITHDRAW, DEPOSIT, TRANSFER_OUT, TRANSFER_IN
    private final double amount;
    private final double balanceAfter;
    private final LocalDateTime timestamp;
    private final String details;       // extra info, e.g. recipient account id

    public Transaction(String type, double amount, double balanceAfter, String details) {
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.timestamp = LocalDateTime.now();
        this.details = details;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        String base = String.format("[%s] %-13s ₹%-10.2f Balance: ₹%-10.2f",
                timestamp.format(FORMATTER), type, amount, balanceAfter);
        if (details != null && !details.isEmpty()) {
            base += "  (" + details + ")";
        }
        return base;
    }
}
