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

import com.dustinredmond.sparkfx.AppRouteInitializer;
import com.dustinredmond.sparkfx.ServerContext;
import com.dustinredmond.sparkfx.ui.custom.CustomAlert;
import com.dustinredmond.sparkfx.ui.custom.CustomExceptionHandler;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import spark.Spark;

import static spark.Spark.port;

/**
 * The entry-point for the JavaFX application.
 * Gets the UI ready for display.
 */
public final class UI extends Application {

    public static final String APP_ICON_URL =
        UI.class.getResource("icons8-api-48.png").toExternalForm();
    public static final String APP_TITLE = "SparkFX";

    @Override
    public void stop() {
        if (ServerContext.isActive()) {
            Spark.stop();
            Spark.awaitStop();
        }
    }

    @Override
    public void start(Stage stage) {
        Thread.currentThread().setUncaughtExceptionHandler(
            new CustomExceptionHandler());
        primaryStage = stage;
        primaryStage.setTitle(APP_TITLE);
        CustomAlert.setApplicationTitle(APP_TITLE);
        CustomAlert.setIconPath(APP_ICON_URL);
        stage.getIcons().add(new Image(APP_ICON_URL));
        stage.setScene(new Scene(new Group()));
        if (!ServerContext.available(ServerContext.getPort())) {
            CustomAlert.showWarning(String.format("Cannot use port %s as it's"
                    + " already in use. Application will now exit.\n Please "
                    + "have port 8080 accessible before using %s.",
                    ServerContext.getPort(),
                    UI.APP_TITLE));
            return; // let other threads from Main finish
        }
        String prompt = "Welcome to " + APP_TITLE + "!\n\n"
                + "The embedded application server is currently not running.\n"
                + "Is it okay to start it on port "
                + ServerContext.getPort() + "?";
        if (CustomAlert.showConfirmation(prompt)) {
            port(ServerContext.getPort());
            spark.Spark.init();
            ServerContext.setActive(true);
            new Thread(new AppRouteInitializer()).start();
        }
        new RouteWindow().show();
        stage.show();
        stage.setMaximized(true);

        stage.setOnCloseRequest(e -> {
            final String promptClose = "Are you sure you wish to exit "
                + "the application?\n"
                + "This will stop any HTTP server that is currently running.";
            if (!CustomAlert.showConfirmation(promptClose)) {
                e.consume();
            }
        });
    }


    /**
     * Calls the JavaFX {@code Application.launch()} method.
     * @param args Arguments passed as command line parameters.
     */
    public void startUi(final String... args) {
        Application.launch(UI.class, args);
    }

    /**
     * Gets the application's primary Stage created at application start.
     * Developers looking to extend the UI, should operate directly on this
     * object.
     * @return The application's default parent Stage
     */
    public static Stage getPrimaryStage() {
        return UI.primaryStage;
    }

    /**
     * The JavaFX application's main stage.
     */
    private static Stage primaryStage;
}
