package Model;

import Model.Exceptions.NullConection;
import java.sql.*;

public class DriverConnection {

    private static Connection conn;
    private static PreparedStatement allQuery;

    public static void connect(String basePath) {
        try {
            // db path
            String url = "jdbc:sqlite:"+basePath;

            // create a connection to the database
            conn = DriverManager.getConnection(url);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Connection getConnection() {
        if(conn != null)
            return conn;
        else
           throw new NullConection();
    }

    public static void update(String table, String key, String value) {
        String sql = "UPDATE " + table + " SET value = ? "
                + "WHERE key = ?";

        try {
            allQuery = conn.prepareStatement(sql);

            //set parameters query
            allQuery.setString(1, value);
            allQuery.setString(2, key);
            allQuery.addBatch();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void execute() {
        try {
            allQuery.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}