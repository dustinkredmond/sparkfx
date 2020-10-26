package com.dustinredmond.apifx.ui.custom;

import com.dustinredmond.apifx.ui.UI;
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
