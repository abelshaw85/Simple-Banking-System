package banking;

import java.util.Random;

public class CardGenerator {
    final private static int PIN_LENGTH = 4;

    private CardGenerator() { }

    public static Card generateCard(Bank bank, Account account) {
        String cardNumber = bank.getIssuerIdentificationNumber() +
                account.getAccountNumber() +
                generateChecksum();
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

    private static int generateChecksum() {
        Random random = new Random();
        return random.nextInt(10);
    }
}
