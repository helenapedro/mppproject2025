package edu.miu.eems.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.InputStream;
import java.util.Properties; // Import Properties

public class DB {
    private static final String URL, USER, PASS;

    static {
        Properties props = new Properties();
        try (InputStream input = DB.class.getClassLoader().getResourceAsStream("config.properties")) {

            if (input == null) {
                System.err.println("Sorry, unable to find config.properties");
                URL = null;
                USER = null;
                PASS = null;
            } else {
                props.load(input);

                URL  = props.getProperty("DB_URL");
                USER = props.getProperty("DB_USER");
                PASS = props.getProperty("DB_PASS");
            }

        } catch (Exception ex) {
            System.err.println("Error loading config properties: " + ex.getMessage());
            throw new RuntimeException("Error loading config properties", ex);
        }
    }

    private DB() {}

    public static Connection getConnection() throws SQLException {
        if (URL == null || USER == null || PASS == null) {
            throw new SQLException("Database configuration is missing or failed to load.");
        }
        return DriverManager.getConnection(URL, USER, PASS);
    }
}