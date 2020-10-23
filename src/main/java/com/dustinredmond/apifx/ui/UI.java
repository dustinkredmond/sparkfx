package com.dustinredmond.apifx.ui;

import com.dustinredmond.apifx.AppRouteInitializer;
import com.dustinredmond.apifx.ServerContext;
import com.dustinredmond.apifx.ui.custom.CustomAlert;
import com.dustinredmond.apifx.ui.custom.CustomExceptionHandler;
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
public class UI extends Application {

    public static final String APP_ICON_URL = UI.class.getResource("icons8-api-48.png").toExternalForm();
    public static final String APP_TITLE = "FxAPI Client";

    @Override
    public void stop() {
        if (ServerContext.isActive()) {
            Spark.stop();
            Spark.awaitStop();
        }
    }

    @Override
    public void start(Stage stage) {
        Thread.currentThread().setUncaughtExceptionHandler(new CustomExceptionHandler());
        primaryStage = stage;
        primaryStage.setTitle(APP_TITLE);
        CustomAlert.setApplicationTitle(APP_TITLE);
        CustomAlert.setIconPath(APP_ICON_URL);
        stage.getIcons().add(new Image(APP_ICON_URL));
        stage.setScene(new Scene(new Group()));
        String prompt = "Welcome to " + APP_TITLE + "! The embedded application server is currently not running. " +
                "Is it okay to start it on port " + ServerContext.getPort() + "?";
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
            final String promptClose = "Are you sure you wish to exit the application? This will stop " +
                    "any HTTP server that is currently running.";
            if (!CustomAlert.showConfirmation(promptClose)) {
                e.consume();
            }
        });
    }


    /**
     * Calls the JavaFX {@code Application.launch()} method
     * @param args Arguments passed as command line parameters.
     */
    public void startUi(String... args) {
        Application.launch(UI.class, args);
    }

    /**
     * Gets the application's primary Stage created at application start.
     * Developers looking to extend the UI, should operate directly on this
     * object.
     * @return The application's default parent Stage
     */
    public static Stage getPrimaryStage() { return UI.primaryStage; }

    private static Stage primaryStage;
}
