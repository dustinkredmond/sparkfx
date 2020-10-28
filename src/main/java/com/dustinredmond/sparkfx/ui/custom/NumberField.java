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

import javafx.scene.control.TextField;

/**
 * Custom implementation of {@code javafx.scene.control.TextField} which
 * only allows for the input of Integer/Long values
 */
public class NumberField extends TextField {

    public NumberField() {
        this.textProperty().addListener((ov, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                this.setText(oldValue);
            }
        });
    }

    /**
     * Return the value of the TextField as a {@code java.lang.Long}
     * @return The Long value of the field.
     */
    public Long getLong() {
        return Long.parseLong(this.getText());
    }

    /**
     * Return the value of the TextField as a {@code java.lang.Integer}
     * @return The Integer value of the field.
     */
    public Integer getInteger() {
        return Integer.parseInt(this.getText());
    }

}
