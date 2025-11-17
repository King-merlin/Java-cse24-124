package banking.model;

public class SavingsAccount extends Account implements InterestBearing {
    private static final double MONTHLY_RATE = 0.0005; // 0.05% monthly

    public SavingsAccount(String accountNumber, double initialDeposit, String branch, String openDate) {
        super(accountNumber, initialDeposit, branch, openDate);
    }

    @Override
    public boolean withdraw(double amount) {
        System.out.println("Withdrawals are not allowed from Savings accounts.");
        return false;
    }

    @Override
    public double calculateMonthlyInterest() {
        double interest = getBalance() * MONTHLY_RATE;
        // apply interest
        changeBalance(interest);
        addTransaction(new Transaction("INTEREST", interest));
        return interest;
    }
}
