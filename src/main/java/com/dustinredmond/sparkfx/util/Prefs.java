package com.dustinredmond.sparkfx.util;

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

import java.util.prefs.Preferences;

/**
 * Class to centralize getting and setting of preferences
 * Underlying implementation uses the {@code java.util.prefs.Preferences} class.
 * Values are not encrypted on the user's file system, so take care to handle
 * the encryption of sensitive preferences (e.g. SQL database connections).
 */
public final class Prefs {

    private Prefs() {
        super();
    }

    public static String get(final String key) {
        return PREF.get(key, "");
    }

    public static String get(final String key, final String defaultVal) {
        return PREF.get(key, defaultVal);
    }

    public static void put(final String key, final String value) {
        PREF.put(key, value);
    }

    public static void clear(final String key) {
        PREF.remove(key);
    }

    public static Long getLong(final String key) {
        return PREF.getLong(key, 0L);
    }

    public static void putLong(final String key, final Long value) {
        PREF.putLong(key, value);
    }

    private static final Preferences PREF =
        Preferences.userNodeForPackage(Prefs.class);
}
