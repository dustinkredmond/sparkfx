package com.dustinredmond.apifx.persistence;

/*
 *  Copyright 2020  Dustin K. Redmond
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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
