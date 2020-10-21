package com.dustinredmond.apifx.ui.custom;

import com.dustinredmond.apifx.AppRouteInitializer;
import com.dustinredmond.apifx.ServerContext;
import com.dustinredmond.apifx.model.Route;
import com.dustinredmond.apifx.model.RouteLibrary;
import com.dustinredmond.apifx.ui.UI;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;

import static spark.Spark.port;

public class CustomMenuBarController {

    /**
     * Exports a Route's code to a chosen file on the user's filesystem.
     * @param table TableView containing selected Route
     */
    public void exportRoute(TableView<Route> table) {
        if (table.getSelectionModel().isEmpty()) {
            CustomAlert.showWarning("Please select a route from the table first.");
            return;
        }

        Route route = table.getSelectionModel().getSelectedItem();
        String fileName = route.getUrl().replaceAll("/", "-");
        if (fileName.startsWith("-")) {
            fileName = fileName.substring(1);
        }
        File file = getFile(fileName);
        if (file != null) {
            try {
                Files.write(Paths.get(file.getAbsolutePath()), route.getCode().getBytes(), StandardOpenOption.CREATE_NEW);
            } catch (IOException ex) {
                CustomAlert.showExceptionDialog(ex, "Unable to write file");
            }
        } // if file is null, user closed save dialog
    }

    public void showAbout() {
        Stage stage = new CustomStage();
        stage.setTitle(UI.APP_TITLE);
        TextArea taLicense = new TextArea(LICENSE_TEXT);
        taLicense.setEditable(false);
        String version = getClass().getPackage().getImplementationVersion();

        Label linkLabel = new Label("Icons provided by Icons8 - ");
        Hyperlink link = new Hyperlink("https://icons8.com/");
        link.setOnAction(e -> browseLink(link.getText()));
        HBox linkBox = new HBox(linkLabel, link);
        linkBox.setAlignment(Pos.CENTER_LEFT);

        VBox vBox = new VBox(5, new Label(UI.APP_TITLE+"\n"+"Version: "+version),
                taLicense,
                linkBox);
        stage.setScene(new Scene(new CustomGrid(vBox)));
        stage.setResizable(false);
        stage.showAndWait();
    }

    private void browseLink(String linkText) {
        try { Desktop.getDesktop().browse(new URI(linkText)); } catch (Exception ignored) {}
    }

    private static final String LICENSE_TEXT = " Copyright \u00A9 "+ LocalDate.now().getYear() +" " +
            "Dustin K. Redmond (dustin@dustinredmond.com)\n" +
            "\n" +
            "   Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
            "   you may not use this file except in compliance with the License.\n" +
            "   You may obtain a copy of the License at\n" +
            "\n" +
            "       http://www.apache.org/licenses/LICENSE-2.0\n" +
            "\n" +
            "   Unless required by applicable law or agreed to in writing, software\n" +
            "   distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
            "   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
            "   See the License for the specific language governing permissions and\n" +
            "   limitations under the License.";

    public void startServer(MenuItem miStart, MenuItem miStop) {
        log.debug("Service started on port {}", ServerContext.getPort());
        ServerContext.setActive(true);
        UI.getPrimaryStage().setTitle(UI.APP_TITLE + " - " + ServerContext.getDescription());
        port(ServerContext.getPort());
        spark.Spark.init();
        new Thread(new AppRouteInitializer()).start();
        miStart.setDisable(true);
        miStop.setDisable(false);
    }

    public void stopServer(MenuItem miStart, MenuItem miStop) {
        spark.Spark.stop();
        ServerContext.setActive(false);
        UI.getPrimaryStage().setTitle(UI.APP_TITLE + " - " + ServerContext.getDescription());
        miStart.setDisable(false);
        miStop.setDisable(true);
    }

    /**
     * Exports the code from a selected RouteLibrary to the user's filesystem.
     * @param table TableView containing selected Route
     */
    public void exportRouteLibrary(TableView<RouteLibrary> table) {
        if (table.getSelectionModel().isEmpty()) {
            CustomAlert.showWarning("Please first select a Route Library from the table.");
            return;
        }

        RouteLibrary lib = table.getSelectionModel().getSelectedItem();
        File file = getFile(lib.getClassName());
        if (file != null) {
            try {
                Files.write(Paths.get(file.getAbsolutePath()), lib.getCode().getBytes(), StandardOpenOption.CREATE_NEW);
            } catch (IOException ex) {
                CustomAlert.showExceptionDialog(ex, "Unable to write file");
            }
        } // if file is null, user closed save dialog
    }

    private File getFile(String initFileName) {
        FileChooser fc = new FileChooser();
        fc.setInitialFileName(initFileName);
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Groovy Source Code File", "*.groovy"),
                new FileChooser.ExtensionFilter("Java Source Code File", "*.java"),
                new FileChooser.ExtensionFilter("All files", "*.*"));
        return fc.showSaveDialog(null);
    }

    private static final Logger log = LoggerFactory.getLogger(CustomMenuBarController.class);
}
