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
        this.bank.closeDB();
        System.out.println("\nBye!");
    }

    public void generateAccount() {
        String accNumber = AccountGenerator.generateAccountNumber();
        //keep generating new card numbers until the number is unique
        while (this.bank.findAccount(accNumber) != null) {
            accNumber = AccountGenerator.generateAccountNumber();
        }
        Card newCard = CardGenerator.generateCard(bank, accNumber);
        this.bank.addCard(newCard);
        this.bank.addAccount(new Account(accNumber, newCard.getCardNumber()));
        System.out.println(String.format("\nYour card has been created\n" +
                "Your card number:\n%s\n" +
                "Your card PIN:\n%s\n",
                newCard.getCardNumber(), newCard.getPin()));
    }

    public boolean login() {
        System.out.println("\nEnter your card number:");
        String cardNumber = scanner.nextLine();
        System.out.println("Enter your PIN:");
        String pinNumber = scanner.nextLine();
        Card card = this.bank.verifyDetails(cardNumber, pinNumber);
        if (card != null) {
            System.out.println("\nYou have successfully logged in!\n");
            loop: while (true) {
                System.out.println("1. Balance\n" +
                        "2. Add income\n" +
                        "3. Do transfer\n" +
                        "4. Close account\n" +
                        "5. Log out\n" +
                        "0. Exit");
                String input = scanner.nextLine();
                switch (input) {
                    case "1":
                        System.out.println("\nBalance: " + this.bank.getBalance(card) + "\n");
                        break;
                    case "2":
                        addIncome(card);
                        break;
                    case "3":
                        doTransfer(card);
                        break;
                    case "4":
                        closeAccount(card);
                        break loop;
                    case "5":
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

    public void addIncome(Card card) {
        System.out.println("\nEnter income:");
        int income = Integer.valueOf(scanner.nextLine());
        this.bank.addIncomeToCard(card, income);
        System.out.println("Income was added!\n");
    }

    public void doTransfer(Card sender) {
        System.out.println("\nTransfer");
        System.out.println("Enter card number:");
        String cardNumber = scanner.nextLine();
        if (CardGenerator.verifyChecksum(cardNumber)) {
            Card receiver = this.bank.findCardByCardNumber(cardNumber);
            if (receiver != null) {
                System.out.println("Enter how much money you want to transfer:");
                int amount = Integer.valueOf(scanner.nextLine());
                if (this.bank.getBalance(sender) >= amount) {
                    this.bank.transfer(sender, receiver, amount);
                    System.out.println("Success!\n");
                } else {
                    System.out.println("Not enough money!\n");
                    return;
                }
            } else {
                System.out.println("Such a card does not exist.\n");
            }
        } else {
            System.out.println("Probably you made mistake in the card number. Please try again!\n");
        }

    }

    public void closeAccount(Card card) {
        this.bank.closeAccount(card);
        System.out.println("\nThe account has been closed!");
    }
}
