package banking;

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
    }

    public void addAccount(Account newAccount) {
        try {
            this.db.submitSQL("INSERT INTO account (number) VALUES (\n" +
                    "'" + newAccount.getAccountNumber() + "'\n" +
                    ");"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.accounts.put(newAccount.getAccountNumber(), newAccount);
    }

    public void addCard(Card card) {
        try {
            this.db.submitSQL("INSERT INTO card (number, pin, balance) VALUES (\n" +
                    "'" + card.getCardNumber() + "',\n" +
                    "'" + card.getPin() + "',\n" +
                    "0\n" +
                    ");"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Account findAccount(String accountNumber) {
        try {
            ResultSet rs = this.db.select("SELECT * FROM account WHERE number='" + accountNumber + "';");
            if (rs.next()) {
                return new Account(rs.getString("number"), rs.getString("card_number"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Card findCardByCardNumber(String cardNumber) {
        String sql = "SELECT * FROM card WHERE number='" + cardNumber + "';";
        try {
            ResultSet rs = this.db.select(sql);
            if (rs.next()) {
                return new Card(rs.getString("number"), rs.getString("pin"), rs.getInt("balance"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void transfer(Card sender, Card receiver, int amount) {
        try {
            this.db.submitSQL(List.of(
                    "UPDATE card " +
                            "SET balance = balance - " + amount + "\n" +
                            "WHERE number='" + sender.getCardNumber() + "';",
                    "UPDATE card " +
                            "SET balance = balance + " + amount + "\n" +
                            "WHERE number='" + receiver.getCardNumber() + "';"
            ));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addIncomeToCard(Card card, int amount) {
        String sql = "UPDATE card\n" +
                "SET balance = balance + " + amount + "\n" +
                "WHERE number='" + card.getCardNumber() + "';";
        try {
            this.db.submitSQL(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getBalance(Card card) {
        String sql = "SELECT balance FROM card WHERE number='" + card.getCardNumber() + "';";
        try {
            ResultSet rs = this.db.select(sql);
            if (rs.next()) {
                return rs.getInt("balance");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Card not found");
        return -1;
    }

    public Card verifyDetails(String cardNumber, String pin) {
        Card card = findCardByCardNumber(cardNumber);
        if (card != null && card.getPin().equals(pin)) {
            return card;
        } else if (card == null) {
            System.out.println("Card not found.");
        } else {
            System.out.println("Pin incorrect.");
        }
        return null;
    }

    public void closeAccount(Card card) {
        try {
            this.db.submitSQL(
                    "DELETE FROM card " +
                            "WHERE number='" + card.getCardNumber() + "';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getIssuerIdentificationNumber() {
        return this.issuerIdentificationNumber;
    }

    public void closeDB() {
        this.db.close();
    }
}
