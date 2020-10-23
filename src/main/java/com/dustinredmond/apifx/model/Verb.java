package com.dustinredmond.apifx.model;

/**
 * Represents an HTTP method
 */
public enum Verb {
    POST,
    GET,
    PUT,
    PATCH,
    DELETE,
    PATH,
    HEAD,
    TRACE,
    CONNECT,
    OPTIONS,
    UNKN;

    @Override
    public String toString() {
        if (super.toString().equals("UNKN")) {
            return "Unknown - Server Offline";
        }
        return super.toString();
    }
}
