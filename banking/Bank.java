package banking;

import java.util.HashMap;
import java.util.Map;

public class Bank {
    private Map<String, Account> accounts;
    private String issuerIdentificationNumber;
    private final int IIN_LENGTH = 6;
    private final int ACCOUNT_NUMBER_LENGTH = 9;

    public Bank(String issuerIdentificationNumber) {
        this.issuerIdentificationNumber = issuerIdentificationNumber;
        this.accounts = new HashMap();
    }

    public void addAccount(Account newAccount) {
        this.accounts.put(newAccount.getAccountNumber(), newAccount);
    }

    public void addCard(String accNumber, Card card) {
        Account accountToAddCardTo = findAccount(accNumber);
        if (accountToAddCardTo == null) {
            System.out.println("Account not in database.");
        } else {
            accountToAddCardTo.addCard(card);
        }
    }

    public Account findAccount(String accountNumber) {
        return this.accounts.get(accountNumber);
    }

    private Card findCardByCardNumber(String cardNumber) {
        if (cardNumber.length() == IIN_LENGTH + ACCOUNT_NUMBER_LENGTH + 1) {
            String cardIIN = cardNumber.substring(0, IIN_LENGTH);
            String accNumber = cardNumber.substring(IIN_LENGTH, cardNumber.length() - 1);
            if (!this.issuerIdentificationNumber.equals(cardIIN)) {
                System.out.println("Card does not belong to this bank.");
                System.out.println(this.issuerIdentificationNumber + " " + cardIIN);
                return null;
            }
            Account account = findAccount(accNumber);
            if (account == null) {
                System.out.println("Account not in database.");
                return null;
            }
            Card card = account.getCard();
            if (card != null && card.getCardNumber().equals(cardNumber)) {
                return card;
            }
        }
        return null;
    }

    public Card verifyDetails(String cardNumber, String pin) {
        Card card = findCardByCardNumber(cardNumber);
        return card != null && card.getPin().equals(pin) ? card : null;
    }

    public String getIssuerIdentificationNumber() {
        return this.issuerIdentificationNumber;
    }
}
