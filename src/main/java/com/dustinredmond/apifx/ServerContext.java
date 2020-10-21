package com.dustinredmond.apifx;

import com.dustinredmond.apifx.util.Prefs;

/**
 * Utility class for storing the current details of the
 * server providing access to application Routes
 */
public class ServerContext {

    private static boolean active;

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
}
