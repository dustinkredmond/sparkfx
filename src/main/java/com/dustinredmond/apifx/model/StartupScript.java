package com.dustinredmond.apifx.model;

/**
 * Represents a script that can be executed during server startup
 * or run on demand.
 */
public class StartupScript {

    private long id;
    private String description;
    private String code;
    private boolean enabled;

    public StartupScript(long id, String description, String code, boolean enabled) {
        this.id = id;
        this.description = description;
        this.code = code;
        this.enabled = enabled;
    }

    public StartupScript(String description, String code, boolean enabled) {
        this.description = description;
        this.code = code;
        this.enabled = enabled;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
