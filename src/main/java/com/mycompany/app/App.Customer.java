public class Customer {
        private String customerId;
        private String firstName;
        private String lastName;
        private String address;
        private List<Account> accounts;
        

        public Customer(String customerId, String firstName, String lastName, String address) {
            this.customerId = customerId;
            this.firstName = firstName;
            this.lastName = lastName;
            this.address = address;
            this.accounts = new ArrayList<>();
        }

        public void addAccount(Account account) {
            this.accounts.add(account);
        }

        public List<Account> getAccounts() {
            return accounts;
        }
}
