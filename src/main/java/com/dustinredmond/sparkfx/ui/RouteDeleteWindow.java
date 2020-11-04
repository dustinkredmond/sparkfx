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
import com.dustinredmond.sparkfx.model.Route;
import com.dustinredmond.sparkfx.ui.custom.CustomAlert;
import javafx.scene.control.TableView;

/**
 * Class representing the window used to delete Route objects.
 */
public final class RouteDeleteWindow {

    public void show(final TableView<Route> table) {
        if (!ServerContext.isActive()) {
            CustomAlert.showInfo(
                "The server is not running, "
                    + "please start it before adding/modifying routes");
            return;
        }
        if (table.getSelectionModel().isEmpty()) {
            CustomAlert.showWarning("Please select an API route first.");
            return;
        }

        Route e = table.getSelectionModel().getSelectedItem();
        final String prompt = String.format(
            "Are you sure you wish to remove route: %s", e.getUrl());
        if (CustomAlert.showConfirmation(prompt)) {
            controller.removeRoute(e);
            RouteWindow.refreshTable();
        }
    }

    private final RoutesController controller = new RoutesController();
}
