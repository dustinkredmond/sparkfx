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

import com.dustinredmond.sparkfx.util.Prefs;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;

/**
 * Utility class for storing the current details of the
 * server providing access to application Routes
 */
public class ServerContext {

    private static boolean active;
    private static boolean headless;

    /**
     * Returns the embedded HTTP server's status.
     * @return True if the server is running.
     */
    public static boolean isActive() {
        return active;
    }

    /**
     * Should pass true when any Spark code causes the embedded server
     * to be started.
     * @param active The server's active status
     */
    public static void setActive(boolean active) {
        ServerContext.active = active;
    }

    public static boolean isHeadless() { return ServerContext.headless; }
    public static void setHeadless(boolean enabled) { ServerContext.headless = enabled; }

    /**
     * Returns the preferred, or default port of the server.
     * @return The port as set by the user, or 8080 if not set.
     */
    public static int getPort() {
        Long appPort = Prefs.getLong("appPort");
        if (appPort > 0) {
            return appPort.intValue();
        } else {
            return 8080; // Default if not set in preferences
        }
    }

    /**
     * Returns a String displaying server status and port.
     * @return A String description of the current server context
     */
    public static String getDescription() {
        return String.format("Server: (Port: %s, Running: %s)",
                getPort(), active ? "Yes" : "No");
    }

    /**
     * Returns true if the port is not in use and is available for use
     * @param port The port to check
     * @return True if the port is available and can be bound
     */
    public static boolean available(int port) {
        try (ServerSocket ss = new ServerSocket(port); DatagramSocket ds = new DatagramSocket(port)) {
            ss.setReuseAddress(true);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException ignored) {
          return false;
        }
    }
}
