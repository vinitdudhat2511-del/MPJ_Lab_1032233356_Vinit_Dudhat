import java.io.*;
import java.util.*;

// ─────────────────────────────────────────────
//  User-Defined Exceptions
// ─────────────────────────────────────────────

class MinimumBalanceException extends Exception {
    public MinimumBalanceException() {
        super("Account creation failed: Minimum initial deposit is Rs. 1000.");
    }
}

class InsufficientFundsException extends Exception {
    public InsufficientFundsException(double balance) {
        super("Withdrawal failed: Insufficient funds. Available balance: Rs. " + balance);
    }
}

class InvalidCustomerIDException extends Exception {
    public InvalidCustomerIDException() {
        super("Invalid Customer ID: CID must be in the range 1 to 20.");
    }
}

class NegativeAmountException extends Exception {
    public NegativeAmountException() {
        super("Invalid Amount: Amount must be a positive value.");
    }
}

class CustomerNotFoundException extends Exception {
    public CustomerNotFoundException(int cid) {
        super("No account found with Customer ID: " + cid);
    }
}

class DuplicateCustomerIDException extends Exception {
    public DuplicateCustomerIDException(int cid) {
        super("Customer ID " + cid + " already exists. Please use a different ID.");
    }
}

// ─────────────────────────────────────────────
//  Customer Model (Serializable for file I/O)
// ─────────────────────────────────────────────

class Customer implements Serializable {
    private static final long serialVersionUID = 1L;

    private int    cid;
    private String cname;
    private double amount;

    public Customer(int cid, String cname, double amount) {
        this.cid    = cid;
        this.cname  = cname;
        this.amount = amount;
    }

    public int    getCid()    { return cid; }
    public String getCname()  { return cname; }
    public double getAmount() { return amount; }

    public void setAmount(double amount) { this.amount = amount; }

    @Override
    public String toString() {
        return String.format("| %-4d | %-20s | Rs. %-12.2f |", cid, cname, amount);
    }
}

// ─────────────────────────────────────────────
//  File I/O Helper
// ─────────────────────────────────────────────

class FileHandler {
    private static final String FILE_PATH = "bank_records.dat";

    /** Load all customers from file. Returns empty map if file doesn't exist. */
    @SuppressWarnings("unchecked")
    public static Map<Integer, Customer> loadRecords() {
        File f = new File(FILE_PATH);
        if (!f.exists()) return new LinkedHashMap<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            return (Map<Integer, Customer>) ois.readObject();
        } catch (Exception e) {
            System.out.println("[Warning] Could not read existing records: " + e.getMessage());
            return new LinkedHashMap<>();
        }
    }

    /** Persist all customers to file. */
    public static void saveRecords(Map<Integer, Customer> records) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(records);
        } catch (IOException e) {
            System.out.println("[Error] Could not save records: " + e.getMessage());
        }
    }

    /** Append a single transaction log entry to a human-readable text log. */
    public static void logTransaction(String entry) {
        try (FileWriter fw = new FileWriter("transaction_log.txt", true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(new Date() + " | " + entry);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("[Warning] Could not write to transaction log.");
        }
    }
}

// ─────────────────────────────────────────────
//  Validation Helper
// ─────────────────────────────────────────────

class Validator {
    public static void validateCID(int cid) throws InvalidCustomerIDException {
        if (cid < 1 || cid > 20) throw new InvalidCustomerIDException();
    }

    public static void validatePositiveAmount(double amount) throws NegativeAmountException {
        if (amount <= 0) throw new NegativeAmountException();
    }

    public static void validateMinimumBalance(double amount) throws MinimumBalanceException {
        if (amount < 1000) throw new MinimumBalanceException();
    }

    public static void validateWithdrawal(double wthAmt, double balance)
            throws InsufficientFundsException {
        if (wthAmt > balance) throw new InsufficientFundsException(balance);
    }

    public static void validateNoDuplicate(int cid, Map<Integer, Customer> records)
            throws DuplicateCustomerIDException {
        if (records.containsKey(cid)) throw new DuplicateCustomerIDException(cid);
    }

    public static Customer validateExists(int cid, Map<Integer, Customer> records)
            throws CustomerNotFoundException {
        Customer c = records.get(cid);
        if (c == null) throw new CustomerNotFoundException(cid);
        return c;
    }
}

// ─────────────────────────────────────────────
//  Main Banking Application
// ─────────────────────────────────────────────

public class BankingSystem {

    private static Map<Integer, Customer> records;
    private static final Scanner sc = new Scanner(System.in);

    // ── Utilities ────────────────────────────

    private static void printHeader() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║       JAVA BANKING SYSTEM  v1.0      ║");
        System.out.println("╚══════════════════════════════════════╝");
    }

    private static void printMenu() {
        System.out.println("\n┌──────────────────────────────────────┐");
        System.out.println("│              MAIN MENU               │");
        System.out.println("├──────────────────────────────────────┤");
        System.out.println("│  1. Create New Account               │");
        System.out.println("│  2. Deposit Amount                   │");
        System.out.println("│  3. Withdraw Amount                  │");
        System.out.println("│  4. Check Balance                    │");
        System.out.println("│  5. Display All Accounts             │");
        System.out.println("│  6. Delete Account                   │");
        System.out.println("│  7. Exit                             │");
        System.out.println("└──────────────────────────────────────┘");
        System.out.print("  Enter your choice: ");
    }

    private static void divider() {
        System.out.println("──────────────────────────────────────────");
    }

    /** Safely read an integer, re-prompting on bad input. */
    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  [!] Please enter a valid integer.");
            }
        }
    }

    /** Safely read a double, re-prompting on bad input. */
    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  [!] Please enter a valid numeric amount.");
            }
        }
    }

    // ── Banking Operations ────────────────────

    /** 1. Create account */
    private static void createAccount() {
        divider();
        System.out.println("  [ CREATE NEW ACCOUNT ]");
        divider();
        try {
            int cid = readInt("  Enter Customer ID (1-20)  : ");
            Validator.validateCID(cid);
            Validator.validateNoDuplicate(cid, records);

            System.out.print("  Enter Customer Name        : ");
            String cname = sc.nextLine().trim();
            if (cname.isEmpty()) { System.out.println("  [!] Name cannot be empty."); return; }

            double amount = readDouble("  Enter Initial Deposit (min Rs.1000): ");
            Validator.validatePositiveAmount(amount);
            Validator.validateMinimumBalance(amount);

            Customer c = new Customer(cid, cname, amount);
            records.put(cid, c);
            FileHandler.saveRecords(records);
            FileHandler.logTransaction("CREATED  | CID=" + cid + " | Name=" + cname + " | Amount=" + amount);

            System.out.println("\n  ✔ Account created successfully!");
            System.out.println("  " + c);

        } catch (InvalidCustomerIDException | MinimumBalanceException |
                 NegativeAmountException    | DuplicateCustomerIDException e) {
            System.out.println("\n  [Exception] " + e.getMessage());
        }
    }

    /** 2. Deposit */
    private static void deposit() {
        divider();
        System.out.println("  [ DEPOSIT AMOUNT ]");
        divider();
        try {
            int cid = readInt("  Enter Customer ID: ");
            Validator.validateCID(cid);
            Customer c = Validator.validateExists(cid, records);

            double amt = readDouble("  Enter Deposit Amount: ");
            Validator.validatePositiveAmount(amt);

            c.setAmount(c.getAmount() + amt);
            FileHandler.saveRecords(records);
            FileHandler.logTransaction("DEPOSIT  | CID=" + cid + " | Amount=" + amt + " | Balance=" + c.getAmount());

            System.out.printf("%n  ✔ Rs. %.2f deposited. New Balance: Rs. %.2f%n", amt, c.getAmount());

        } catch (InvalidCustomerIDException | CustomerNotFoundException |
                 NegativeAmountException e) {
            System.out.println("\n  [Exception] " + e.getMessage());
        }
    }

    /** 3. Withdraw */
    private static void withdraw() {
        divider();
        System.out.println("  [ WITHDRAW AMOUNT ]");
        divider();
        try {
            int cid = readInt("  Enter Customer ID: ");
            Validator.validateCID(cid);
            Customer c = Validator.validateExists(cid, records);

            double amt = readDouble("  Enter Withdrawal Amount: ");
            Validator.validatePositiveAmount(amt);
            Validator.validateWithdrawal(amt, c.getAmount());

            c.setAmount(c.getAmount() - amt);
            FileHandler.saveRecords(records);
            FileHandler.logTransaction("WITHDRAW | CID=" + cid + " | Amount=" + amt + " | Balance=" + c.getAmount());

            System.out.printf("%n  ✔ Rs. %.2f withdrawn. New Balance: Rs. %.2f%n", amt, c.getAmount());

        } catch (InvalidCustomerIDException | CustomerNotFoundException |
                 NegativeAmountException    | InsufficientFundsException e) {
            System.out.println("\n  [Exception] " + e.getMessage());
        }
    }

    /** 4. Check balance */
    private static void checkBalance() {
        divider();
        System.out.println("  [ CHECK BALANCE ]");
        divider();
        try {
            int cid = readInt("  Enter Customer ID: ");
            Validator.validateCID(cid);
            Customer c = Validator.validateExists(cid, records);

            System.out.printf("%n  Account Holder : %s%n", c.getCname());
            System.out.printf("  Customer ID    : %d%n",   c.getCid());
            System.out.printf("  Balance        : Rs. %.2f%n", c.getAmount());

        } catch (InvalidCustomerIDException | CustomerNotFoundException e) {
            System.out.println("\n  [Exception] " + e.getMessage());
        }
    }

    /** 5. Display all accounts */
    private static void displayAll() {
        divider();
        System.out.println("  [ ALL ACCOUNT RECORDS ]");
        divider();
        if (records.isEmpty()) {
            System.out.println("  No records found.");
            return;
        }
        System.out.println("  +------+----------------------+------------------+");
        System.out.println("  | CID  | Customer Name        | Balance          |");
        System.out.println("  +------+----------------------+------------------+");
        for (Customer c : records.values()) {
            System.out.println("  " + c);
        }
        System.out.println("  +------+----------------------+------------------+");
        System.out.println("  Total accounts: " + records.size());
    }

    /** 6. Delete account */
    private static void deleteAccount() {
        divider();
        System.out.println("  [ DELETE ACCOUNT ]");
        divider();
        try {
            int cid = readInt("  Enter Customer ID to delete: ");
            Validator.validateCID(cid);
            Customer c = Validator.validateExists(cid, records);

            System.out.printf("  Deleting account of: %s (CID: %d)%n", c.getCname(), c.getCid());
            System.out.print("  Confirm? (yes/no): ");
            String confirm = sc.nextLine().trim().toLowerCase();

            if (confirm.equals("yes")) {
                records.remove(cid);
                FileHandler.saveRecords(records);
                FileHandler.logTransaction("DELETED  | CID=" + cid + " | Name=" + c.getCname());
                System.out.println("\n  ✔ Account deleted successfully.");
            } else {
                System.out.println("  Deletion cancelled.");
            }

        } catch (InvalidCustomerIDException | CustomerNotFoundException e) {
            System.out.println("\n  [Exception] " + e.getMessage());
        }
    }

    // ── Entry Point ───────────────────────────

    public static void main(String[] args) {
        records = FileHandler.loadRecords();   // load persisted data on startup
        printHeader();
        System.out.println("  Records loaded from file: " + records.size());

        boolean running = true;
        while (running) {
            printMenu();
            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  [!] Invalid choice. Please enter a number 1-7.");
                continue;
            }

            switch (choice) {
                case 1 -> createAccount();
                case 2 -> deposit();
                case 3 -> withdraw();
                case 4 -> checkBalance();
                case 5 -> displayAll();
                case 6 -> deleteAccount();
                case 7 -> {
                    System.out.println("\n  Goodbye! All records saved.");
                    running = false;
                }
                default -> System.out.println("  [!] Invalid choice. Enter 1-7.");
            }
        }
        sc.close();
    }
}
