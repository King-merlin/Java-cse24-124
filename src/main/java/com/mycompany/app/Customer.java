package com.mycompany.app;
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

        //========= Getters and Setters=========
        public String getCustomerId() {
            return customerId;
        }
        public void setCustomerId(String customerId) { 
            this.customerId = customerId;
        }
        public String getFirstName() {
            return firstName; 
        }
        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }
        public String getLastName() {
            return lastName;
        }
        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
        public String getAddress() { 
            return address;
        }
        public void setAddress(String address) { 
            this.address = address; 
        }
    }

