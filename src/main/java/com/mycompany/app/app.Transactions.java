package com.mycompany.app; 

    public class Transaction {
        private double amount;
        private String type;
        private Date date;

        public Transaction(double amount, String type) {
            this.amount = amount;
            this.type = type;
            this.date = new Date();
        }

        //===== Getters and Setters==========
        public double getAmount() { 
            return amount; 
        }
        public void setAmount(double amount) { 
            this.amount = amount; 
        }
        public String getType() { 
            return type;
        }
        public void setType(String type) { 
            this.type = type;
        }
        public Date getDate() { 
            return date; 
        }
        public void setDate(Date date) { 
            this.date = date; 
        }
    }
