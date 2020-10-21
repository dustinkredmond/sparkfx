package com.dustinredmond.apifx.ui.custom;

import com.dustinredmond.apifx.ServerContext;
import com.dustinredmond.apifx.ui.PreferencesWindow;
import com.dustinredmond.apifx.ui.RouteLibraryWindow;
import com.dustinredmond.apifx.ui.RouteWindow;
import com.dustinredmond.apifx.ui.StartupScriptWindow;
import javafx.application.Platform;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

/**
 * Custom global JavaFX MenuBar that is displayed throughout the application
 */
public class CustomMenuBar extends MenuBar {

    public CustomMenuBar(RouteWindow window) {

        Menu menuFile = new Menu("File");
        MenuItem miRefresh = new MenuItem("Refresh Tables");
        miRefresh.setOnAction(e -> {
            RouteWindow.refreshTable();
            RouteLibraryWindow.refreshTableView();
            StartupScriptWindow.refreshTable();
        });

        MenuItem miExport = new MenuItem("Export Route...");
        miExport.setOnAction(e -> controller.exportRoute(window.getTable()));

        MenuItem miExportLib = new MenuItem("Export Route Library...");
        miExportLib.setOnAction(e -> controller.exportRouteLibrary(RouteLibraryWindow.getTable()));

        MenuItem miExit = new MenuItem("Exit program");
        miExit.setOnAction(e -> {
            Platform.exit();
            System.exit(0);
        });
        menuFile.getItems().addAll(miRefresh, miExport, miExportLib, new SeparatorMenuItem(), miExit);

        Menu menuRoutes = new Menu("Routes");
        MenuItem miOverview = new MenuItem("Routes Overview");
        MenuItem miRouteLibraries = new MenuItem("Route Libraries");
        miRouteLibraries.setOnAction(e -> new RouteLibraryWindow().show(window));
        miOverview.setOnAction(e -> window.show());
        menuRoutes.getItems().addAll(miOverview, miRouteLibraries);


        Menu menuServer = new Menu("Server");
        MenuItem miStartup = new MenuItem("Startup Scripts");
        miStartup.setOnAction(e -> new StartupScriptWindow().show(window));
        miStart.setDisable(ServerContext.isActive());
        miStop.setDisable(!ServerContext.isActive());
        miStart.setOnAction(e -> controller.startServer(miStart, miStop));
        miStop.setOnAction(e -> controller.stopServer(miStart, miStop));
        menuServer.getItems().addAll(miStartup, new SeparatorMenuItem(), miStart, miStop);


        Menu menuOptions = new Menu("Options");
        MenuItem miPrefs = new MenuItem("Preferences");
        miPrefs.setOnAction(e -> new PreferencesWindow().show(window));
        menuOptions.getItems().add(miPrefs);

        Menu menuHelp = new Menu("Help");
        MenuItem miAbout = new MenuItem("About this program");
        miAbout.setOnAction(e -> controller.showAbout());
        menuHelp.getItems().add(miAbout);

        this.getMenus().addAll(menuFile, menuRoutes, menuServer, menuOptions, menuHelp);
    }

    private final MenuItem miStart = new MenuItem("Start Server");
    private final MenuItem miStop = new MenuItem("Stop Server");
    private static final CustomMenuBarController controller = new CustomMenuBarController();
    public void enableServerStartItem(boolean enable) {
        miStart.setDisable(!enable);
    }
    public void enableServerStopItem(boolean enable) {
        miStop.setDisable(!enable);
    }
}
