package banking.model;


import java.io.Serializable;

public class SessionManager implements Serializable {
    private static Customer currentCustomer;
    private static boolean isAdminLoggedIn = false;

    public static void setCustomer(Customer c) {
        currentCustomer = c;
        isAdminLoggedIn = false;
    }

    public static Customer getCustomer() {
        return currentCustomer;
    }

    public static void setAdmin(boolean loggedIn) {
        isAdminLoggedIn = loggedIn;
        currentCustomer = null;
    }

    public static boolean isAdmin() {
        return isAdminLoggedIn;
    }

    public static void logout() {
        currentCustomer = null;
        isAdminLoggedIn = false;
    }
}
