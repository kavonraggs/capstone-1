package com.pluralsight;

public class Transaction {
    public String getDateTime() {
        return dateTime;
    }

    private String dateTime;

    public String getTransactionType() {
        return transactionType;
    }

    private String transactionType;
    String name;
    double amount;

    public String getVendorName() {
        return vendorName;
    }

    String vendorName;
    String description;

    public Transaction(String dateTime, String transactionType, String vendorName, String description, double amount){
        this.dateTime = dateTime;
        this.transactionType = transactionType;
        this.vendorName = vendorName;
        this.description = description;
        this.amount = amount;
    }

    public Transaction(String dateTime, String transactionType, double amount){
        this.dateTime = dateTime;
        this.transactionType = transactionType;
        this.amount = amount;
        this.vendorName = "N/A";
        this.description = "N/A";
    }

    @Override
    public String toString(){
        return dateTime + "|" + transactionType + "|" + vendorName + "|" + description + "|" + amount;
    }

    public String toCSV() {
        return dateTime + "|" + transactionType + "|" + vendorName + "|" + description + "|" + amount;
    }
}
