package com.dustinredmond.apifx;

import com.dustinredmond.apifx.groovy.GroovyEnvironment;
import com.dustinredmond.apifx.persistence.RouteDAO;
import com.dustinredmond.apifx.persistence.StartupScriptDAO;
import com.dustinredmond.apifx.ui.RouteWindow;
import com.dustinredmond.apifx.ui.custom.CustomAlert;
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
                        Platform.runLater(() -> CustomAlert.showExceptionDialog(e,
                                "Exception occurred when executing Startup Script.\n" +
                                        "Description: " + script.getDescription()));
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
                    Platform.runLater(() -> CustomAlert.showExceptionDialog(e,
                            "Exception occurred when creating route: "+ route.getUrl()));
            }
        });

        Platform.runLater(() -> {
            CustomAlert.showInfo("Server Startup complete",
                    String.format(" - Startup Scripts executed successfully:  %s\n" +
                                    " - Startup Scripts executed unsuccessfully: %s\n\n" +
                                    " - Routes enabled successfully:  %s\n" +
                                    " - Routes enabled unsuccessfully: %s", startupSuccess.get(), startupFailure.get(),
                            routesSuccess.get(), routesFailure.get()));
            RouteWindow.refreshTable();
        });
    }

    private static final Logger logger = LoggerFactory.getLogger(AppRouteInitializer.class);
}
