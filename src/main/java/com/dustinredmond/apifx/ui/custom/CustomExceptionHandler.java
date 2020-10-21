package com.dustinredmond.apifx.ui.custom;

import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Custom implementation of {@code java.lang.Thread.UncaughtExceptionHandler}
 * The custom implementation shows a custom Alert dialog with the Exception's
 * stack trace, as well as logs the Exception to the console.
 */
public class CustomExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Platform.runLater(() ->
            CustomAlert.showExceptionDialog(e)
        );
        log.error("Uncaught Exception ({}) on Thread: {}-{}", e.getLocalizedMessage(), t.getId(), t.getName());
    }

    private static final Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);
}
