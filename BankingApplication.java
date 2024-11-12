import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

class User
{
    private String username;
    private String password;
    private ArrayList<Account> accounts;

    public User(String username, String password)
    {
        this.username = username;
        this.password = password;
        this.accounts = new ArrayList<>();
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public ArrayList<Account> getAccounts()
    {
        return accounts;
    }

    public void addAccount(Account account)
    {
        accounts.add(account);
    }
}

class Account
{
    private static int accountCounter = 1000;
    private String accountNumber;
    private String accountHolderName;
    private String accountType;
    private double balance;
    private ArrayList<Transaction> transactions;

    public Account(String accountHolderName, String accountType, double initialDeposit)
    {
        this.accountNumber = "AC" + (++accountCounter);
        this.accountHolderName = accountHolderName;
        this.accountType = accountType;
        this.balance = initialDeposit;
        this.transactions = new ArrayList<>();
    }

    public String getAccountNumber()
    {
        return accountNumber;
    }

    public String getAccountHolderName()
    {
        return accountHolderName;
    }

    public String getAccountType()
    {
        return accountType;
    }

    public double getBalance()
    {
        return balance;
    }

    public void deposit(double amount)
    {
        balance += amount;
        transactions.add(new Transaction("Deposit", amount));
        System.out.println("Deposit successful! New balance: $" + balance);
    }

    public void withdraw(double amount)
    {
        if (amount > balance)
        {
            System.out.println("Insufficient balance!");
        }
        else
        {
            balance -= amount;
            transactions.add(new Transaction("Withdrawal", amount));
            System.out.println("Withdrawal successful! New balance: $" + balance);
        }
    }

    public void displayStatement()
    {
        System.out.println("Transaction statement for account: " + accountNumber);
        for (Transaction t : transactions)
        {
            System.out.println(t);
        }
    }

    public void addMonthlyInterest(double rate)
    {
        if (accountType.equalsIgnoreCase("savings"))
        {
            double interest = balance * rate / 100;
            balance += interest;
            transactions.add(new Transaction("Interest", interest));
            System.out.println("Interest added! New balance: $" + balance);
        }
    }
}

class Transaction
{
    private static int transactionCounter = 100;
    private String transactionId;
    private Date date;
    private String type;
    private double amount;

    public Transaction(String type, double amount)
    {
        this.transactionId = "TX" + (++transactionCounter);
        this.date = new Date();
        this.type = type;
        this.amount = amount;
    }

    @Override
    public String toString()
    {
        return transactionId + " | " + date + " | " + type + " | $" + amount;
    }
}

public class BankingApplication
{
    private static ArrayList<User> users = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args)
    {
        while (true)
        {
            System.out.println("\nBanking Application Menu:");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice)
            {
                case 1 -> register();
                case 2 -> login();
                case 3 ->
                {
                    System.out.println("Exiting application.");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void register()
    {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        for (User user : users)
        {
            if (user.getUsername().equals(username))
            {
                System.out.println("Username already exists.");
                return;
            }
        }

        users.add(new User(username, password));
        System.out.println("User registered successfully!");
    }

    private static void login()
    {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User currentUser = null;
        for (User user : users)
        {
            if (user.getUsername().equals(username) && user.getPassword().equals(password))
            {
                currentUser = user;
                break;
            }
        }

        if (currentUser == null)
        {
            System.out.println("Invalid username or password.");
        }
        else
        {
            System.out.println("Login successful!");
            userMenu(currentUser);
        }
    }

    private static void userMenu(User user)
    {
        while (true)
        {
            System.out.println("\nUser Menu:");
            System.out.println("1. Open Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Check Balance");
            System.out.println("5. Print Statement");
            System.out.println("6. Add Monthly Interest");
            System.out.println("7. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice)
            {
                case 1 -> openAccount(user);
                case 2 -> deposit(user);
                case 3 -> withdraw(user);
                case 4 -> checkBalance(user);
                case 5 -> printStatement(user);
                case 6 -> addMonthlyInterest(user);
                case 7 ->
                {
                    System.out.println("Logging out.");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void openAccount(User user)
    {
        System.out.print("Enter account holder's name: ");
        String holderName = scanner.nextLine();
        System.out.print("Enter account type (Savings/Checking): ");
        String accountType = scanner.nextLine();
        System.out.print("Enter initial deposit amount: ");
        double initialDeposit = scanner.nextDouble();
        scanner.nextLine();

        Account account = new Account(holderName, accountType, initialDeposit);
        user.addAccount(account);
        System.out.println("Account opened successfully! Account Number: " + account.getAccountNumber());
    }

    private static void deposit(User user)
    {
        Account account = selectAccount(user);
        if (account != null)
        {
            System.out.print("Enter amount to deposit: ");
            double amount = scanner.nextDouble();
            account.deposit(amount);
        }
    }

    private static void withdraw(User user)
    {
        Account account = selectAccount(user);
        if (account != null)
        {
            System.out.print("Enter amount to withdraw: ");
            double amount = scanner.nextDouble();
            account.withdraw(amount);
        }
    }

    private static void checkBalance(User user)
    {
        Account account = selectAccount(user);
        if (account != null)
        {
            System.out.println("Current balance: $" + account.getBalance());
        }
    }

    private static void printStatement(User user)
    {
        Account account = selectAccount(user);
        if (account != null)
        {
            account.displayStatement();
        }
    }

    private static void addMonthlyInterest(User user)
    {
        Account account = selectAccount(user);
        if (account != null)
        {
            System.out.print("Enter monthly interest rate (in %): ");
            double rate = scanner.nextDouble();
            account.addMonthlyInterest(rate);
        }
    }

    private static Account selectAccount(User user)
    {
        System.out.println("Select an account:");
        for (int i = 0; i < user.getAccounts().size(); i++)
        {
            Account account = user.getAccounts().get(i);
            System.out.println((i + 1) + ". " + account.getAccountNumber() + " - " + account.getAccountType());
        }
        System.out.print("Enter account number: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice < 1 || choice > user.getAccounts().size())
        {
            System.out.println("Invalid account choice.");
            return null;
        }

        return user.getAccounts().get(choice - 1);
    }
}
