package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:";//C://sqlite/dbs/";
        if (args.length >= 2 && args[0].equals("-fileName")) {
            url += args[1];
            System.out.println(args[1]);
        } else {
            url += "Bank.db";
        }

        String sql = "CREATE TABLE IF NOT EXISTS card (\n"
                + "     id INTEGER PRIMARY KEY,\n" // autimaticly create new id
                + "     number TEXT,\n" // don't write VARCHAR its mistake!
                + "     pin TEXT,\n"
                + "     balance INTEGER DEFAULT 0"
                + ");";

        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
//            stmt.addBatch(sql2);
//            stmt.executeBatch();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

//        try (Connection con = dataSource.getConnection()) {
//            if (con != null) {
//                DatabaseMetaData meta = con.getMetaData();
//                System.out.println("The driver name is " + meta.getDriverName());
//                System.out.println("A new database has been created.");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

        Scanner scanner = new Scanner(System.in);
        BankingUI bankingUI = new BankingUI(scanner, url);
        bankingUI.start();
    }
}



