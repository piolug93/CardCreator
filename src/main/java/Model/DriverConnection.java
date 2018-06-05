package Model;

import Model.Exceptions.NullConection;
import java.sql.*;

public class DriverConnection {

    private static Connection conn;
    private static PreparedStatement allQuery;

    public static void connect(String basePath) throws SQLException {
        String url = "jdbc:sqlite:"+basePath;
        conn = DriverManager.getConnection(url);
    }

    public static Connection getConnection() {
        if(conn != null)
            return conn;
        else
           throw new NullConection();
    }

    public static void update(String key, String value) throws SQLException {
        //set parameters query
        allQuery.setString(1, value);
        allQuery.setString(2, key);
        allQuery.addBatch();
    }

    public static void prepareStatment(String table) throws SQLException{
        String sql = "UPDATE " + table + " SET value = ? "
                + "WHERE key = ?";
        allQuery = conn.prepareStatement(sql);
    }

    public static void execute() throws SQLException {
        int[] errors = allQuery.executeBatch();
        for (int error : errors) {
            if (error == 0) {
                throw new BatchUpdateException("Bad key, update doesn't executed", errors);
            }
        }
    }
}