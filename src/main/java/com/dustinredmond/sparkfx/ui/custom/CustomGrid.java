package com.dustinredmond.sparkfx.ui.custom;

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

import com.dustinredmond.sparkfx.ui.UI;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Custom implementation of {@code javafx.scene.layout.GridPane}
 * with default padding, style, parent window application icon,
 * and H/V gaps set.
 */
public class CustomGrid extends GridPane {

    public CustomGrid() {
        this.setHgap(5);
        this.setVgap(5);
        this.setPadding(new Insets(10));
        applyWindowIcon();
    }

    public CustomGrid(VBox vBox) {
        this.setHgap(5);
        this.setVgap(5);
        this.setPadding(new Insets(10));
        applyWindowIcon();
        this.getChildren().add(vBox);
    }

    /**
     * Attempts to style the parent window with the app icon
     * fails silently if the icon cannot be applied
     */
    private void applyWindowIcon() {
        if (this.getScene() == null) {
            return;
        }
        try {
            Stage stage = (Stage) this.getScene().getWindow();
            stage.getIcons().add(new Image(UI.APP_ICON_URL));
        } catch (Exception ignored) {}
    }
}
