package banking;

public class Account {
    private String accountNumber;
    private Card card;

    public Account(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void addCard(Card card) {
        this.card = card;
    }

    public Card getCard() {
        return this.card;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}
