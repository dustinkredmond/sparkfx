package com.dustinredmond.apifx.util;

import java.util.prefs.Preferences;

/**
 * Class to centralize getting and setting of preferences
 * Underlying implementation uses the {@code java.util.prefs.Preferences} class.
 * Values are not encrypted on the user's file system, so take care to handle
 * the encryption of sensitive preferences (e.g. SQL database connections).
 */
public class Prefs {

    public static String get(String key) {
        return preferences.get(key, "");
    }
    public static String get(String key, String defaultVal) { return preferences.get(key, defaultVal); }
    public static void put(String key, String value) {
        preferences.put(key, value);
    }
    public static void clear(String key) { preferences.remove(key); }
    public static Long getLong(String key) {
        return preferences.getLong(key, 0L);
    }
    public static void putLong(String key, Long value) {
        preferences.putLong(key, value);
    }

    private static final Preferences preferences = Preferences.userNodeForPackage(Prefs.class);
}
