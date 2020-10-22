package com.dustinredmond.apifx.persistence;

import com.dustinredmond.apifx.model.Route;
import com.dustinredmond.apifx.model.Verb;
import com.dustinredmond.apifx.ui.custom.CustomAlert;
import javafx.application.Platform;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RouteDAO implements DAO<Route> {

    @Override
    public List<Route> findAll() {

        List<Route> routes = new ArrayList<>();
        final String sql = "SELECT ID, URL, CODE, VERB, CREATED, ENABLED FROM ROUTE E";

        try (Connection conn = new ConnectionFactory().connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                routes.add(new Route(rs.getLong("ID"),
                        rs.getString("URL"),
                        rs.getString("CODE"),
                        Verb.valueOf(rs.getString("VERB")),
                        rs.getDate("CREATED"),
                        rs.getBoolean("ENABLED")));
            }
        } catch (SQLException e) {
            Platform.runLater(() -> CustomAlert.showExceptionDialog(e));
        }
        return routes;
    }

    @Override
    public boolean create(Route route) {
        final String sql = "INSERT INTO ROUTE (URL,CODE,VERB,CREATED,ENABLED) VALUES (?,?,?,?,?);";

        try (Connection conn = new ConnectionFactory().connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, route.getUrl());
            ps.setString(2, route.getCode());
            ps.setString(3, route.getVerb().name());
            ps.setDate(4, java.sql.Date.valueOf(LocalDate.now()));
            ps.setBoolean(5, route.isEnabled());
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            CustomAlert.showExceptionDialog(ex);
            return false;
        }
    }

    @Override
    public boolean remove(Route route) {
        final String sql = "DELETE FROM ROUTE WHERE ID = ?";
        try (Connection conn = new ConnectionFactory().connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, route.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            CustomAlert.showExceptionDialog(e);
            return false;
        }
    }

    @Override
    public boolean update(Route e) {
        final String sql = "UPDATE ROUTE SET URL = ?, CODE = ?, VERB = ?, ENABLED = ? WHERE ID = ?";
        try (Connection conn = new ConnectionFactory().connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getUrl());
            ps.setString(2, e.getCode());
            ps.setString(3, e.getVerb().name());
            ps.setBoolean(4, e.isEnabled());
            ps.setLong(5, e.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            CustomAlert.showExceptionDialog(ex);
        }
        return false;
    }
}
