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

import com.dustinredmond.sparkfx.ui.custom.CustomGrid;
import com.dustinredmond.sparkfx.ui.custom.CustomStage;
import com.dustinredmond.sparkfx.ui.custom.GroovySyntaxEditor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * Class to represent the window used to add StartupScripts
 */
public class StartupScriptAddWindow {

    public void show() {
        CustomStage stage = new CustomStage();
        stage.setTitle(UI.APP_TITLE + " - Add Startup Script");
        CustomGrid grid = new CustomGrid();

        grid.add(new Label("Description"), 0, 0);
        TextField tfDescription = new TextField();
        grid.add(tfDescription, 1, 0);

        CheckBox cbEnabled = new CheckBox("Enabled?");
        grid.add(cbEnabled, 0, 1);

        GroovySyntaxEditor gse = new GroovySyntaxEditor();
        gse.setText(PROMPT);
        grid.add(new Label("Code:"), 0, 2);
        grid.add(gse, 1, 2, 2, 1);
        GridPane.setVgrow(gse, Priority.ALWAYS);
        GridPane.setHgrow(gse, Priority.ALWAYS);
        gse.setPrefWidth(Double.MAX_VALUE);

        Button buttonAdd = new Button("Add Script");
        buttonAdd.setOnAction(e -> {
            if (controller.addStartupScript(tfDescription.getText(), cbEnabled.isSelected(), gse.getText())) {
                stage.hide();
            }
        });
        grid.add(buttonAdd, 0, 3);

        stage.setScene(new Scene(grid));
        stage.setMaximized(true);
        stage.show();
    }

    private static final StartupScriptController controller = new StartupScriptController();
    private static final String PROMPT = "import spark.Spark as http\n\n" +
            "// Example script: GZIP everything\n" +
            "http.after((req,res) -> res.header(\"Content-Encoding\", \"gzip\"));";
}
