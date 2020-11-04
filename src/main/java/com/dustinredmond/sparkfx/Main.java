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

import com.dustinredmond.sparkfx.persistence.DatabaseBootstrap;
import com.dustinredmond.sparkfx.ui.UI;
import com.dustinredmond.sparkfx.ui.custom.CustomExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.port;

/**
 * Application entry point.
 */
public final class Main {

    private Main() { super(); }

    public static void main(final String[] args) {
        parseArgs(args);

        // Make sure to set for each Thread in our application
        Thread.currentThread().setUncaughtExceptionHandler(new CustomExceptionHandler());
        Thread t = new Thread(new DatabaseBootstrap());
        t.setUncaughtExceptionHandler(new CustomExceptionHandler());
        t.start();
        while (true) {
            // Wait for DB tables to be created before allowing user input
            if (!t.isAlive()) {
                if (!ServerContext.isHeadless()) {
                    new UI().startUi(args);
                } else {
                    port(ServerContext.getPort());
                    spark.Spark.init();
                    ServerContext.setActive(true);
                    new AppRouteInitializer().run();
                }
                break;
            }
        }
    }

    private static void parseArgs(final String[] args) {
        try {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("--headless")) {
                    ServerContext.setHeadless(true);
                    log.info("Running in headless mode");
                }
                if (args[i].equals("--port")) {
                    ServerContext.setPort(Integer.parseInt(args[i+1]));
                    log.info("Running on port {}", ServerContext.getPort());
                }
            }
        } catch (Exception e) {
            System.err.println("Malformed arguments received. Please check the documentation.");
            System.exit(-1);
        }
    }

    private static final Logger log = LoggerFactory.getLogger(Main.class);
}
