package banking;

import java.util.Random;

public class AccountGenerator {
    private AccountGenerator() { }

    public static String generateAccountNumber() {
        Random random = new Random();
        String accountNumber = String.valueOf(random.nextInt(1_000_000_000)); //random acc between 0 and 999999999
        while (accountNumber.length() < 9) {
            accountNumber = "0" + accountNumber;
        }
        return accountNumber;
    }
}
