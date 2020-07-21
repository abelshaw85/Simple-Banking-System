package banking;

public class Account {
    private String accountNumber;
    private String cardNumber;

    public Account(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Account(String accountNumber, String cardNumber) {
        this(accountNumber);
        this.cardNumber = cardNumber;
    }

    public void addCard(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCard() {
        return this.cardNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}
