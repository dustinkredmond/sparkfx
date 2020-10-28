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
import com.dustinredmond.sparkfx.model.StartupScript;
import com.dustinredmond.sparkfx.persistence.StartupScriptDAO;
import com.dustinredmond.sparkfx.ui.custom.CustomGrid;
import com.dustinredmond.sparkfx.ui.custom.CustomMenuBar;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * Class used to represent the window for displaying StartupScripts
 */
public class StartupScriptWindow {

    public static void refreshTable() {
        table.setItems(FXCollections.observableArrayList(new StartupScriptDAO().findAll()));
    }

    public void show(RouteWindow parentWindow) {
        BorderPane root = new BorderPane();
        CustomGrid grid = new CustomGrid();
        root.setCenter(grid);
        root.setTop(new CustomMenuBar(parentWindow));

        ButtonBar buttons = new ButtonBar();
        Button buttonAdd = new Button("Add Script");
        buttonAdd.setOnAction(e -> new StartupScriptAddWindow().show());
        Button buttonEdit = new Button("Edit Script");
        buttonEdit.setOnAction(e -> new StartupScriptEditWindow().show());
        Button buttonDelete = new Button("Delete Script");
        buttonDelete.setOnAction(e -> new StartupScriptDeleteWindow().show());
        buttons.getButtons().addAll(buttonAdd, buttonEdit, buttonDelete);
        grid.add(buttons, 0, 0);

        grid.add(table, 0, 1);
        GridPane.setHgrow(table, Priority.ALWAYS);
        GridPane.setVgrow(table, Priority.ALWAYS);

        UI.getPrimaryStage().getScene().setRoot(root);
        UI.getPrimaryStage().setTitle(UI.APP_TITLE + " - Startup Scripts - " + ServerContext.getDescription());
    }

    private static final TableView<StartupScript> table = getTableView();

    public static TableView<StartupScript> getTable() { return  StartupScriptWindow.table; }

    private static TableView<StartupScript> getTableView() {
        TableView<StartupScript> table = new TableView<>();

        TableColumn<StartupScript, String> columnDesc = new TableColumn<>("Description");
        columnDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        table.getColumns().add(columnDesc);

        TableColumn<StartupScript, Boolean> columnEnabled = new TableColumn<>("Enabled");
        columnEnabled.setCellValueFactory(s -> new SimpleBooleanProperty(s.getValue().isEnabled()));
        columnEnabled.setCellFactory(c -> new CheckBoxTableCell<>());
        table.getColumns().add(columnEnabled);

        ContextMenu cm = new ContextMenu();
        MenuItem miEnableDisable = new MenuItem("Enable/Disable Startup Script");
        miEnableDisable.setOnAction(e -> controller.enableDisableScript());
        MenuItem miRun = new MenuItem("Run On Demand");
        miRun.setOnAction(e -> controller.runOnDemand());
        MenuItem miAdd = new MenuItem("Add Startup Script");
        miAdd.setOnAction(e -> new StartupScriptAddWindow().show());
        MenuItem miEdit = new MenuItem("Edit Startup Script");
        miEdit.setOnAction(e -> new StartupScriptEditWindow().show());
        MenuItem miDelete = new MenuItem("Delete Startup Script");
        miDelete.setOnAction(e -> new StartupScriptDeleteWindow().show());
        cm.getItems().addAll(miEnableDisable, miRun, new SeparatorMenuItem(), miAdd, miEdit, miDelete);

        table.setContextMenu(cm);

        table.setOnContextMenuRequested(e -> {
                if (!table.getSelectionModel().isEmpty()) {
                    miEdit.setDisable(false);
                    miDelete.setDisable(false);
                    miEnableDisable.setDisable(false);
                } else {
                    miEdit.setDisable(true);
                    miDelete.setDisable(true);
                    miEnableDisable.setDisable(true);
                }
        });

        table.setOnMouseClicked(m -> {
            if (m.getButton().equals(MouseButton.PRIMARY) && m.getClickCount() == 2
                    && !table.getSelectionModel().isEmpty()) {
                new StartupScriptEditWindow().show();
            }
        });

        table.setItems(FXCollections.observableArrayList(new StartupScriptDAO().findAll()));

        return table;
    }

    private static final StartupScriptController controller = new StartupScriptController();
}
