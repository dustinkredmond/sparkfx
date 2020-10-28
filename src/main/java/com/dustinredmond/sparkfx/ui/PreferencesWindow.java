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
import com.dustinredmond.sparkfx.ui.custom.CustomAlert;
import com.dustinredmond.sparkfx.ui.custom.CustomGrid;
import com.dustinredmond.sparkfx.ui.custom.CustomMenuBar;
import com.dustinredmond.sparkfx.util.Prefs;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Optional;


/**
 * Class representing the window used to change application preferences.
 */
public class PreferencesWindow {

    public void show(RouteWindow window) {
        UI.getPrimaryStage().setTitle(UI.APP_TITLE + " - Preferences" + " - " + ServerContext.getDescription());
        BorderPane root = new BorderPane();
        CustomGrid grid = new CustomGrid();
        root.setCenter(grid);
        root.setTop(new CustomMenuBar(window));

        grid.add(new Label("Database Path:"), 0, 0);
        TextField tfPath = new TextField(Prefs.get("dbPath", "./fxapi.db"));
        tfPath.setEditable(false);
        grid.add(tfPath, 1, 0);
        Button buttonOverride = new Button("Override");
        grid.add(buttonOverride, 2, 0);

        Button buttonReset = new Button("Reset to Default");
        grid.add(buttonReset, 3, 0);

        String prompt = "Changing the SQLite Database path requires application restart, continue?";
        buttonOverride.setOnAction(e -> {
            if (!CustomAlert.showConfirmation(prompt)) {
                return;
            }
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("SQLite Database", "*.db"));
            File file = fc.showSaveDialog(grid.getScene().getWindow());
            if (file != null) {
                Prefs.put("dbPath", file.getAbsolutePath());
                tfPath.setText(file.getAbsolutePath());
            }
            CustomAlert.showInfo("The application will now exit.");
            System.exit(0);
        });

        buttonReset.setOnAction(e -> {
            if (!CustomAlert.showConfirmation(prompt)) {
                return;
            }

            Prefs.clear("dbPath");
            System.exit(0);
        });


        String defaultPort = Prefs.getLong("appPort") > 0 ? Prefs.getLong("appPort").toString() : "8080";
        grid.add(new Label("Application Port:"), 0, 1);
        TextField taPort = new TextField(defaultPort);
        taPort.setEditable(false);
        grid.add(taPort, 1, 1);
        Button buttonOverridePort = new Button("Override");
        grid.add(buttonOverridePort, 2, 1);
        TextInputDialog tid = new TextInputDialog(defaultPort);
        tid.setHeaderText("");
        tid.setTitle(UI.APP_TITLE);
        tid.setContentText("Enter a new port number.");
        ((Stage) tid.getDialogPane().getScene().getWindow()).getIcons().add(new Image(UI.APP_ICON_URL));
        tid.setGraphic(new ImageView(UI.APP_ICON_URL));
        tid.getEditor().textProperty().addListener((ov, old, newVal) -> {
            if (!newVal.matches("\\d+") && !newVal.isEmpty()) {
                tid.getEditor().setText(old);
            }
        });


        buttonOverridePort.setOnAction(e -> {
            Optional<String> port = tid.showAndWait();
            port.ifPresent(s -> Prefs.putLong("appPort", Long.parseLong(s)));
            CustomAlert.showWarning("The application will now exit. Please restart the application.");
            System.exit(0);
        });

        UI.getPrimaryStage().getScene().setRoot(root);
    }

}
