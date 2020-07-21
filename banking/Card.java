package banking;

public class Card {
    final private String cardNumber;
    final private String pin;
    private int balance = 0;

    public Card(String cardNumber, String pin) {
        this.cardNumber = cardNumber;
        this.pin = pin;
    }

    public Card(String cardNumber, String pin, int balance) {
        this(cardNumber, pin);
        this.balance = balance;
    }

    public String getCardNumber() {
        return this.cardNumber;
    }

    public String getPin() {
        return this.pin;
    }

    public int getBalance() {
        return this.balance;
    }
}
