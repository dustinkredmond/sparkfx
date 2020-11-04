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
import com.dustinredmond.sparkfx.model.Verb;
import com.dustinredmond.sparkfx.persistence.RouteDAO;
import com.dustinredmond.sparkfx.ui.custom.CustomAlert;
import com.dustinredmond.sparkfx.ui.custom.CustomGrid;
import com.dustinredmond.sparkfx.ui.custom.CustomMenuBar;
import com.dustinredmond.sparkfx.util.FXUtils;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import spark.Spark;
import spark.routematch.RouteMatch;

import java.util.Date;
import java.util.Optional;

/**
 * Class to represent the window used to view Routes.
 */
public final class RouteWindow {

    public void show() {
        UI.getPrimaryStage().setTitle(UI.APP_TITLE
            + " - Routes Overview - " + ServerContext.getDescription());
        CustomGrid grid = new CustomGrid();
        BorderPane root = new BorderPane();
        CustomMenuBar mb = new CustomMenuBar(this);
        mb.enableServerStopItem(ServerContext.isActive());
        mb.enableServerStartItem(!ServerContext.isActive());
        root.setTop(mb);
        root.setCenter(grid);

        ButtonBar buttons = new ButtonBar();
        Button buttonAdd = new Button("Add Route");
        buttonAdd.setOnAction(e -> new RouteAddWindow().show());
        Button buttonEdit = new Button("Edit Route");
        buttonEdit.setOnAction(e -> new RouteEditWindow().show(table));
        Button buttonDelete = new Button("Delete Route");
        buttonDelete.setOnAction(e -> new RouteDeleteWindow().show(table));
        buttons.getButtons().addAll(buttonAdd, buttonEdit, buttonDelete);

        grid.add(buttons, 0, 0);

        grid.add(table, 0, 1);
        GridPane.setHgrow(table, Priority.ALWAYS);
        GridPane.setVgrow(table, Priority.ALWAYS);

        UI.getPrimaryStage().getScene().setRoot(root);
    }

    private static final TableView<Route> table = getRoutesTable();

    public TableView<Route> getTable() {
        return table;
    }

    private static TableView<Route> getRoutesTable() {
        TableView<Route> table = new TableView<>();

        TableColumn<Route, Date> columnCreated =
            new TableColumn<>("Created");
        columnCreated.setCellValueFactory(
            new PropertyValueFactory<>("created"));
        table.getColumns().add(columnCreated);

        TableColumn<Route, String> columnUrl =
            new TableColumn<>("URL");
        columnUrl.setCellValueFactory(
            new PropertyValueFactory<>("url"));
        table.getColumns().add(columnUrl);

        TableColumn<Route, Boolean> columnEnabled =
            new TableColumn<>("Enabled");
        columnEnabled.setCellValueFactory(e ->
            new SimpleBooleanProperty(e.getValue().isEnabled()));
        columnEnabled.setCellFactory(cell ->
            new CheckBoxTableCell<>());
        table.getColumns().add(columnEnabled);

        TableColumn<Route, Verb> columnVerb = new TableColumn<>("HTTP Method");
        columnVerb.setCellValueFactory(new PropertyValueFactory<>("verb"));
        columnVerb.setCellValueFactory(e -> {
            if (!ServerContext.isActive()) {
                return new SimpleObjectProperty<>(Verb.UNKN);
            }
            Optional<RouteMatch> route = Spark.routes().stream()
                .filter(r -> r.getMatchUri().equals(e.getValue().getUrl()))
                .findFirst();
            return route.map(routeMatch -> new SimpleObjectProperty<>(
                Verb.valueOf(routeMatch.getHttpMethod().name().toUpperCase())))
                    .orElse(new SimpleObjectProperty<>(Verb.UNKN));
        });
        table.getColumns().add(columnVerb);

        table.setItems(FXCollections.observableArrayList(
            new RouteDAO().findAll()));
        FXUtils.autoResizeColumns(table);
        ContextMenu cm = new ContextMenu();

        MenuItem miBrowse = new MenuItem("Open in Browser");
        miBrowse.setOnAction(e -> controller.browseRoute(table));
        MenuItem miDisable = new MenuItem("Disable/Enable Route");
        miDisable.setOnAction(e -> controller.disableRoute(table));
        MenuItem miAdd = new MenuItem("Add Route");
        miAdd.setOnAction(e -> new RouteAddWindow().show());
        MenuItem miEdit = new MenuItem("Edit Route");
        miEdit.setOnAction(e -> new RouteEditWindow().show(table));
        MenuItem miDelete = new MenuItem("Delete Route");
        miDelete.setOnAction(e -> new RouteDeleteWindow().show(table));

        cm.getItems().addAll(miBrowse, miDisable,
            new SeparatorMenuItem(), miAdd, miEdit, miDelete);
        table.setContextMenu(cm);

        table.setOnContextMenuRequested(e -> {
            if (!ServerContext.isActive()) {
                CustomAlert.showInfo("The server is not running, "
                    + "please start it before making "
                    + "changes to a route.");
                cm.hide();
            }
            if (table.getSelectionModel().isEmpty()) {
                miBrowse.setDisable(true);
                miEdit.setDisable(true);
                miDelete.setDisable(true);
                miDisable.setDisable(true);
            } else {
                miBrowse.setDisable(false);
                miEdit.setDisable(false);
                miDelete.setDisable(false);
                miDisable.setDisable(false);
            }
        });

        table.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2
                && MouseButton.PRIMARY.equals(e.getButton())) {
                new RouteEditWindow().show(table);
            }
        });

        table.setPlaceholder(new Label(
            "Welcome to " + UI.APP_TITLE + " click on "
                + "\"Add Route\" to get started!"));
        return table;
    }

    /**
     * Refreshes the TableView associated with RouteWindow.
     */
    public static void refreshTable() {
        table.setItems(FXCollections.observableArrayList(
            new RouteDAO().findAll()));
        FXUtils.autoResizeColumns(table);
    }

    private static final RoutesController controller = new RoutesController();
}
