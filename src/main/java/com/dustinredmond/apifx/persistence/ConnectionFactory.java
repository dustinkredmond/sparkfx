package com.dustinredmond.apifx.persistence;

import com.dustinredmond.apifx.util.Prefs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Factory class for getting a {@code java.sql.Connection} to the default, or optionally, user-defined
 * SQLite database.
 */
public class ConnectionFactory {

    static {
        // Load SQLite JDBC driver
        try { Class.forName("org.sqlite.JDBC"); } catch (ClassNotFoundException ignored) {}
    }

    /**
     * Gets a connection to the SQLite default or user defined SQLite database
     * @return {@code java.sql.Connection} to the SQLite database
     */
    protected Connection connect() {
        Connection conn;
        try {
            if (!DB_USER_PATH.isEmpty()) {
                conn = DriverManager.getConnection("jdbc:sqlite:"+DB_USER_PATH);
            } else {
                conn = DriverManager.getConnection(DB_URL);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to get local database connection.", e);
        }
        return conn;
    }

    private static final String DB_URL = "jdbc:sqlite:apifx.db";
    private static final String DB_USER_PATH = Prefs.get("dbPath", "");

}
