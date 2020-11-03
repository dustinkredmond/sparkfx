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
        log.error("Handling uncaught exception | Thread: {} |Message : {}", t.getName(), e.getMessage());
        if (ServerContext.isHeadless()) {
            return; // don't show dialogs, just log it
        }

        if (Platform.isFxApplicationThread()) {
            CustomAlert.showExceptionDialog(e);
        } else {
            Platform.runLater(() -> CustomAlert.showExceptionDialog(e));
            e.printStackTrace();
        }
    }

    private static final Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);
}
