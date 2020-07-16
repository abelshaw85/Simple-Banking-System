package banking;

import java.util.Arrays;
import java.util.Random;

public class CardGenerator {
    final private static int PIN_LENGTH = 4;

    private CardGenerator() { }

    public static Card generateCard(Bank bank, Account account) {
        String cardNumber = bank.getIssuerIdentificationNumber() +
                account.getAccountNumber();
        cardNumber += generateChecksum(cardNumber);

        String pin = generatePin();
        return new Card(cardNumber, pin);
    }

    private static String generatePin() {
        Random random = new Random();
        int pinValue = random.nextInt(10000); //Generate number between 0 and 9999
        String pin = String.valueOf(pinValue);
        //pad beginning of pin with 0's if fewer than 4 digits
        while (pin.length() < PIN_LENGTH) {
            pin = "0" + pin;
        }
        return pin;
    }

    private static int generateChecksum(String cardNumber) {
        int sum = 0;
        for (int i = 0; i < cardNumber.length(); i++) {
            int numAtIndex = Integer.parseInt(String.valueOf(cardNumber.charAt(i)));
            if ((i + 1) % 2 != 0) {
                numAtIndex *= 2;
                if (numAtIndex > 9) {
                    numAtIndex -= 9;
                }
            }
            sum += numAtIndex;
        }
        int checksum = 10 - (sum % 10);
        return checksum == 10 ? 0 : checksum; //if 10, return 0
    }
}
