package com.dustinredmond.apifx.ui;

import com.dustinredmond.apifx.ServerContext;
import com.dustinredmond.apifx.groovy.GroovyEnvironment;
import com.dustinredmond.apifx.model.Route;
import com.dustinredmond.apifx.model.Verb;
import com.dustinredmond.apifx.persistence.RouteDAO;
import com.dustinredmond.apifx.ui.custom.CustomAlert;
import com.dustinredmond.apifx.ui.custom.GroovySyntaxEditor;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

/**
 * Form controller class for the following:
 * <ul>
 *     <li>RouteWindow</li>
 *     <li>RouteAddWindow</li>
 *     <li>RouteEditWindow</li>
 *     <li>RouteDeleteWindow</li>
 * </ul>
 */
public class RoutesController {

    public boolean addRoute(TextField tfRoute, ComboBox<Verb> cbEndpoint, GroovySyntaxEditor taCode) {
        if (tfRoute.getText().trim().isEmpty()
                || cbEndpoint.getSelectionModel().isEmpty()
                || taCode.getText().trim().isEmpty()) {
            CustomAlert.showWarning("All fields are required.");
            return false;
        }
        // System.out.println(getRouteTemplate(tfRoute.getText(), cbEndpoint.getSelectionModel().getSelectedItem(), taCode.getText()));
        if (ServerContext.isActive()) {
            GroovyEnvironment.getInstance().evaluate(
                    getRouteTemplate(
                            tfRoute.getText(),
                            cbEndpoint.getSelectionModel().getSelectedItem(),
                            taCode.getText()));
        }
        Route route = new Route();
        route.setCode(taCode.getText());
        route.setCreated(new Date());
        route.setEnabled(true);
        route.setUrl(tfRoute.getText());
        route.setVerb(cbEndpoint.getSelectionModel().getSelectedItem());
        new RouteDAO().create(route);
        logger.info("Created Route: {}", route.getUrl());
        return true;
    }

    public static String getRouteTemplate(String endpoint, Verb cbVerb, String code) {
        return "spark.Spark."+cbVerb.name().toLowerCase()+"(\""+endpoint+"\", (req, res) -> {\n" +
                "\t"+code+"\n" +
                "});";
    }

    public void removeRoute(Route route) {
        if (ServerContext.isActive()) {
            spark.Spark.unmap(route.getUrl(), route.getVerb().name().toLowerCase());
        }

        if (new RouteDAO().remove(route)) {
            RouteWindow.refreshTable();
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(RoutesController.class);

    public void disableRoute(TableView<Route> table) {
        if (table.getSelectionModel().isEmpty()) {
            CustomAlert.showWarning("Please first select a route from the table.");
            return;
        }
        Route e = table.getSelectionModel().getSelectedItem();
        String disabledOrEnabled = e.isEnabled() ? "enabled" : "disabled";
        String toBe = !e.isEnabled() ? "enable" : "disable";

        String prompt = String.format("The selected route (%s) is %s, are you sure you wish to %s it?",
                e.getUrl(), disabledOrEnabled, toBe);

        if (CustomAlert.showConfirmation(prompt)) {
            if (e.isEnabled() && ServerContext.isActive()) {
                spark.Spark.unmap(e.getUrl(), e.getVerb().name().toLowerCase());
            } else {
                if (ServerContext.isActive()) {
                    GroovyEnvironment.getInstance().evaluate(
                            getRouteTemplate(
                                    e.getUrl(),
                                    e.getVerb(),
                                    e.getCode()));
                }
            }
            e.setEnabled(!e.isEnabled());
            new RouteDAO().update(e);
            RouteWindow.refreshTable();
        }

    }

    public void browseRoute(TableView<Route> table) {
        if (!ServerContext.isActive()) {
            if (!CustomAlert.showConfirmation("The webserver has not been started, do you still wish " +
                    "to open the route in the browser?")) {
                return;
            }
        }
        if (table.getSelectionModel().isEmpty()) {
            return; // Don't show message, should be disabled in front-end
        }
        Route e = table.getSelectionModel().getSelectedItem();
        try {
            Desktop.getDesktop().browse(new URI("http://localhost:"+ ServerContext.getPort() + e.getUrl()));
        } catch (IOException | URISyntaxException ex) {
            CustomAlert.showWarning("Unable to browse to route.");
        }
    }

}
