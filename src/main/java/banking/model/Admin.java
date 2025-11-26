package banking.model;

public class Admin {
    private String username;
    private String password;
    private String fullName;
    private String createdDate;

    public Admin(String username, String password, String fullName, String createdDate) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.createdDate = createdDate;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public boolean checkPassword(String pw) {
        return password.equals(pw);
    }

    @Override
    public String toString() {
        return String.format("Admin: %s (%s) - Created: %s", username, fullName, createdDate);
    }
}