package com.dustinredmond.apifx.model;

import java.util.Date;

/**
 * Represents library style code that can be called from a Route
 */
public class RouteLibrary {

    private long id;
    private String className;
    private String code;
    private Date created;
    private Date modified;
    private boolean enabled;

    public RouteLibrary(String className, String code, Date created, Date modified, boolean enabled) {
        this.className = className;
        this.code = code;
        this.created = created;
        this.modified = modified;
        this.enabled = enabled;
    }

    public RouteLibrary() { super(); }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
