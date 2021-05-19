package application.logic;

import java.util.Random;

public class PinGenerator {
	final private static int PIN_LENGTH = 4;
	
    public static String generatePin() {
        Random random = new Random();
        // Generate number between 0 and 9999
        int pinValue = random.nextInt(10000); 
        
        // Convert to String
        String pin = String.valueOf(pinValue);
        
        // Pad beginning of pin with 0's if fewer than 4 digits
        while (pin.length() < PIN_LENGTH) {
            pin = "0" + pin;
        }
        return pin;
    }
}
