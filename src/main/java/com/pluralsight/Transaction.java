package com.pluralsight;

public class Transaction {


    private double amount;
    private String dateTime;
    private String vendorName;
    private String description;

    public Transaction(String dateTime, String description, String vendorName, double amount){
        this.dateTime = dateTime;
        //this.transactionType = transactionType;
        this.vendorName = vendorName;
        this.description = description;
        this.amount = amount;
    }

    public String getDateTime() {
        return dateTime;
    }

    public double getAmount() {
        return amount;
    }

    public String getVendorName() {
        return vendorName;
    }

    @Override
    public String toString(){
        return dateTime + "|" + description + "|" + vendorName + "| $" + amount;
    }

    public String toCSV() {
        return dateTime + "|" + description + "|" + vendorName + "|" + amount;
    }
}
