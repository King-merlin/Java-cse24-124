package banking.model;

public class InvestmentAccount extends Account implements InterestBearing {
    private static final double MONTHLY_RATE = 0.05; // 5% monthly
    private static final double MIN_BALANCE = 500.0;

    public InvestmentAccount(String accountNumber, double initialDeposit, String branch, String openDate) {
        super(accountNumber, initialDeposit, branch, openDate);
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Invalid amount.");
            return false;
        }
        double resulting = getBalance() - amount;
        if (resulting < MIN_BALANCE) {
            System.out.printf("Cannot withdraw %.2f. Investment accounts must keep a minimum balance of %.2f%n", amount, MIN_BALANCE);
            return false;
        }
        changeBalance(-amount);
        addTransaction(new Transaction("WITHDRAW", amount));
        System.out.printf("Withdrew %.2f from %s. New balance: %.2f%n", amount, getAccountNumber(), getBalance());
        return true;
    }

    @Override
    public double calculateMonthlyInterest() {
        double interest = getBalance() * MONTHLY_RATE;
        changeBalance(interest);
        addTransaction(new Transaction("INTEREST", interest));
        return interest;
    }
}
