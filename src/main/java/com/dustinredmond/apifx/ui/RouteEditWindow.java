package com.dustinredmond.apifx.ui;

import com.dustinredmond.apifx.model.Route;
import com.dustinredmond.apifx.model.Verb;
import com.dustinredmond.apifx.ui.custom.CustomAlert;
import com.dustinredmond.apifx.ui.custom.CustomGrid;
import com.dustinredmond.apifx.ui.custom.CustomStage;
import com.dustinredmond.apifx.ui.custom.GroovySyntaxEditor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * Class representing the window used to delete Route objects
 */
public class RouteEditWindow {

    public void show(TableView<Route> table) {
        if (table.getSelectionModel().isEmpty()) {
            CustomAlert.showWarning("Please first select a route from the table.");
        } else {
            showEditDialog(table);
        }
    }

    private void showEditDialog(TableView<Route> table) {
        final Route route = table.getSelectionModel().getSelectedItem();
        CustomStage stage = new CustomStage();
        stage.setTitle("Edit Route");
        CustomGrid grid = new CustomGrid();
        stage.setScene(new Scene(grid));

        int rowIndex = 0;

        TextField tfRoute = new TextField(route.getUrl());
        grid.add(new Label("API Endpoint:"), 0, rowIndex);
        grid.add(tfRoute, 1, rowIndex++);

        ComboBox<Verb> cbEndpoint = new ComboBox<>();
        cbEndpoint.getItems().addAll(Verb.values());
        cbEndpoint.getSelectionModel().select(route.getVerb());
        grid.add(new Label("HTTP Verb:"), 0, rowIndex);
        grid.add(cbEndpoint, 1, rowIndex++);

        GroovySyntaxEditor taCode = new GroovySyntaxEditor();
        taCode.setText(route.getCode());
        grid.add(taCode, 0, rowIndex++, 2, 1);
        GridPane.setVgrow(taCode, Priority.ALWAYS);
        GridPane.setHgrow(taCode, Priority.ALWAYS);

        Button buttonAdd = new Button("Save Changes");
        buttonAdd.setMinWidth(120);
        buttonAdd.setOnAction(e -> {
            if (!CustomAlert.showConfirmation(REMOVE_PROMPT)) {
                return;
            }
            controller.removeRoute(route);
            if (controller.addRoute(tfRoute, cbEndpoint, taCode)) {
                stage.hide();
                RouteWindow.refreshTable();
            }
        });
        grid.add(buttonAdd, 0, rowIndex);

        stage.setMaximized(true);
        stage.show();
    }

    private static final RoutesController controller = new RoutesController();
    private static final String REMOVE_PROMPT = "In order to modify the route, the current route will have to first be " +
            "removed. Ensure that no users are using the currently mapped route. Are you sure you wish to continue?";
}
