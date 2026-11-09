import java.util.List;
import java.util.Scanner;

public class ATM {

    private static final int MAX_LOGIN_ATTEMPTS = 3;

    private final Bank bank;
    private final Scanner scanner;

    public ATM(Bank bank, Scanner scanner) {
        this.bank = bank;
        this.scanner = scanner;
    }

    public void start() {
        System.out.println("========================================");
        System.out.println("        WELCOME TO JAVA ATM");
        System.out.println("========================================");

        boolean machineOn = true;

        while (machineOn) {
            Account account = login();
            if (account == null) {
                System.out.println("\nToo many incorrect attempts. Card retained. Goodbye.");
                return;
            }

            System.out.println("\nLogin successful. Welcome, " + account.getHolderName() + "!");
            String exitAction = runMenu(account);

            if ("QUIT".equals(exitAction)) {
                machineOn = false;
            } else {
                System.out.println("\n----------------------------------------");
            }
        }
    }

    private Account login() {
        int attemptsLeft = MAX_LOGIN_ATTEMPTS;

        while (attemptsLeft > 0) {
            System.out.print("\nEnter User ID: ");
            String id = scanner.nextLine().trim();
            System.out.print("Enter PIN: ");
            String pin = scanner.nextLine().trim();

            Account account = bank.authenticate(id, pin);
            if (account != null) {
                return account;
            }

            attemptsLeft--;
            if (attemptsLeft > 0) {
                System.out.println("Incorrect User ID or PIN. Attempts remaining: " + attemptsLeft);
            }
        }
        return null;
    }

    private String runMenu(Account account) {
        while (true) {
            printMenu(account);
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    showTransactionHistory(account);
                    break;
                case "2":
                    handleWithdraw(account);
                    break;
                case "3":
                    handleDeposit(account);
                    break;
                case "4":
                    handleTransfer(account);
                    break;
                case "5":
                    System.out.println("\nLogged out. Returning to login screen for the next user...");
                    return "LOGOUT";
                case "6":
                    System.out.println("\nThank you for banking with us. Goodbye, "
                            + account.getHolderName() + "!");
                    return "QUIT";
                default:
                    System.out.println("\nInvalid option. Please choose 1-6.");
            }
        }
    }

    private void printMenu(Account account) {
        System.out.println("\n----------------------------------------");
        System.out.printf("Current Balance: Rs.%.2f%n", account.getBalance());
        System.out.println("1. Transaction History");
        System.out.println("2. Withdraw");
        System.out.println("3. Deposit");
        System.out.println("4. Transfer");
        System.out.println("5. Logout / Switch Account");
        System.out.println("6. Quit");
        System.out.print("Choose an option: ");
    }

    private void showTransactionHistory(Account account) {
        List<Transaction> history = account.getHistory();

        System.out.println("\n----- Transaction History -----");
        if (history.isEmpty()) {
            System.out.println("No transactions yet.");
            return;
        }
        for (Transaction t : history) {
            System.out.println(t);
        }
    }

    private void handleWithdraw(Account account) {
        double amount = promptAmount("withdraw");
        if (amount <= 0) return;

        boolean success = account.withdraw(amount);
        if (success) {
            System.out.printf("Withdrawal successful. New balance: Rs.%.2f%n", account.getBalance());
        } else {
            System.out.println("Insufficient Funds. Withdrawal declined.");
        }
    }

    private void handleDeposit(Account account) {
        double amount = promptAmount("deposit");
        if (amount <= 0) return;

        account.deposit(amount);
        System.out.printf("Deposit successful. New balance: Rs.%.2f%n", account.getBalance());
    }

    private void handleTransfer(Account account) {
        System.out.print("Enter recipient Account ID: ");
        String toId = scanner.nextLine().trim();

        double amount = promptAmount("transfer");
        if (amount <= 0) return;

        String result = bank.transfer(account, toId, amount);
        switch (result) {
            case "OK":
                System.out.printf("Transfer successful. New balance: Rs.%.2f%n", account.getBalance());
                break;
            case "INSUFFICIENT_FUNDS":
                System.out.println("Insufficient Funds. Transfer declined.");
                break;
            case "NO_SUCH_ACCOUNT":
                System.out.println("No account found with ID: " + toId);
                break;
            case "SELF_TRANSFER":
                System.out.println("Cannot transfer to your own account.");
                break;
            default:
                System.out.println("Transfer could not be completed.");
        }
    }

    private double promptAmount(String actionVerb) {
        System.out.print("Enter amount to " + actionVerb + ": Rs.");
        String input = scanner.nextLine().trim();
        try {
            double amount = Double.parseDouble(input);
            if (amount <= 0) {
                System.out.println("Amount must be greater than zero.");
                return -1;
            }
            return amount;
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount entered.");
            return -1;
        }
    }
}