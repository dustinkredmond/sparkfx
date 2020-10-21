package com.dustinredmond.apifx.ui;

import com.dustinredmond.apifx.model.Route;
import com.dustinredmond.apifx.ui.custom.CustomAlert;
import javafx.scene.control.TableView;

/**
 * Class representing the window used to delete Route objects.
 */
public class RouteDeleteWindow {

    public void show(TableView<Route> table) {
        if (table.getSelectionModel().isEmpty()) {
            CustomAlert.showWarning("Please select an API route first.");
            return;
        }

        Route e = table.getSelectionModel().getSelectedItem();
        final String prompt = String.format("Are you sure you wish to remove route: %s", e.getUrl());
        if (CustomAlert.showConfirmation(prompt)) {
            controller.removeRoute(e);
            RouteWindow.refreshTable();
        }
    }

    private static final RoutesController controller = new RoutesController();
}
