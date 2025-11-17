package banking.model;

public class ChequeAccount extends Account {
    private String employer;
    private String employerAddress;

    public ChequeAccount(String accountNumber, double initialDeposit, String branch, String openDate, String employer, String employerAddress) {
        super(accountNumber, initialDeposit, branch, openDate);
        this.employer = employer;
        this.employerAddress = employerAddress;
    }

    public String getEmployer() { return employer; }
    public String getEmployerAddress() { return employerAddress; }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Invalid amount.");
            return false;
        }
        if (amount > getBalance()) {
            System.out.println("Insufficient funds.");
            return false;
        }
        changeBalance(-amount);
        addTransaction(new Transaction("WITHDRAW", amount));
        System.out.printf("Withdrew %.2f from %s. New balance: %.2f%n", amount, getAccountNumber(), getBalance());
        return true;
    }

    @Override
    public double calculateMonthlyInterest() {
        // Cheque accounts do not earn interest in this design
        return 0.0;
    }
}
