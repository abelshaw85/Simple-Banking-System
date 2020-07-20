package banking;

import java.util.Scanner;

public class BankingUI {
    Scanner scanner;
    Bank bank;

    public BankingUI(Scanner scanner, String dbUrl) {
        this.scanner = scanner;
        this.bank = new Bank("400000", dbUrl);
    }

    public void start() {
        loop: while (true) {
            System.out.println("1. Create an account\n" +
                    "2. Log into account\n" +
                    "0. Exit");
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    generateAccount();
                    break;
                case "2":
                    if (!login()) {
                        break loop;
                    }
                    break;
                case "0":
                    break loop;
            }
        }
        System.out.println("\nBye!");
    }

    public void generateAccount() {
        String accNumber = AccountGenerator.generateAccountNumber();
        //keep generating new card numbers until the number is unique
        while (this.bank.findAccount(accNumber) != null) {
            accNumber = AccountGenerator.generateAccountNumber();
        }
        Account newAccount = new Account(accNumber);
        this.bank.addAccount(newAccount);
        Card newCard = CardGenerator.generateCard(bank, newAccount);
        this.bank.addCard(newAccount.getAccountNumber(), newCard);
        System.out.println(String.format("\nYour card has been created\n" +
                "Your card number:\n%s\n" +
                "Your card PIN:\n%s\n",
                newCard.getCardNumber(), newCard.getPin()));
    }

    public boolean login() {
        System.out.println("Enter your card number:");
        String cardNumber = scanner.nextLine();
        System.out.println("Enter your PIN:");
        String pinNumber = scanner.nextLine();
        Card card = this.bank.verifyDetails(cardNumber, pinNumber);
        if (card != null) {
            System.out.println("\nYou have successfully logged in!\n");
            loop: while (true) {
                System.out.println("1. Balance\n" +
                        "2. Log out\n" +
                        "0. Exit");
                String input = scanner.nextLine();
                switch (input) {
                    case "1":
                        System.out.println("\nBalance: " + card.getBalance() + "\n");
                        break;
                    case "2":
                        System.out.println("You have successfully logged out!\n");
                        break loop;
                    case "0":
                        return false;
                    default:
                        System.out.println("Invalid command");
                        break;
                }
            }
        } else {
            System.out.println("Wrong card number or PIN!\n");
        }
        return true;
    }
}
