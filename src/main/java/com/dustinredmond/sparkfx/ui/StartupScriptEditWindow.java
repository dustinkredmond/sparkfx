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

import com.dustinredmond.sparkfx.model.StartupScript;
import com.dustinredmond.sparkfx.ui.custom.CustomAlert;
import com.dustinredmond.sparkfx.ui.custom.CustomGrid;
import com.dustinredmond.sparkfx.ui.custom.CustomStage;
import com.dustinredmond.sparkfx.ui.custom.GroovySyntaxEditor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * Class to represent the window used to edit StartupScripts
 */
public class StartupScriptEditWindow {

    public void show() {
        if (table.getSelectionModel().isEmpty()) {
            CustomAlert.showWarning("Please select a script from the table first.");
            return;
        }

        StartupScript script = table.getSelectionModel().getSelectedItem();

        CustomStage stage = new CustomStage();
        stage.setTitle(UI.APP_TITLE + " - Edit Startup Script");
        CustomGrid grid = new CustomGrid();

        grid.add(new Label("Description"), 0, 0);
        TextField tfDescription = new TextField(script.getDescription());
        grid.add(tfDescription, 1, 0);

        CheckBox cbEnabled = new CheckBox("Enabled?");
        cbEnabled.setSelected(script.isEnabled());
        grid.add(cbEnabled, 0, 1);

        GroovySyntaxEditor gse = new GroovySyntaxEditor();
        gse.setText(script.getCode());
        grid.add(new Label("Code:"), 0, 2);
        grid.add(gse, 1, 2, 2, 1);
        GridPane.setVgrow(gse, Priority.ALWAYS);
        GridPane.setHgrow(gse, Priority.ALWAYS);
        gse.setPrefWidth(Double.MAX_VALUE);

        Button buttonAdd = new Button("Save Changes");
        buttonAdd.setOnAction(e -> {
            if (controller.editStartupScript(script, tfDescription.getText(), gse.getText(), cbEnabled.isSelected())) {
                gse.dispose();
                stage.hide();
                StartupScriptWindow.refreshTable();
            }
        });
        grid.add(buttonAdd, 0, 3);

        stage.setScene(new Scene(grid));
        stage.setMaximized(true);
        stage.show();
    }

    private static final StartupScriptController controller = new StartupScriptController();
    private final TableView<StartupScript> table = StartupScriptWindow.getTable();
}
