package banking.model;

/**
 * Marker for accounts that can earn interest.
 */
public interface InterestBearing {
    double calculateMonthlyInterest();
}
