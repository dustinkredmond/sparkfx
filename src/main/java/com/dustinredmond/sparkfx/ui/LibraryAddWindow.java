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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * Class representing the window used to add RouteLibrary objects.
 */
public final class LibraryAddWindow {

    public void show() {
        CustomStage stage = new CustomStage();
        CustomGrid grid = new CustomGrid();
        stage.setTitle(UI.APP_TITLE + " - New Library");

        grid.add(new Label("Class Name:"), 0, 0);
        TextField tfClassName = new TextField();
        grid.add(tfClassName, 1, 0);

        GroovySyntaxEditor se = new GroovySyntaxEditor();
        se.setText(getPromptText());
        grid.add(se, 0, 1, 2, 1);
        GridPane.setVgrow(se, Priority.ALWAYS);
        GridPane.setHgrow(se, Priority.ALWAYS);
        se.setPrefWidth(Double.MAX_VALUE);

        Button buttonAdd = new Button("Add Library");
        buttonAdd.setMinWidth(120);
        grid.add(buttonAdd, 0, 2);
        buttonAdd.setOnAction(e -> {
            if (controller.addLibrary(tfClassName.getText(), se.getText())) {
                se.dispose();
                stage.hide();
            }
        });

        stage.setScene(new Scene(grid));
        stage.setMaximized(true);
        stage.show();
    }

    private String getPromptText() {
        return "class SomeLibrary {\n"
               + "    def someUtilityMethod() {\n"
               + "        println \"Some work was done...\";\n"
               + "    }\n"
               + "}";
    }

    private final RouteLibraryController controller =
        new RouteLibraryController();
}
