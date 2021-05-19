package application.logic;

public class ChecksumManager {
	
    public static boolean verifyChecksum(String cardNumber) {
        int givenChecksum = Character.getNumericValue(cardNumber.charAt(cardNumber.length() - 1));
        int actualChecksum = generateChecksum(cardNumber.substring(0, cardNumber.length() - 1)); // Removes last digit
        return givenChecksum == actualChecksum;
    }

    // Generate checksum using luhn's algorithm
    public static int generateChecksum(String cardNumber) {
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
        return checksum == 10 ? 0 : checksum; // If 10, return 0
    }
}
