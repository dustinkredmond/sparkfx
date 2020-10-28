package com.dustinredmond.sparkfx.ui;

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
import com.dustinredmond.sparkfx.groovy.GroovyEnvironment;
import com.dustinredmond.sparkfx.model.Route;
import com.dustinredmond.sparkfx.persistence.RouteDAO;
import com.dustinredmond.sparkfx.ui.custom.CustomAlert;
import com.dustinredmond.sparkfx.ui.custom.GroovySyntaxEditor;
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

    public boolean addRoute(TextField tfRoute, GroovySyntaxEditor taCode) {
        if (tfRoute.getText().trim().isEmpty() || taCode.getText().trim().isEmpty()) {
            CustomAlert.showWarning("All fields are required.");
            return false;
        }
        if (ServerContext.isActive()) {
            GroovyEnvironment.getInstance().evaluate(
                    taCode.getText());
        }
        Route route = new Route();
        route.setCode(taCode.getText());
        route.setCreated(new Date());
        route.setEnabled(true);
        route.setUrl(tfRoute.getText());
        new RouteDAO().create(route);
        logger.info("Created Route: {}", route.getUrl());
        return true;
    }

    public void removeRoute(Route route) {
        if (ServerContext.isActive()) {
            spark.Spark.unmap(route.getUrl());
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
                spark.Spark.unmap(e.getUrl());
            } else {
                if (ServerContext.isActive()) {
                    GroovyEnvironment.getInstance().evaluate(
                            e.getCode());
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
