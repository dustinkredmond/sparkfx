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

import com.dustinredmond.apifx.ui.custom.CustomAlert;
import javafx.application.Platform;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * A class, implementing {@code java.lang.Runnable} which
 * is responsible for creating the necessary application
 * database tables for the specified SQLite database.
 */
public class DatabaseBootstrap implements Runnable {

    @Override
    public void run() {
        createTablesIfNotExists();
    }

    private void createTablesIfNotExists() {
        try (Connection conn = new ConnectionFactory().connect();
             PreparedStatement ps = conn.prepareStatement(sqlRouteCreate);
             PreparedStatement ps2 = conn.prepareStatement(sqlGroovyCreate);
             PreparedStatement ps3 = conn.prepareStatement(sqlStartupCreate)) {
            ps.executeUpdate();
            ps2.executeUpdate();
            ps3.executeUpdate();
        } catch (SQLException e) {
            Platform.runLater(() ->
                    CustomAlert.showExceptionDialog(e, "Database bootstrapping failed."));
        }
    }

    private static final String sqlRouteCreate = "" +
            "CREATE TABLE IF NOT EXISTS ROUTE(" +
                "ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "URL VARCHAR(255) NOT NULL," +
                "CODE LONGTEXT NOT NULL," +
                "CREATED DATE NOT NULL," +
                "ENABLED BOOLEAN NOT NULL" +
            ");";

    private static final String sqlGroovyCreate = "" +
            "CREATE TABLE IF NOT EXISTS GROOVY(" +
                "ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "CLASS_NAME VARCHAR(255) NOT NULL," +
                "CODE LONGTEXT NOT NULL," +
                "CREATED DATE NOT NULL," +
                "MODIFIED DATE NULL," +
                "ENABLED BOOLEAN NOT NULL" +
            ");";

    private static final String sqlStartupCreate = "" +
            "CREATE TABLE IF NOT EXISTS STARTUP_SCRIPT(" +
                "ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "DESCRIPTION VARCHAR(255) NOT NULL," +
                "CODE LONGTEXT NOT NULL," +
                "ENABLED BOOLEAN NOT NULL" +
            ");";
}
