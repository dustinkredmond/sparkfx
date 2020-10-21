package com.dustinredmond.apifx.ui.custom;

import com.dustinredmond.apifx.ui.UI;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class CustomStage extends Stage {

    /**
     * Custom implementation of {@code javafx.stage.Stage} with
     * the application's icon.
     */
    public CustomStage() {
        try {
            this.getIcons().add(new Image(UI.APP_ICON_URL));
        } catch (Exception ignored) {}
    }

}
