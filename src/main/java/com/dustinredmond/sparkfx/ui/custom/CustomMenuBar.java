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

import com.dustinredmond.sparkfx.ServerContext;
import com.dustinredmond.sparkfx.ui.PreferencesWindow;
import com.dustinredmond.sparkfx.ui.RouteLibraryWindow;
import com.dustinredmond.sparkfx.ui.RouteWindow;
import com.dustinredmond.sparkfx.ui.StartupScriptWindow;
import javafx.application.Platform;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

/**
 * Custom global JavaFX MenuBar that is displayed throughout the application.
 */
public final class CustomMenuBar extends MenuBar {

    public CustomMenuBar(final RouteWindow window) {

        Menu menuFile = new Menu("File");
        MenuItem miRefresh = new MenuItem("Refresh Tables");
        miRefresh.setOnAction(e -> {
            RouteWindow.refreshTable();
            RouteLibraryWindow.refreshTableView();
            StartupScriptWindow.refreshTable();
        });

        MenuItem miExport = new MenuItem("Export Route...");
        miExport.setOnAction(e ->
            controller.exportRoute(window.getTable()));

        MenuItem miExportLib = new MenuItem("Export Route Library...");
        miExportLib.setOnAction(e ->
            controller.exportRouteLibrary(RouteLibraryWindow.getTable()));

        MenuItem miExit = new MenuItem("Exit program");
        miExit.setOnAction(e -> Platform.exit());
        menuFile.getItems().addAll(
            miRefresh, miExport, miExportLib, new SeparatorMenuItem(), miExit);

        Menu menuRoutes = new Menu("Routes");
        MenuItem miOverview = new MenuItem("Routes Overview");
        MenuItem miRouteLibraries = new MenuItem("Route Libraries");
        miRouteLibraries.setOnAction(e ->
            new RouteLibraryWindow().show(window));
        miOverview.setOnAction(e -> window.show());
        menuRoutes.getItems().addAll(miOverview, miRouteLibraries);


        Menu menuServer = new Menu("Server");
        MenuItem miStartup = new MenuItem("Startup Scripts");
        miStartup.setOnAction(e -> new StartupScriptWindow().show(window));
        miStart.setDisable(ServerContext.isActive());
        miStop.setDisable(!ServerContext.isActive());
        miStart.setOnAction(e ->
            controller.startServer(miStart, miStop));
        miStop.setOnAction(e ->
            controller.stopServer(miStart, miStop));
        menuServer.getItems().addAll(
            miStartup, new SeparatorMenuItem(), miStart, miStop);


        Menu menuOptions = new Menu("Options");
        MenuItem miPrefs = new MenuItem("Preferences");
        miPrefs.setOnAction(e -> new PreferencesWindow().show(window));
        menuOptions.getItems().add(miPrefs);

        Menu menuHelp = new Menu("Help");
        MenuItem miAbout = new MenuItem("About this program");
        miAbout.setOnAction(e -> controller.showAbout());
        menuHelp.getItems().add(miAbout);

        this.getMenus().addAll(
            menuFile, menuRoutes, menuServer, menuOptions, menuHelp);
    }

    private final MenuItem miStart = new MenuItem("Start Server");
    private final MenuItem miStop = new MenuItem("Stop Server");
    private final CustomMenuBarController controller =
        new CustomMenuBarController();
    public void enableServerStartItem(final boolean enable) {
        miStart.setDisable(!enable);
    }
    public void enableServerStopItem(final boolean enable) {
        miStop.setDisable(!enable);
    }
}
