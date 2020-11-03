package com.dustinredmond.sparkfx;

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

import com.dustinredmond.sparkfx.groovy.GroovyEnvironment;
import com.dustinredmond.sparkfx.persistence.RouteDAO;
import com.dustinredmond.sparkfx.persistence.StartupScriptDAO;
import com.dustinredmond.sparkfx.ui.RouteWindow;
import com.dustinredmond.sparkfx.ui.custom.CustomAlert;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * Runnable used to initialize the StartupScripts and Routes of the application.
 * Will cause the embedded Jetty server to start if Routes or StartupScripts are
 * run. After calling run() on this class, a developer should be sure to set the
 * appropriate {@code com.dustinredmond.apifx.ServerContext} variables.
 */
public class AppRouteInitializer implements Runnable {

    @Override
    public void run() {

        AtomicInteger startupSuccess = new AtomicInteger(0);
        AtomicInteger startupFailure = new AtomicInteger(0);
        new StartupScriptDAO().findAll().forEach(script -> {
            if (script.isEnabled()) {
                try {
                    GroovyEnvironment.getInstance().evaluate(script.getCode());
                    startupSuccess.incrementAndGet();
                } catch (Exception e) {
                    startupFailure.incrementAndGet();
                    if (!ServerContext.isHeadless()) {
                        Platform.runLater(() -> CustomAlert.showExceptionDialog(e,
                                "Exception occurred when executing Startup Script.\n" +
                                        "Description: " + script.getDescription()));
                    } else {
                        e.printStackTrace();
                    }
                }
            }
        });

        AtomicInteger routesSuccess = new AtomicInteger(0);
        AtomicInteger routesFailure = new AtomicInteger(0);

        new RouteDAO().findAll().forEach(route -> {
            if (!route.isEnabled()) {
                return;
            }
            try {
                GroovyEnvironment.getInstance().evaluate(route.getCode());
                routesSuccess.incrementAndGet();
                logger.info("Enabled Route: {}", route.getUrl());
            } catch (Exception e) {
                routesFailure.incrementAndGet();
                if (ServerContext.isHeadless()) {
                    e.printStackTrace();
                } else {
                    Platform.runLater(() -> CustomAlert.showExceptionDialog(e,
                            "Exception occurred when creating route: "+ route.getUrl()));
                }
            }
        });

        output(startupSuccess.get(), startupFailure.get(), routesSuccess.get(), routesFailure.get());
        if (!ServerContext.isHeadless()) {
            RouteWindow.refreshTable();
        }
    }


    private void output(int startupSuccess, int startupFailure, int routesSuccess, int routesFailure) {
        logger.info("Server Startup complete");
        logger.info("Startup Scripts Executed (Success {} | Failure {})", startupSuccess, startupFailure);
        logger.info("Routes Enabled (Success {} | Failure {})", routesSuccess, routesFailure);
        if (!ServerContext.isHeadless()) {
            Platform.runLater(() -> CustomAlert.showInfo("Server Startup complete", String.format("- Startup Scripts executed successfully: %s\n" +
                    "- Startup Scripts executed unsuccessfully: %s\n\n" +
                    "- Routes enabled successfully: %s\n" +
                    "- Routes enabled unsuccessfully: %s", startupSuccess, startupFailure, routesSuccess, routesFailure)));
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(AppRouteInitializer.class);
}
