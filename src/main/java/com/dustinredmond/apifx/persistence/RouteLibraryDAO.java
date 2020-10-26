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

import com.dustinredmond.apifx.model.RouteLibrary;
import com.dustinredmond.apifx.ui.custom.CustomAlert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for the {@code com.dustinredmond.apifx.model.RouteLibrary} class.
 */
public class RouteLibraryDAO implements DAO<RouteLibrary> {

    @Override
    public boolean update(RouteLibrary lib) {
        final String sql = "UPDATE GROOVY SET CLASS_NAME = ?, CODE = ?, MODIFIED = ?, ENABLED = ? WHERE ID = ?";
        try (Connection conn = db.connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lib.getClassName());
            ps.setString(2, lib.getCode());
            ps.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
            ps.setBoolean(4, lib.isEnabled());
            ps.setLong(5, lib.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            CustomAlert.showExceptionDialog(e);
        }
        return false;
    }

    @Override
    public boolean remove(RouteLibrary lib) {
        final String sql = "DELETE FROM GROOVY WHERE ID = ?";
        try (Connection conn = db.connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, lib.getId());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            CustomAlert.showExceptionDialog(e);
            return false;
        }
    }

    @Override
    public boolean create(RouteLibrary lib) {
        final String sql = "INSERT INTO GROOVY (CLASS_NAME,CODE,CREATED,ENABLED) VALUES (?,?,?,?)";
        try (Connection conn = db.connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lib.getClassName());
            ps.setString(2, lib.getCode());
            ps.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
            ps.setBoolean(4, lib.isEnabled());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            CustomAlert.showExceptionDialog(e);
            return false;
        }
    }

    @Override
    public List<RouteLibrary> findAll() {
        List<RouteLibrary> classes = new ArrayList<>();
        final String sql = "SELECT ID, CLASS_NAME, CODE, CREATED, MODIFIED, ENABLED FROM GROOVY";

        try (Connection conn = db.connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                RouteLibrary clazz = new RouteLibrary();
                clazz.setId(rs.getLong("ID"));
                clazz.setClassName(rs.getString("CLASS_NAME"));
                clazz.setCode(rs.getString("CODE"));
                clazz.setCreated(rs.getDate("CREATED"));
                clazz.setModified(rs.getDate("MODIFIED"));
                clazz.setEnabled(rs.getBoolean("ENABLED"));
                classes.add(clazz);
            }
        } catch (SQLException e) {
            CustomAlert.showExceptionDialog(e);
        }
        return classes;
    }

    /**
     * Finds the first RouteLibrary with the given class name, or null if
     * one is unable to be found with the class name specified.
     * @param className The class name of the RouteLibrary to retrieve
     * @return RouteLibrary, or null if not found
     */
    public RouteLibrary findByClassName(String className) {
        Optional<RouteLibrary> clazz = findAll().stream()
                .filter(c -> c.getClassName().equals(className))
                .findFirst();
        return clazz.orElse(null);
    }

    private static final ConnectionFactory db = new ConnectionFactory();
}
