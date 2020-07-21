package banking;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:";
        if (args.length >= 2 && args[0].equals("-fileName")) {
            url += args[1];
            System.out.println(args[1]);
        } else {
            url += "C://sqlite/dbs/Bank.db";
        }

        Scanner scanner = new Scanner(System.in);
        BankingUI bankingUI = new BankingUI(scanner, url);
        bankingUI.start();
    }
}



