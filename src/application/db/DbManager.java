package application.db;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.derby.jdbc.EmbeddedDriver;

public class DbManager {
    private static Connection connection = null;
    private static Statement statement = null;
    
    private static String URL = "jdbc:derby:bankdb;create=true";
    private static String USERNAME = "admin";
    private static String PASSWORD = "pass123";
	
	public static void initialise() {
    	createDb();
    }
	
    private static void createDb() {
    	try {
    		System.out.println(">> Initialising Database...");
            Driver derbyEmbeddedDriver = new EmbeddedDriver();
            DriverManager.registerDriver(derbyEmbeddedDriver);
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            System.out.println(">> Database initialised!");

            connection.commit();
         } catch (SQLException ex) {
             ex.printStackTrace();
         }
    }
    
    public static Statement getstatement() {
    	if (statement == null) {
    		initialise();
    	}
    	return statement;
    }
    
    public static PreparedStatement getPreparedstatement(String sql) {
    	try {
    		if (connection == null) {
        		initialise();
        	}
			return connection.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
    }
    
    public static void shutdown() {
        try {
        	connection.commit();
        	connection.close();
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
         } catch (SQLException ex) {
            if (((ex.getErrorCode() == 50000) &&
               ("XJ015".equals(ex.getSQLState())))) {
                  System.out.println("Derby Database shut down successfully.");
            } else {
               System.err.println("Derby Database did not shut down correctly!");
               System.err.println(ex.getMessage());
            }
         }
    }
}
