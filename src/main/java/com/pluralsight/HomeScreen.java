package com.pluralsight;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class HomeScreen {
    static Scanner scanner = new Scanner(System.in);
    static LocalDateTime now = LocalDateTime.now();
    static DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'|'HH:mm:ss");
    static String transactionType;
    static String record;
    static String today = now.format(fmt);
    static String name;

    static ArrayList<Transaction> transactions = new ArrayList<>();

    public static void main(String[] args) {
        name = getInput(scanner, "Please enter name: ");

        boolean isRunning = true;
        while (isRunning) {
            showHomeMenu();
            String input = getInput(scanner, "Enter the corresponding letter, then press enter: ");

            switch (input.toUpperCase()) {
                case "D":
                    makeDeposit();
                    break;
                case "P":
                    makePayment();
                    break;
                case "L":
                    viewLedger();
                    break;
                case "X":
                    System.out.println("Goodbye!");
                    createCSV();
                    System.out.println("-------------------------");
                    isRunning = false;
                    pause();
                    break;
                default:
                    System.out.println("Error. Try again!");

            }
        }
    }

    public static String pause(){
        return getInput(scanner, "\nPress Enter to continue...");
    }

    public static void showHomeMenu() {
        System.out.println("""
                Welcome!
                Select from the options below:
                D) Add Deposit
                P) Make a Payment
                L) Ledger
                X) Exit 
                """);
    }

    public static String getInput(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public static void makeDeposit() {
        LocalDateTime now = LocalDateTime.now();
        String today = now.format(fmt);

        double depositAmount = Double.parseDouble(getInput(scanner, "How much would you like to deposit? $"));
        transactionType = "Deposit";

        transactions.add(new Transaction(today, transactionType, depositAmount));
        record = today + "|" + transactionType + "|" + depositAmount;

        System.out.println("Deposit of $" + depositAmount + " completed successfully");
    }

    private static void makePayment() {
        LocalDateTime now = LocalDateTime.now();
        String today = now.format(fmt);
        String vendorName = getInput(scanner, "Who would you like to pay? ");
        String description = getInput(scanner, "What is the item/service you are purchasing? ");
        int paymentAmount = Integer.parseInt(getInput(scanner, "How much is it? $"));
        transactionType = "Payment";

        transactions.add(new Transaction(today, transactionType, vendorName, description, paymentAmount));
        System.out.println("Payment of $" + paymentAmount + " sent to " + vendorName + " for " + description + " successfully");
        showHomeMenu();
    }

    private static void viewLedger() {
        LocalDateTime now = LocalDateTime.now();
        String today = now.format(fmt);
        boolean isViewing = true;

        while(isViewing) {
            System.out.println("""
                           Ledger Menu
                    Select from the options below:
                    A) View All Transactions
                    D) View All Deposits
                    P) View All Payments
                    R) View Reports
                    H) Return to Home Screen
                    """);

            String input = getInput(scanner, "Enter the corresponding letter, then press enter:");

            switch (input.toUpperCase()) {
                case "A":
                    displayTransactions(transactions);
                    break;
                case "D":
                    displayDeposits();
                    break;
                case "P":
                    displayPayments();
                    break;
                case "R":
                    viewReports();
                    break;
                case "H":
                    showHomeMenu();
                    isViewing = false;
                    break;
                default:
                    System.out.println("Error. Try again!");
            }
        }
    }

    private static void displayTransactions(ArrayList<Transaction> transactions) {
        if (transactions.isEmpty()) {
            System.out.println("No transaction history");
            return;
        }
        for (Transaction t : transactions) {
            System.out.println(t);
        }
        pause();
    }

    private static void displayDeposits() {
        ArrayList<Transaction> deposits = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getTransactionType().equalsIgnoreCase("Deposit")) {
                deposits.add(t);

            }
        }
        if (deposits.isEmpty()) {
            System.out.println("You have made no deposits");
        } else {
            for (Transaction d : deposits) {
                System.out.println(d);
            }
        }
        pause();


    }

    private static void displayPayments() {
        ArrayList<Transaction> payments = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getTransactionType().equalsIgnoreCase("Payment")) {
                payments.add(t);
            }
        }
        if (payments.isEmpty()) {
            System.out.println("No payments have been made");
        } else {
            for (Transaction p : payments) {
                System.out.println(p);
            }
        }
        pause();
    }

    private static void viewReports() {
        System.out.println("""
                    Report Menu
                1) Month to Date
                2) Previous Month
                3) Year to Date
                4) Previous Year
                5) Search by Vendor
                0) Back
                """);

        String input = getInput(scanner, "Enter the corresponding number, then press enter: ");

        switch (input.toUpperCase()) {
            case "1":
                monthToDate();
                break;
            case "2":
                previousMonth();
                break;
            case "3":
                yearToDate();
                break;
            case "4":
                previousYear();
                break;
            case "5":
                searchByVendor();
                break;
            case "0":
                viewLedger();
                break;
            default:
                System.out.println("Error. Try again!");

        }


    }

    private static void previousYear() {
    }

    private static void yearToDate() {
    }

    private static void previousMonth() {
    }

    private static void monthToDate() {
    }

    private static void searchByVendor() {
        ArrayList<Transaction> vendorList = new ArrayList<>();
        String vendorSearch = getInput(scanner, "What is the name of the vendor you want to search for? ");
        for (Transaction t : transactions) {
            if (t.getVendorName() != null && t.getVendorName().equalsIgnoreCase(vendorSearch)) {
                vendorList.add(t);
            }
        }

        if (vendorList.isEmpty()) {
            System.out.println("There is no payment history with " + vendorSearch);
        } else {
            for (Transaction v : vendorList) {
                System.out.println(v);
            }
        }
    }



    private static void createCSV() {
        try (BufferedWriter buffWriter = new BufferedWriter(new FileWriter("transactions.csv"))) {
            buffWriter.write(name + "'s transaction history");
            buffWriter.newLine();

            for (Transaction t : transactions) {
                buffWriter.write(t.toCSV());
                buffWriter.newLine();
            }
            System.out.println("Please view transactions.csv for all transaction history");
        } catch (IOException e) {
            System.out.println("File could not be created");
        }

    }


}
