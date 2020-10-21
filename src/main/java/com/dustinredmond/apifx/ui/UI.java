package com.dustinredmond.apifx.ui;

import com.dustinredmond.apifx.AppRouteInitializer;
import com.dustinredmond.apifx.ServerContext;
import com.dustinredmond.apifx.ui.custom.CustomAlert;
import com.dustinredmond.apifx.ui.custom.CustomExceptionHandler;
import com.dustinredmond.apifx.ui.custom.CustomMenuBar;
import com.dustinredmond.apifx.ui.custom.CustomTrayIcon;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.Optional;

import static spark.Spark.port;

/**
 * The entry-point for the JavaFX application.
 * Gets the UI ready for display.
 */
public class UI extends Application {

    public static final String APP_ICON_URL = UI.class.getResource("icons8-api-48.png").toExternalForm();
    public static final String APP_TITLE = "FxAPI Client";

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

        addTrayIcon(stage);

        stage.setOnCloseRequest(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setGraphic(new ImageView(UI.APP_ICON_URL));
            ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().addAll(new Image(UI.APP_ICON_URL));
            alert.getButtonTypes().clear();
            ButtonType typeExit = new ButtonType("Exit Program");
            ButtonType typeMinimize = new ButtonType("Minimize to Tray");
            alert.getButtonTypes().addAll(typeMinimize, typeExit);
            alert.setTitle(UI.APP_TITLE);
            alert.setHeaderText("");
            alert.setContentText("Do you wish to exit the application and stop any running webserver, or" +
                    " minimize the application to the system tray?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get().equals(typeExit)) {
                Platform.exit();
                spark.Spark.stop();
                spark.Spark.awaitStop();
                System.exit(0);
            }
        });
    }

    private void addTrayIcon(Stage stage) {
        CustomTrayIcon icon = new CustomTrayIcon(stage, getClass().getResource("icons8-api-48.png"));
        icon.setTrayIconTooltip(UI.APP_TITLE);
        icon.setApplicationTitle(UI.APP_TITLE);
        icon.show();
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
