package application.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import application.model.Bank;

public class BankDb {
	// make this private after testing.
	public static List<Bank> defaultBanks = List.of(
			new Bank("400000", "Green Bank"),
			new Bank("599999", "Allstar Bank"));
    private static final String createBankSql = "CREATE TABLE bank ("
	    + "iin VARCHAR(6) NOT NULL, "
	    + "name VARCHAR(30) NOT NULL, "
	    + "PRIMARY KEY(iin)"
	    + ")";
    private static final String insertBankSql = "INSERT INTO bank(iin, name) VALUES(?, ?)";
    
    private static PreparedStatement preparedStmt;
    private static Statement stmt;
    private static ResultSet rs = null;
    
    static {
    	stmt = DbManager.getstatement();
    }
    
    public static void initialise() {
    	createBankTable();
    }
    
    private static void createBankTable() {
    	try {
            System.out.println(">> Creating bank table...");
            //stmt = DbManager.getstatement();
            stmt.execute(createBankSql);
            System.out.println(">> Bank table created!");
            insertBanks();
         } catch (SQLException ex) {
         	// Derby error codes: http://db.apache.org/derby/docs/10.8/ref/rrefexcept71493.html
             if(ex.getSQLState().equals("X0Y32")) {
             	System.out.println("Table already exists, bypassing creation...");
                return;
             }
             ex.printStackTrace();
         }
    }
    
    private static void insertBanks() {
    	System.out.println(">> Inserting Bank data...");
        try {
        	for (Bank bank: defaultBanks) {
        		preparedStmt = DbManager.getPreparedstatement(insertBankSql);
    			preparedStmt.setString(1, bank.getIin());
    	        preparedStmt.setString(2, bank.getName());
    	        preparedStmt.executeUpdate();
        	}
	        System.out.println(">> Bank data inserted!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    public static void printBanks() {
    	System.out.println("Banks:");
        try {
			rs = stmt.executeQuery("SELECT * FROM bank");
			while (rs.next()) {
	           System.out.printf("%d. %s\n", 
	         		  rs.getInt("iin"),
	         		  rs.getString("name"));
		    }
		} catch (SQLException e) {
			e.printStackTrace();
		}
        System.out.println("");
    }
    
    public static List<Bank> getBanks() {
    	List<Bank> banks = new ArrayList<Bank>();
    	try {
			rs = stmt.executeQuery("SELECT * FROM bank");
			while (rs.next()) {
				banks.add(new Bank(rs.getString("iin"), rs.getString("name")));
		    }
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return banks;
    }
    

}
