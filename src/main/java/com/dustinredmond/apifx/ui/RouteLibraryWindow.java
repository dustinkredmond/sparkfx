package com.dustinredmond.apifx.ui;

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

import com.dustinredmond.apifx.ServerContext;
import com.dustinredmond.apifx.model.RouteLibrary;
import com.dustinredmond.apifx.persistence.RouteLibraryDAO;
import com.dustinredmond.apifx.ui.custom.CustomGrid;
import com.dustinredmond.apifx.ui.custom.CustomMenuBar;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.util.Date;

/**
 * Class representing the window used to display RouteLibraries
 */
public class RouteLibraryWindow {

    public void show(RouteWindow parentWindow) {
        BorderPane root = new BorderPane();
        UI.getPrimaryStage().getScene().setRoot(root);
        UI.getPrimaryStage().setTitle(UI.APP_TITLE + " - Route Libraries - " + ServerContext.getDescription());
        CustomGrid grid = new CustomGrid();
        root.setCenter(grid);
        root.setTop(new CustomMenuBar(parentWindow));

        ButtonBar buttons = new ButtonBar();
        Button buttonAdd = new Button("Add Library");
        buttonAdd.setOnAction(e -> new LibraryAddWindow().show());
        Button buttonEdit = new Button("Edit Library");
        buttonEdit.setOnAction(e -> new LibraryEditWindow().show());
        Button buttonDelete = new Button("Delete Library");
        buttonDelete.setOnAction(e -> new LibraryDeleteWindow().show());
        buttons.getButtons().addAll(buttonAdd, buttonEdit, buttonDelete);
        grid.add(buttons, 0, 0);

        grid.add(table, 0, 1);
        GridPane.setHgrow(table, Priority.ALWAYS);
        GridPane.setVgrow(table, Priority.ALWAYS);
    }

    private static TableView<RouteLibrary> getTableView() {
        TableView<RouteLibrary> table = new TableView<>();

        TableColumn<RouteLibrary, String> columnClassName = new TableColumn<>("Class Name");
        columnClassName.setCellValueFactory(new PropertyValueFactory<>("className"));
        table.getColumns().add(columnClassName);

        TableColumn<RouteLibrary, Date> columnCreated = new TableColumn<>("Created");
        columnCreated.setCellValueFactory(new PropertyValueFactory<>("created"));
        table.getColumns().add(columnCreated);

        TableColumn<RouteLibrary, Date> columnModified = new TableColumn<>("Modified");
        columnModified.setCellValueFactory(new PropertyValueFactory<>("modified"));
        table.getColumns().add(columnModified);

        TableColumn<RouteLibrary, Boolean> columnEnabled = new TableColumn<>("Enabled");
        columnEnabled.setCellValueFactory(c -> new SimpleBooleanProperty(c.getValue().isEnabled()));
        columnEnabled.setCellFactory(cell -> new CheckBoxTableCell<>());
        table.getColumns().add(columnEnabled);

        table.setPlaceholder(new Label("No Libraries exist. Click on \"Add Library\" to get started."));
        table.setItems(FXCollections.observableArrayList(new RouteLibraryDAO().findAll()));

        ContextMenu cm = new ContextMenu();
        MenuItem miEnableDisable = new MenuItem("Enable/Disable Library");
        miEnableDisable.setOnAction(e -> controller.enableDisableLibrary());
        MenuItem miAdd = new MenuItem("Add Library");
        miAdd.setOnAction(e -> new LibraryAddWindow().show());
        MenuItem miEdit = new MenuItem("Edit Library");
        miEdit.setOnAction(e -> new LibraryEditWindow().show());
        MenuItem miDelete = new MenuItem("Delete Library");
        miDelete.setOnAction(e -> new LibraryDeleteWindow().show());
        cm.getItems().addAll(miEnableDisable, new SeparatorMenuItem(), miAdd, miEdit, miDelete);
        table.setContextMenu(cm);

        table.setOnContextMenuRequested(e -> {
            if (table.getSelectionModel().isEmpty()) {
                miEnableDisable.setDisable(true);
                miEdit.setDisable(true);
                miDelete.setDisable(true);
            } else {
                miEnableDisable.setDisable(false);
                miEdit.setDisable(false);
                miDelete.setDisable(false);
            }
        });

        table.setOnMouseClicked(m -> {
            if (m.getClickCount() == 2
                    && m.getButton().equals(MouseButton.PRIMARY)
                    && !table.getSelectionModel().isEmpty()) {
                new LibraryEditWindow().show();
            }
        });

        return table;
    }

    private static final TableView<RouteLibrary> table = getTableView();
    private static final RouteLibraryController controller = new RouteLibraryController();

    public static void refreshTableView() {
        table.setItems(FXCollections.observableArrayList(new RouteLibraryDAO().findAll()));
    }

    public static TableView<RouteLibrary> getTable() {
        return RouteLibraryWindow.table;
    }
}
