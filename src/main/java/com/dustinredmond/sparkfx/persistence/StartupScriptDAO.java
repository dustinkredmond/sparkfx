package com.dustinredmond.sparkfx.persistence;

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

import com.dustinredmond.sparkfx.ServerContext;
import com.dustinredmond.sparkfx.model.StartupScript;
import com.dustinredmond.sparkfx.ui.custom.CustomAlert;
import javafx.application.Platform;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for the
 * {@code com.dustinredmond.apifx.model.StartupScript}.
 */
@SuppressWarnings("SqlNoDataSourceInspection")
public final class StartupScriptDAO implements DAO<StartupScript> {

    @Override
    public boolean update(final StartupScript script) {
        final String sql =
            "UPDATE STARTUP_SCRIPT "
                + "SET DESCRIPTION = ?, CODE = ?, ENABLED = ? "
                + "WHERE ID = ?";
        try (Connection conn = DB.connect();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, script.getDescription());
            ps.setString(2, script.getCode());
            ps.setBoolean(3, script.isEnabled());
            ps.setLong(4, script.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            CustomAlert.showExceptionDialog(e);
        }
        return false;
    }

    @Override
    public boolean remove(final StartupScript script) {
        final String sql = "DELETE FROM STARTUP_SCRIPT WHERE ID = ?";
        try (Connection conn = DB.connect();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, script.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            CustomAlert.showExceptionDialog(e);
        }
        return false;
    }

    @Override
    public boolean create(final StartupScript script) {
        final String sql =
            "INSERT INTO STARTUP_SCRIPT (DESCRIPTION,CODE,ENABLED) "
                + "VALUES (?,?,?)";
        try (Connection conn = DB.connect();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, script.getDescription());
            ps.setString(2, script.getCode());
            ps.setBoolean(3, script.isEnabled());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            CustomAlert.showExceptionDialog(e);
        }
        return false;
    }

    @Override
    public List<StartupScript> findAll() {
        List<StartupScript> scripts = new ArrayList<>();
        final String sql = "SELECT ID, DESCRIPTION, CODE, ENABLED "
            + "FROM STARTUP_SCRIPT";
        try (Connection conn = DB.connect();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                scripts.add(new StartupScript(rs.getLong("ID"),
                        rs.getString("DESCRIPTION"),
                        rs.getString("CODE"),
                        rs.getBoolean("ENABLED")));
            }
        } catch (SQLException e) {
            if (ServerContext.isHeadless()) {
                e.printStackTrace();
            } else {
                Platform.runLater(() -> CustomAlert.showExceptionDialog(e));
            }
        }
        return scripts;
    }

    /**
     * Default ConnectionFactory for the application's database.
     */
    private static final ConnectionFactory DB = new ConnectionFactory();
}
