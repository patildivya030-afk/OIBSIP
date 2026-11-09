import java.util.Scanner;

/**
 * Entry point. Seeds the Bank with a couple of demo accounts so the
 * evaluator/grader can log straight in, then hands control to the ATM.
 *
 * Demo credentials:
 *   User ID: 1001   PIN: 1234   (Alice, opening balance ₹5000)
 *   User ID: 1002   PIN: 5678   (Bob,   opening balance ₹2000)
 */
public class Main {

    public static void main(String[] args) {
        Bank bank = new Bank();
        bank.addAccount(new Account("1001", "Divya Patil", "1234", 5000.00));
        bank.addAccount(new Account("1002", "Riddhi Patil", "5678", 2000.00));
        bank.addAccount(new Account("1003", "Radha Shinde", "3750", 1000.00));


        Scanner scanner = new Scanner(System.in);
        ATM atm = new ATM(bank, scanner);
        atm.start();

        scanner.close();
    }
}
