package com.pluralsight;

import java.io.*;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class HomeScreen {
    static Scanner scanner = new Scanner(System.in);
    static LocalDateTime now = LocalDateTime.now();
    static DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'|'HH:mm:ss");
    static String transactionType;
    static String today = now.format(fmt);
    static LocalDateTime dateObj = LocalDateTime.parse(today, fmt);
    static int year = dateObj.getYear();
    static Month month = dateObj.getMonth();
    static String name;

    static ArrayList<Transaction> transactions = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Welcome!");
        name = getInput(scanner, "Please enter name: ");
        loadTransactions();


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

    // pause between entries
    public static void pause() {
        getInput(scanner, "\nPress Enter to continue...");
    }

    //show home menu
    public static void showHomeMenu() {
        System.out.println("""
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

    //make deposit
    public static void makeDeposit() {
        LocalDateTime now = LocalDateTime.now();
        String today = now.format(fmt);

        String vendorName = getInput(scanner, "Who is the deposit from? ");
        String description = getInput(scanner, "Description: ");
        double depositAmount = Double.parseDouble(getInput(scanner, "How much would you like to deposit? $"));

        transactions.add(new Transaction(today, description, vendorName, depositAmount));
        createCSV();
        System.out.println("Deposit of $" + depositAmount + " completed successfully");
        pause();
    }

    //make payment
    private static void makePayment() {
        LocalDateTime now = LocalDateTime.now();
        String today = now.format(fmt);
        String vendorName = getInput(scanner, "Who would you like to pay? ");
        String description = getInput(scanner, "What is the item/service you are purchasing? ");
        double paymentAmount = Double.parseDouble(getInput(scanner, "How much is it? $"));
        transactionType = "Payment";
        paymentAmount = -Math.abs(paymentAmount);

        transactions.add(new Transaction(today, description, vendorName, paymentAmount));
        createCSV();
        System.out.println("Payment of " + String.format("($%.2f)", Math.abs(paymentAmount)) + " sent to " + vendorName + " for " + description + " successfully");
        pause();
        showHomeMenu();
    }

    //view ledger menu
    private static void viewLedger() {
        boolean isViewing = true;

        while (isViewing) {
            System.out.println("""
                    Ledger Menu:
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
                    //showHomeMenu();
                    isViewing = false;
                    break;
                default:
                    System.out.println("Error. Try again!");
            }
        }
    }

    //view transactions
    private static void displayTransactions(ArrayList<Transaction> transactions) {
        if (transactions.isEmpty()) {
            System.out.println("No transaction history");
            return;
        }
        transactions.sort((t1, t2) -> t2.getDateTime().compareTo(t1.getDateTime()));
        for (Transaction t : transactions) {
            System.out.println(t);
        }
        pause();
    }

    //view deposits
    private static void displayDeposits() {
        ArrayList<Transaction> deposits = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getAmount() > 0) {
                deposits.add(t);

            }
        }

        if (deposits.isEmpty()) {
            System.out.println("You have made no deposits");
        } else {
            deposits.sort((t1, t2) -> t2.getDateTime().compareTo(t1.getDateTime()));

            for (Transaction d : deposits) {
                System.out.println(d);
            }
        }
        pause();
    }

    //view deposits
    private static void displayPayments() {
        ArrayList<Transaction> payments = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getAmount() < 0) {
                payments.add(t);
            }
        }
        if (payments.isEmpty()) {
            System.out.println("No payments have been made");
        } else {
            payments.sort((t1, t2) -> t2.getDateTime().compareTo(t1.getDateTime()));

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
                6) Custom Search
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
            case "6":
                customSearch();
                break;
            case "0":
                viewLedger();
                break;
            default:
                System.out.println("Error. Try again!");
        }
    }

    private static void previousYear() {
        ArrayList<Transaction> prevYearList = new ArrayList<>();
        int previousYear = year - 1;

        for (Transaction t : transactions) {
            LocalDateTime dateTime = LocalDateTime.parse(t.getDateTime(), fmt);
            int transactionYear = dateTime.getYear();
            if (previousYear == transactionYear) {
                prevYearList.add(t);
            }
        }
        if (prevYearList.isEmpty()) {
            System.out.println("There is no history from " + previousYear);
        } else {
            prevYearList.sort((t1, t2) -> t2.getDateTime().compareTo(t1.getDateTime()));

            for (Transaction p : prevYearList) {
                System.out.println(p);
            }
        }
        pause();
    }

    private static void yearToDate() {
        ArrayList<Transaction> yearToDateList = new ArrayList<>();
        for (Transaction t : transactions) {
            LocalDateTime dateTime = LocalDateTime.parse(t.getDateTime(), fmt);
            int transactionYear = dateTime.getYear();
            if (year == transactionYear) {
                yearToDateList.add(t);
            }
        }
        if (yearToDateList.isEmpty()) {
            System.out.println("There is no history from " + year);
        } else {
            yearToDateList.sort((t1, t2) -> t2.getDateTime().compareTo(t1.getDateTime()));

            for (Transaction y : yearToDateList) {
                System.out.println(y);
            }
        }
        pause();
    }

    private static void previousMonth() {
        Month previousMonth = month.minus(1);
        int previousYear = year;
        if (month == Month.JANUARY) {
            previousYear = year - 1;
        }

        ArrayList<Transaction> prevMonthList = new ArrayList<>();

        for (Transaction t : transactions) {
            LocalDateTime dateTime = LocalDateTime.parse(t.getDateTime(), fmt);
            Month transactionMonth = dateTime.getMonth();
            if (previousMonth.equals(transactionMonth) && dateTime.getYear() == previousYear) {
                prevMonthList.add(t);
            }
        }
        if (prevMonthList.isEmpty()) {
            System.out.println("There is no transaction history from " + previousMonth);
        } else {
            prevMonthList.sort((t1, t2) -> t2.getDateTime().compareTo(t1.getDateTime()));
            for (Transaction m : prevMonthList) {
                System.out.println(m);
            }
        }
        pause();
    }

    private static void monthToDate() {
        ArrayList<Transaction> monthToDateList = new ArrayList<>();
        for (Transaction t : transactions) {
            LocalDateTime dateTime = LocalDateTime.parse(t.getDateTime(), fmt);
            Month transactionMonth = dateTime.getMonth();
            if (month.equals(transactionMonth)) {
                monthToDateList.add(t);
            }
        }
        if (monthToDateList.isEmpty()) {
            System.out.println("There is no transaction history from " + month);
        } else {
            monthToDateList.sort((t1, t2) -> t2.getDateTime().compareTo(t1.getDateTime()));

            for (Transaction m : monthToDateList) {
                System.out.println(m);
            }
        }
        pause();
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
            vendorList.sort((t1, t2) -> t2.getDateTime().compareTo(t1.getDateTime()));

            for (Transaction v : vendorList) {
                System.out.println(v);
            }
        }
        pause();
    }

    /**
     * Collects user inputs for custom search. Uses inputs with filteredSearch method to find matches.
     */
    public static void customSearch() {
        String startDateSearch = getInput(scanner, "Start date (yyyy-mm-dd): ");
        String endDateSearch = getInput(scanner, "End date (yyyy-mm-dd): ");
        String descriptionSearch = getInput(scanner, "Description: ");
        String vendorSearch = getInput(scanner, "Vendor: ");
        String amountSearch = getInput(scanner, "Amount: ");
        double amountInput = amountSearch.isEmpty() ? 0 : Double.parseDouble(amountSearch);

        ArrayList<Transaction> results = filteredSearch(startDateSearch, endDateSearch, descriptionSearch, vendorSearch, amountInput);
        displayTransactions(results);
    }

    /**
     * Adds time to date so time input not needed. Checks each field for a match, adds to list, then returns and prints list.
     * @param startDateSearch Field used to search transactions for Start Date user input
     * @param endDateSearch Field used to search transactions for End Date user input
     * @param description Field used to search transactions for Description user input
     * @param vendor Field used to search transactions for Vendor user input
     * @param amount Field used to search transactions for Amount
     * @return Returns list of all transactions that match all fields entered
     */

    public static ArrayList<Transaction> filteredSearch(String startDateSearch, String endDateSearch, String description, String vendor, double amount) {
        ArrayList<Transaction> filteredList = new ArrayList<>();

        LocalDateTime startDate = null;
        LocalDateTime endDate = null;

        try {
            if (!startDateSearch.isEmpty()) {
                if (startDateSearch.length() == 10) {
                    startDateSearch += "|00:00:00";
                }
                startDate = LocalDateTime.parse(startDateSearch, fmt);
            }
            if (!endDateSearch.isEmpty()) {
                if (endDateSearch.length() == 10) {
                    endDateSearch += "|23:59:59";
                }
                endDate = LocalDateTime.parse(endDateSearch, fmt);
            }
        } catch (Exception e) {
            System.out.println("Invalid date");
            return filteredList;
        }


        for (Transaction t : transactions) {
            LocalDateTime transactionDate = LocalDateTime.parse(t.getDateTime(), fmt);

            boolean dateMatch = (startDate == null || !transactionDate.isBefore(startDate)) && (endDate == null || !transactionDate.isAfter(endDate));
            boolean vendorMatch = vendor.isEmpty() || t.getVendorName().equalsIgnoreCase(vendor);
            boolean descriptionMatch = description.isEmpty() || t.getDescription().toLowerCase().contains(description.toLowerCase());
            boolean amountMatch = (amount == 0 || Math.abs(t.getAmount() - amount) < 0.01);

            if (dateMatch && vendorMatch && descriptionMatch && amountMatch) {
                filteredList.add(t);
            }
        }
        return filteredList;
    }


    /**
     * Creates Transaction CSV file using arrayList of transactions
     */
    private static void createCSV() {
        try (BufferedWriter buffWriter = new BufferedWriter(new FileWriter("transactions.csv", false))) {
            buffWriter.write(name + "'s transaction history");
            buffWriter.newLine();
            buffWriter.write("Date|Time|Description|Vendor|Amount");
            buffWriter.newLine();

            transactions.sort((t1, t2) -> t2.getDateTime().compareTo(t1.getDateTime()));

            for (Transaction t : transactions) {
                buffWriter.write(t.toCSV());
                buffWriter.newLine();
            }
            System.out.println("Please view transactions.csv for all transaction history");
        } catch (IOException e) {
            System.out.println("File could not be created");
        }
    }

    /**
     * Looks for previous Transactions.csv file and adds it to the arrayList to continue adding new ones
     */
    public static void loadTransactions() {
        File file = new File("transactions.csv");
        if (!file.exists()) {
            System.out.println("No previous transactions found");
            pause();
            return;
        }
        try (BufferedReader buffReader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = buffReader.readLine()) != null) {
                if ((line.contains("transaction history") || line.contains("Date|Time|Description|Vendor|Amount"))) {
                    continue;
                }

                String[] parts = line.split("\\|");

                if (parts.length < 5) {
                    continue;
                }

                String dateTime = parts[0].trim() + "|" + parts[1].trim();
                String description = parts[2].trim();
                String vendor = parts[3].trim();
                double amount = Double.parseDouble(parts[4].trim());

                transactions.add(new Transaction(dateTime, description, vendor, amount));
            }
            transactions.sort((t1, t2) -> t2.getDateTime().compareTo(t1.getDateTime()));
            System.out.println("Transactions loaded");

        } catch (IOException e) {
            System.out.println("Error reading file");
        } catch (NumberFormatException e) {
            System.out.println("Error getting numbers in file");
        }
    }
}
