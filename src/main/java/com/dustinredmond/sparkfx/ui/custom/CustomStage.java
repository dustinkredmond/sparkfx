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
