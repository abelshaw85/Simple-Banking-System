package application.db;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import application.logic.AccountNumberGenerator;
import application.logic.ChecksumManager;
import application.logic.PinGenerator;
import application.model.Account;
import application.model.Bank;

public class AccountDb {
    private static final String createAccountSql = "CREATE TABLE account ("
		+ "account_number VARCHAR(12) NOT NULL, "
		+ "first_name VARCHAR(30) NOT NULL, "
		+ "last_name VARCHAR(30) NOT NULL, "
		+ "amount DECIMAL(6,2) DEFAULT 0.00, "
		+ "bank_iin VARCHAR(6), "
		+ "checksum INTEGER, "
		+ "pin VARCHAR(4), "
		+ "PRIMARY KEY(account_number, bank_iin), "
		+ "CONSTRAINT BANK_FK "
		+ "FOREIGN KEY (bank_iin) "
		+ "REFERENCES bank (iin)"
		+ ")";
    private static final String insertAccountSql = "INSERT INTO account(account_number, first_name, last_name, amount, bank_iin, checksum, pin) VALUES (?, ?, ?, ?, ?, ?, ?)";
    
    private static PreparedStatement preparedStmt;
    private static Statement stmt;
    private static ResultSet rs = null;
    
    static {
    	stmt = DbManager.getstatement();
    }
    
    public static void initialise() {
    	createAccountTable();
    }
    
    private static void createAccountTable() {
    	try {
            System.out.println(">> Creating account table...");
            stmt.execute(createAccountSql);
            System.out.println(">> Account table created!");
         } catch (SQLException ex) {
         	// Derby error codes: http://db.apache.org/derby/docs/10.8/ref/rrefexcept71493.html
             if(ex.getSQLState().equals("X0Y32")) {
             	System.out.println("Table already exists, bypassing creation...");
                return;
             }
             ex.printStackTrace();
         }
    }

    public static Account addAccount(Account account) {
    	System.out.println(">> Adding new account");
    	
    	int count = 1;
    	// Keep generating account numbers until one unique to this bank is found
    	while (count != 0) {
    		String accountNumber = AccountNumberGenerator.generateAccountNumber();
    		System.out.println(accountNumber);
        	account.setAccountNumber(accountNumber);
        	count = getAccountCount(account);
    	}
        try {
    		preparedStmt = DbManager.getPreparedstatement(insertAccountSql);
			preparedStmt.setString(1, account.getAccountNumber());
	        preparedStmt.setString(2, account.getFirstName());
	        preparedStmt.setString(3, account.getLastName());
	        preparedStmt.setBigDecimal(4, account.getAmount());
	        preparedStmt.setString(5, account.getBank().getIin());
	        String partialCardNumber = account.getBank().getIin() + account.getAccountNumber();
	        account.setChecksum(ChecksumManager.generateChecksum(partialCardNumber));
	        boolean checkSumValid = ChecksumManager.verifyChecksum(partialCardNumber + account.getChecksum());
	        if (checkSumValid) {
	        	System.out.println("Checksum OK!");
	        } else {
	        	System.out.println("Bad checksum!");
	        }
	        preparedStmt.setInt(6, account.getChecksum());
	        String pin = PinGenerator.generatePin();
	        account.setPin(pin);
	        preparedStmt.setString(7, account.getPin());
	        preparedStmt.executeUpdate();
	        System.out.println(">> Account data inserted!");
	        return account;
		} catch (SQLException e) {
			e.printStackTrace();
			return null; // Account not created
		}
    }
    
    private static int getAccountCount(Account account) {
    	try {
	    	String countSql = "SELECT COUNT(*) AS count FROM account WHERE account_number = ? AND bank_iin = ? ";
	    	preparedStmt = DbManager.getPreparedstatement(countSql);
	    	preparedStmt.setString(1, account.getAccountNumber());
	    	preparedStmt.setString(2, account.getBank().getIin());
	    	rs = preparedStmt.executeQuery();
	    	rs.next();
	    	int count = rs.getInt("count");
	    	return count;
    	} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
    }
    
    public static List<Account> getAccounts() {
    	List<Bank> banks = BankDb.getBanks();
    	List<Account> accounts = new ArrayList<Account>();
    	
    	try {
	    	String sql = "SELECT * from account";
	    	rs = stmt.executeQuery(sql);
	    	while (rs.next()) {
	    		String accountNumber = rs.getString("account_number");
	    		String firstName = rs.getString("first_name");
	    		String lastName = rs.getString("last_name");
	    		BigDecimal amount = rs.getBigDecimal("amount");
	    		String bankIin = rs.getString("bank_iin");
	    		Bank accBank = banks.stream().filter(bank -> bank.getIin().equals(bankIin)).findFirst().get();
	    		int checkSum = rs.getInt("checksum");
	    		String pin = rs.getString("pin");
	    		accounts.add(new Account(accountNumber, firstName, lastName, amount, accBank, checkSum, pin));
	    	}
	    	rs.next();

    	} catch (SQLException e) {
			e.printStackTrace();
		}
    	return accounts;
    	
    }
    
    public static boolean withdraw(Account account, BigDecimal amountToWithdraw) {
    	try {
    		String selectSql = "SELECT amount FROM account WHERE account_number = ? AND bank_iin = ?";
        	preparedStmt = DbManager.getPreparedstatement(selectSql);
        	preparedStmt.setString(1, account.getAccountNumber());
        	preparedStmt.setString(2, account.getBank().getIin());
			rs = preparedStmt.executeQuery();
			rs.next();
			BigDecimal amount = rs.getBigDecimal("amount");
			// If user has enough money, withdraw money
			if (amount.compareTo(amountToWithdraw) >= 0) {
				amount = amount.subtract(amountToWithdraw);
				String updateSql = "UPDATE account SET amount = ? WHERE account_number = ? AND bank_iin = ?";
				preparedStmt = DbManager.getPreparedstatement(updateSql);
				preparedStmt.setBigDecimal(1, amount);
				preparedStmt.setString(2, account.getAccountNumber());
	        	preparedStmt.setString(3, account.getBank().getIin());
	        	preparedStmt.execute();
	        	
	        	// Update Account object
				account.setAmount(amount);
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return false;
    }
    
    public static boolean deposit(Account account, BigDecimal amountToDeposit) {
    	printAccounts();
    	try {
    		String selectSql = "SELECT first_name, amount FROM account WHERE account_number = ? AND bank_iin = ?";
        	preparedStmt = DbManager.getPreparedstatement(selectSql);
        	preparedStmt.setString(1, account.getAccountNumber());
        	preparedStmt.setString(2, account.getBank().getIin());
			rs = preparedStmt.executeQuery();
			rs.next();
			String name = rs.getString("first_name");
			BigDecimal amount = rs.getBigDecimal("amount");
			System.out.println("name: " + name);
			System.out.println("amount:" + amount);

			amount = amount.add(amountToDeposit);
			System.out.println("amount added:" + amount);
			String updateSql = "UPDATE account SET amount = ? WHERE account_number = ? AND bank_iin = ?";
			preparedStmt = DbManager.getPreparedstatement(updateSql);
			preparedStmt.setBigDecimal(1, amount);
			preparedStmt.setString(2, account.getAccountNumber());
        	preparedStmt.setString(3, account.getBank().getIin());
        	preparedStmt.execute();
        	
        	// Update Account object
			account.setAmount(amount);
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return false;
    }
    
    public static void printAccounts() {
    	System.out.println("Accounts:");
        try {
			rs = stmt.executeQuery("SELECT * FROM account");
			while (rs.next()) {
	           System.out.printf("%d. %s %s [£%s] %d checksum=%d pin=%s", 
	         		  rs.getInt("account_number"),
	         		  rs.getString("first_name"),
	         		  rs.getString("last_name"),
	         		  rs.getBigDecimal("amount"),
	         		  rs.getInt("bank_iin"),
	         		  rs.getInt("checksum"),
	         		  rs.getInt("pin")
	           );
		    }
		} catch (SQLException e) {
			e.printStackTrace();
		}
        System.out.println("");
    }   

}
