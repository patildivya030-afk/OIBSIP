import java.util.HashMap;
import java.util.Map;

/**
 * Owns the collection of all Accounts and mediates operations that touch
 * more than one account (transfers) or need account lookup (login).
 */
public class Bank {

    private final Map<String, Account> accounts;

    public Bank() {
        accounts = new HashMap<>();
    }

    public void addAccount(Account account) {
        accounts.put(account.getAccountId(), account);
    }

    public Account getAccount(String accountId) {
        return accounts.get(accountId);
    }

    public boolean accountExists(String accountId) {
        return accounts.containsKey(accountId);
    }

    /**
     * Authenticates a user by ID + PIN. Returns the Account on success,
     * or null if the ID doesn't exist or the PIN is wrong.
     */
    public Account authenticate(String accountId, String pin) {
        Account account = accounts.get(accountId);
        if (account != null && account.checkPin(pin)) {
            return account;
        }
        return null;
    }

    /**
     * Transfers funds between two accounts held at this bank.
     * Returns a result code: "OK", "INSUFFICIENT_FUNDS", or "NO_SUCH_ACCOUNT".
     */
    public String transfer(Account from, String toAccountId, double amount) {
        if (!accountExists(toAccountId)) {
            return "NO_SUCH_ACCOUNT";
        }
        if (toAccountId.equals(from.getAccountId())) {
            return "SELF_TRANSFER";
        }
        if (!from.hasSufficientBalance(amount)) {
            return "INSUFFICIENT_FUNDS";
        }
        Account to = accounts.get(toAccountId);
        from.recordTransferOut(amount, toAccountId);
        to.recordTransferIn(amount, from.getAccountId());
        return "OK";
    }
}
