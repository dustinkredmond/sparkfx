package com.dustinredmond.apifx.model;

import java.util.Date;

/**
 * Represents an API endpoint or Spark Route
 */
public class Route {

    private long id;
    private Date created;
    private String url;
    private String code;
    boolean enabled;

    public Route() {
        super();
    }

    public Route(long id, String url, String code, Date created, boolean enabled) {
        this.id = id;
        this.url = url;
        this.code = code;
        this.created = created;
        this.enabled = enabled;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
