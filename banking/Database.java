package banking;

import java.sql.*;

import java.util.List;

public class Database {
    final private String url;
    protected Connection conn;

    public Database(String url) throws SQLException {
        this.url = url;
        this.conn = DriverManager.getConnection(url);
    }

    //execute a single query
    public void submitSQL(String sqlQuery) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute(sqlQuery);
    }

    //execute a list of queries
    public void submitSQL(List<String> sqlQueries) throws SQLException {
        Statement stmt = conn.createStatement();
        for (String sqlQuery : sqlQueries) {
            stmt.addBatch(sqlQuery);
        }
        stmt.executeBatch();
    }

    public int count(String sql) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        rs.next();
        return rs.getInt(1);
    }

    public ResultSet select(String sql) throws SQLException {
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(sql);
    }
}
