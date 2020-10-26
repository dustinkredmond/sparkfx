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

import com.dustinredmond.apifx.model.RouteLibrary;
import com.dustinredmond.apifx.ui.custom.CustomAlert;
import com.dustinredmond.apifx.ui.custom.CustomGrid;
import com.dustinredmond.apifx.ui.custom.CustomStage;
import com.dustinredmond.apifx.ui.custom.GroovySyntaxEditor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class LibraryEditWindow {
    public void show() {
        TableView<RouteLibrary> table = RouteLibraryWindow.getTable();
        if (table.getSelectionModel().isEmpty()) {
            CustomAlert.showWarning("Please first select an item from the table.");
            return;
        }

        RouteLibrary lib = table.getSelectionModel().getSelectedItem();
        CustomStage stage = new CustomStage();
        CustomGrid grid = new CustomGrid();
        stage.setTitle(UI.APP_TITLE + " - Edit Library");

        grid.add(new Label("Class Name:"), 0, 0);
        TextField tfClassName = new TextField(lib.getClassName());
        grid.add(tfClassName, 1, 0);

        GroovySyntaxEditor se = new GroovySyntaxEditor();
        se.setText(lib.getCode());
        grid.add(se, 0, 1, 2, 1);
        GridPane.setVgrow(se, Priority.ALWAYS);
        GridPane.setHgrow(se, Priority.ALWAYS);
        se.setPrefWidth(Double.MAX_VALUE);

        Button buttonAdd = new Button("Save Changes");
        buttonAdd.setMinWidth(120);
        grid.add(buttonAdd, 0, 2);
        buttonAdd.setOnAction(e -> {
            if (controller.editLibrary(lib, tfClassName.getText(), se.getText())) {
                stage.hide();
            }
        });

        stage.setScene(new Scene(grid));
        stage.setMaximized(true);
        stage.show();
    }

    private static final RouteLibraryController controller = new RouteLibraryController();
}
