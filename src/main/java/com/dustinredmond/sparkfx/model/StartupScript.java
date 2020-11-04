package com.dustinredmond.sparkfx.model;

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

/**
 * Represents a script that can be executed during server startup
 * or run on demand.
 */
public final class StartupScript {

    private long id;
    private String description;
    private String code;
    private boolean enabled;

    public StartupScript(final long id, final String description,
        final String code, final boolean enabled) {
        this.id = id;
        this.description = description;
        this.code = code;
        this.enabled = enabled;
    }

    public StartupScript(final String description, final String code,
        final boolean enabled) {
        this.description = description;
        this.code = code;
        this.enabled = enabled;
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
}
