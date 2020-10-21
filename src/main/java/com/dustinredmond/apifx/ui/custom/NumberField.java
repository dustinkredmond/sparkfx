package com.dustinredmond.apifx.ui.custom;

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
