package com.pluralsight;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class HomeScreen {
    static Scanner scanner = new Scanner(System.in);
    static LocalDateTime now = LocalDateTime.now();
    static DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'|'HH:mm:ss");
    static String transactionType;
    static String record;
    static String today = now.format(fmt);
    static String name;

    public static void main(String[] args) {
        name = getInput(scanner, "Please enter name: ");

        try (BufferedWriter buffWriter = new BufferedWriter(new FileWriter("transactions.csv", true))) {
            buffWriter.write(name + "'s Transaction History");
            buffWriter.newLine();
        } catch (IOException e){
            System.out.println("File could not be created");
        }

        showMenu();
        String input = getInput(scanner, "Enter the corresponding letter, then press enter: ");

        switch (input.toUpperCase()){
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
                break;
            default:
                System.out.println("Error. Try again!");

        }

    }


    public static void showMenu(){
        System.out.println("""
                Welcome!
                Select from the options below:
                D) Add Deposit
                P) Make a Payment
                L) Ledger
                X) Exit 
                """);
    }
    public static String getInput(Scanner scanner, String prompt){
        System.out.print(prompt);
        String input = scanner.nextLine();
        return input;
    }

    public static void makeDeposit(){;
        int depositAmount = Integer.parseInt(getInput(scanner,"How much would you like to deposit? $"));
        transactionType = "Deposit";
        record = today + '|' + transactionType + '|' + depositAmount;

        try (BufferedWriter buffWriter = new BufferedWriter(new FileWriter("transactions.csv", true))) {
            buffWriter.write(record);
            buffWriter.newLine();
        } catch (IOException e){
            System.out.println("File could not be created");
        }

    }


    private static void makePayment() {
    }

    private static void viewLedger() {
    }



}
