package banking;

public class Card {
    private String cardNumber;
    private String pin;
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
        return cardNumber;
    }

    public String getPin() {
        return pin;
    }

    public int getBalance() {
        return balance;
    }
}
