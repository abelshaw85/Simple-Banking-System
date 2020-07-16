package banking;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BankingUI bankingUI = new BankingUI(scanner);
        bankingUI.start();
    }
}
