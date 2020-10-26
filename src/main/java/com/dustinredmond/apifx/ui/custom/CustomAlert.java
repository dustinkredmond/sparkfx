package com.dustinredmond.apifx.ui.custom;

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

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

/**
 * Utility class for displaying custom JavaFX Alert dialogs.
 * Methods in this class must be called from the JavaFX
 * Application thread, or wrapped in a call to {@code Platform.runLater()}
 */
public class CustomAlert {

    private static String iconPath;
    private static String applicationTitle = "";

    /**
     * Displays a JavaFX (INFORMATION) Alert with custom title bar icon.
     *
     * @param header  Dialog header text.
     * @param content Dialog message text.
     */
    public static void showInfo(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        addDialogIconTo(alert, true);
        alert.setTitle(applicationTitle);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Displays a JavaFX (INFORMATION) Alert with custom title bar icon.
     *
     * @param contentText Dialog message text.
     */
    public static void showInfo(String contentText) {
        showInfo("", contentText);
    }

    /**
     * Displays a JavaFX (ERROR) Alert with custom title bar icon.
     *
     * @param header  Dialog header text.
     * @param content Dialog message text.
     */
    public static void showWarning(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        addDialogIconTo(alert, false);
        alert.setTitle(applicationTitle);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Displays a JavaFX (ERROR) Alert with custom title bar icon.
     *
     * @param contentText Dialog message text.
     */
    public static void showWarning(String contentText) {
        showWarning("", contentText);
    }

    /**
     * Shows a JavaFX (CONFIRMATION Alert. Returns true if and only if user
     * presses OK button.
     *
     * @param headerText  JavaFX Alert's Header Text.
     * @param contentText JavaFX Alert's Content Text.
     * @return true if user presses OK.
     */
    public static Boolean showConfirmation(String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        addDialogIconTo(alert, true);
        alert.setTitle(applicationTitle);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get().equals(ButtonType.YES);
    }

    /**
     * Shows a JavaFX (CONFIRMATION) Alert. Returns true if and only if
     * user presses OK button.
     *
     * @param contentText JavaFX Alert's Content Text.
     * @return true if user presses OK.
     */
    public static Boolean showConfirmation(String contentText) {
        return showConfirmation("", contentText);
    }

    /**
     * Show a dialog with the Exception stack trace.
     *
     * @param e Exception whose stack trace to show.
     */
    public static void showExceptionDialog(Throwable e) {
        showExceptionDialog(e, "");
    }

    /**
     * Shows a dialog with the Exception stack trace.
     *
     * @param e       Exception whose stack trace to show.
     * @param message Dialog message content.
     */
    public static void showExceptionDialog(Throwable e, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        addDialogIconTo(alert, false);
        alert.setTitle(applicationTitle);
        alert.setHeaderText("Exception Occurred");
        alert.setContentText(message.isEmpty() ? "An unknown error occurred.\n" + e.getMessage() : message);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);
        alert.getDialogPane().setExpandableContent(expContent);
        alert.getDialogPane().setExpanded(true);
        alert.showAndWait();
    }

    /**
     * Adds a custom icon to a JavaFX Alert. Passing true as the second parameter
     * will also add the icon as the Alert's header graphic.
     *
     * @param alert                  JavaFX alert whose icon should be customized.
     * @param addCustomHeaderGraphic Pass true to modify the header graphic as well.
     */
    private static void addDialogIconTo(Alert alert, boolean addCustomHeaderGraphic) {
        if (iconPath != null && !iconPath.isEmpty()) {
            final Image appIcon = new Image(iconPath);
            Stage dialogStage = (Stage) alert.getDialogPane().getScene().getWindow();
            dialogStage.getIcons().add(appIcon);

            if (addCustomHeaderGraphic) {
                final ImageView headerIcon = new ImageView(iconPath);
                headerIcon.setFitHeight(48); // Set size to JavaFX API recommendation.
                headerIcon.setFitWidth(48);
                alert.getDialogPane().setGraphic(headerIcon);
            }
        }
    }

    /**
     * Sets the path to the image file to be used as the title bar icon.
     * Some methods may also set this as the Alert's header graphic.
     *
     * @param icon Path to custom icon.
     */
    public static void setIconPath(String icon) {
        iconPath = icon;
    }

    /**
     * Sets the application's title. This will be used in cases where a
     * title is not provided as part of the CustomAlert's .show methods.
     * @param appTitle The CustomAlert's intended title bar text if not otherwise specified
     */
    public static void setApplicationTitle(String appTitle) { applicationTitle = appTitle;}
}