package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bank {
    private Map<String, Account> accounts;
    private String issuerIdentificationNumber;
    private final int IIN_LENGTH = 6;
    private final int ACCOUNT_NUMBER_LENGTH = 9;
    private Database db;

    public Bank(String issuerIdentificationNumber, String dbUrl) {
        this.issuerIdentificationNumber = issuerIdentificationNumber;
        this.accounts = new HashMap();

        try {
            this.db = new Database(dbUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            this.db.submitSQL(List.of(
                    "CREATE TABLE IF NOT EXISTS card (\n" +
                            "id INTEGER PRIMARY KEY,\n" +
                            "number TEXT,\n" +
                            "pin TEXT,\n" +
                            "balance INTEGER DEFAULT 0" +
                            ");",
                    "CREATE TABLE IF NOT EXISTS account (\n" +
                            "id INTEGER PRIMARY KEY,\n" +
                            "number TEXT NOT NULL UNIQUE,\n" +
                            "card_number TEXT\n" +
                            ");"
            ));
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        String sql = "CREATE TABLE IF NOT EXISTS card (\n" +
//                "id INTEGER PRIMARY KEY,\n" +
//                "number TEXT,\n" +
//                "pin TEXT,\n" +
//                "balance INTEGER DEFAULT 0" +
//                ");";
//        try {
//            db.submitSQL(sql);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

    public void addAccount(Account newAccount) {
        try {
            this.db.submitSQL("INSERT INTO account (number) VALUES (\n" +
                            newAccount.getAccountNumber() + "\n" +
                            ");"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.accounts.put(newAccount.getAccountNumber(), newAccount);
    }

    public void addCard(String accNumber, Card card) {
        Account accountToAddCardTo = findAccount(accNumber);
        if (accountToAddCardTo == null) {
            System.out.println("Account not in database.");
        } else {
            try {
                this.db.submitSQL("INSERT INTO card (number, pin, balance) VALUES (\n" +
                                card.getCardNumber() + ",\n" +
                                card.getPin() + ",\n" +
                                "0\n" +
                                ");"
                );
                accountToAddCardTo.addCard(card.getCardNumber());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Account findAccount(String accountNumber) {
        try {
            ResultSet rs = this.db.select("SELECT * FROM account WHERE number='" + accountNumber + "';");
            if (!rs.next()) {
                return null;
            }
            return new Account(rs.getString("number"), rs.getString("card_number"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Card findCardByCardNumber(String cardNumber) {
//        if (cardNumber.length() == IIN_LENGTH + ACCOUNT_NUMBER_LENGTH + 1) {
//            String cardIIN = cardNumber.substring(0, IIN_LENGTH);
//            String accNumber = cardNumber.substring(IIN_LENGTH, cardNumber.length() - 1);
//            if (!this.issuerIdentificationNumber.equals(cardIIN)) {
//                System.out.println("Card does not belong to this bank.");
//                System.out.println(this.issuerIdentificationNumber + " " + cardIIN);
//                return null;
//            }
//            Account account = findAccount(accNumber);
//            if (account == null) {
//                System.out.println("Account not in database.");
//                return null;
//            }
//            Card card = account.getCard();
//            if (card != null && card.getCardNumber().equals(cardNumber)) {
//                return card;
//            }
//        }
        String sql = "SELECT * FROM card WHERE number='" + cardNumber + "';";
        try {
            ResultSet rs = this.db.select(sql);
            if (!rs.next()) {
                return null;
            }
            return new Card(rs.getString("number"), rs.getString("pin"), rs.getInt("balance"));
        } catch (SQLException e) {
            e.printStackTrace();
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
