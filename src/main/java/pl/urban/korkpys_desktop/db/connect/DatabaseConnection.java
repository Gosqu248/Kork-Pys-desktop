package pl.urban.korkpys_desktop.db.connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://195.150.230.208:5432/2023_urban_grzegorz";
    private static final String USER = "2023_urban_grzegorz";
    private static final String PASSWORD = "35240";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}