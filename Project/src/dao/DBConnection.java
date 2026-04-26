package dao;
import java.sql.*;
public class DBConnection {
    private static final String URL  = System.getenv().getOrDefault("FMS_DB_URL", "jdbc:mysql://localhost:3306/faculty_system");
    private static final String USER = System.getenv().getOrDefault("FMS_DB_USER", "root");
    private static final String PASS = System.getenv().getOrDefault("FMS_DB_PASS", "nipun2501");
    private static Connection connection = null;
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASS);
                System.out.println("DB connected.");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL Driver not found. Add mysql-connector-j.jar");
        } catch (SQLException e) {
            System.out.println("DB connection failed: " + e.getMessage());
        }
        return connection;
    }
    public static void closeConnection() {
        try { if (connection != null && !connection.isClosed()) connection.close(); }
        catch (SQLException e) { e.printStackTrace(); }
    }
}
