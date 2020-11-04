package com.dustinredmond.sparkfx.groovy;

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

import com.dustinredmond.sparkfx.model.RouteLibrary;
import com.dustinredmond.sparkfx.persistence.RouteLibraryDAO;
import groovy.lang.GroovyClassLoader;
import groovy.lang.Script;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ExceptionHandler;
import spark.Filter;
import spark.HaltException;
import spark.ModelAndView;
import spark.ResponseTransformer;
import spark.Route;
import spark.RouteGroup;
import spark.Spark;
import spark.TemplateEngine;
import spark.TemplateViewRoute;
import spark.routematch.RouteMatch;


import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.function.Consumer;

/**
 * Base Groovy Script, all run Groovy code in the application will have access
 * to this classes' methods.
 *
 */
@SuppressWarnings({"unused", "RedundantSuppression"})
public abstract class SparkScript extends Script {

    /**
     * Gets a RouteLibrary, can be called and used from Groovy code.
     * @param className RouteLibrary to get
     * @return RouteLibrary if it exists, and is enabled, otherwise null.
     */
    public Object getLibrary(final String className) {
        RouteLibrary library = new RouteLibraryDAO().findByClassName(className);
        if (library == null) {
            return null;
        }
        if (!library.isEnabled()) {
            throw new RuntimeException(String.format("Library %s is currently"
                    + " not enabled. This library may not be used until it is "
                    + "enabled.", className));
        }
        try {
            return ((Class<?>) new GroovyClassLoader()
                .parseClass(library.getCode()))
                .getDeclaredConstructor().newInstance();
        } catch (InstantiationException | NoSuchMethodException
                | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(
                    String.format("Could not load library (%s), ensure the "
                            + "class has a public no-argument constructor.",
                    library.getClassName()), e
            );
        }
    }

    public static void path(String path, RouteGroup routeGroup) {
        Spark.path(path, routeGroup);
    }
    public static void get(String path, Route route) {
        Spark.get(path, route);
    }
    public static void post(String path, Route route) {
        Spark.post(path, route);
    }
    public static void put(String path, Route route) {
        Spark.put(path, route);
    }
    public static void patch(String path, Route route) {
        Spark.patch(path, route);
    }
    public static void delete(String path, Route route) {
        Spark.delete(path, route);
    }
    public static void head(String path, Route route) {
        Spark.head(path, route);
    }
    public static void trace(String path, Route route) {
        Spark.trace(path, route);
    }
    public static void connect(String path, Route route) {
        Spark.connect(path, route);
    }
    public static void options(String path, Route route) {
        Spark.options(path, route);
    }
    public static void before(String path, Filter filter) {
        Spark.before(path, filter);
    }
    public static void before(String path, Filter... filters) { Spark.before(path, filters);}
    public static void after(String path, Filter filter) { Spark.after(path, filter); }
    public static void after(String path, Filter... filters) { Spark.after(path, filters); }
    public static void get(String path, String acceptType, Route route) { Spark.get(path, acceptType, route); }
    public static void post(String path, String acceptType, Route route) { Spark.post(path, acceptType, route); }
    public static void put(String path, String acceptType, Route route) { Spark.put(path, acceptType, route); }
    public static void patch(String path, String acceptType, Route route) { Spark.patch(path, acceptType, route); }
    public static void delete(String path, String acceptType, Route route) { Spark.delete(path, acceptType, route); }
    public static void head(String path, String acceptType, Route route) { Spark.head(path, acceptType, route); }
    public static void trace(String path, String acceptType, Route route) { Spark.trace(path, acceptType, route); }
    public static void connect(String path, String acceptType, Route route) { Spark.connect(path, acceptType, route); }
    public static void options(String path, String acceptType, Route route) { Spark.options(path, acceptType, route); }
    public static void before(Filter... filters) { Spark.before(filters); }
    public static void after(Filter... filters) { Spark.after(filters); }
    public static void before(String path, String acceptType, Filter... filters) { Spark.before(path, acceptType, filters); }
    public static void after(String path, String acceptType, Filter... filters) { Spark.after(path, acceptType, filters); }
    public static void afterAfter(String path, Filter filter) { Spark.afterAfter(path, filter); }
    public static void afterAfter(Filter filter) { Spark.afterAfter(filter); }
    public static void get(String path, TemplateViewRoute route, TemplateEngine engine) { Spark.get(path, route, engine); }
    public static void get(String path, String acceptType, TemplateViewRoute route, TemplateEngine engine) { Spark.get(path, acceptType, route, engine); }
    public static void post(String path, TemplateViewRoute route, TemplateEngine engine) { Spark.post(path, route, engine); }
    public static void post(String path, String acceptType, TemplateViewRoute route, TemplateEngine engine) { Spark.post(path, acceptType, route, engine); }
    public static void put(String path, TemplateViewRoute route, TemplateEngine engine) { Spark.put(path, route, engine); }
    public static void put(String path, String acceptType, TemplateViewRoute route, TemplateEngine engine) { Spark.put(path, acceptType, route, engine); }
    public static void delete(String path, TemplateViewRoute route, TemplateEngine engine) { Spark.delete(path, route, engine); }
    public static void delete(String path, String acceptType, TemplateViewRoute route, TemplateEngine engine) { Spark.delete(path, acceptType, route, engine); }
    public static void patch(String path, TemplateViewRoute route, TemplateEngine engine) { Spark.patch(path, route, engine); }
    public static void patch(String path, String acceptType, TemplateViewRoute route, TemplateEngine engine) { Spark.patch(path, acceptType, route, engine); }
    public static void head(String path, TemplateViewRoute route, TemplateEngine engine) { Spark.head(path, route, engine); }
    public static void head(String path, String acceptType, TemplateViewRoute route, TemplateEngine engine) { Spark.head(path, acceptType, route, engine); }
    public static void trace(String path, TemplateViewRoute route, TemplateEngine engine) { Spark.trace(path, route, engine); }
    public static void trace(String path, String acceptType, TemplateViewRoute route, TemplateEngine engine) { Spark.trace(path, acceptType, route, engine); }
    public static void connect(String path, TemplateViewRoute route, TemplateEngine engine) { Spark.connect(path, route, engine); }
    public static void connect(String path, String acceptType, TemplateViewRoute route, TemplateEngine engine) { Spark.connect(path, acceptType, route, engine); }
    public static void options(String path, TemplateViewRoute route, TemplateEngine engine) { Spark.options(path, route, engine); }
    public static void options(String path, String acceptType, TemplateViewRoute route, TemplateEngine engine) { Spark.options(path, acceptType, route, engine); }
    public static void get(String path, Route route, ResponseTransformer transformer) { Spark.get(path, route, transformer); }
    public static void get(String path, String acceptType, Route route, ResponseTransformer transformer) { Spark.get(path, acceptType, route, transformer); }
    public static void post(String path, Route route, ResponseTransformer transformer) { Spark.post(path, route, transformer); }
    public static void post(String path, String acceptType, Route route, ResponseTransformer transformer) { Spark.post(path, acceptType, route, transformer); }
    public static void put(String path, Route route, ResponseTransformer transformer) { Spark.put(path, route, transformer); }
    public static void put(String path, String acceptType, Route route, ResponseTransformer transformer) { Spark.put(path, acceptType, route, transformer); }
    public static void delete(String path, Route route, ResponseTransformer transformer) { Spark.delete(path, route, transformer); }
    public static void delete(String path, String acceptType, Route route, ResponseTransformer transformer) { Spark.delete(path, acceptType, route, transformer); }
    public static void head(String path, Route route, ResponseTransformer transformer) { Spark.head(path, route, transformer); }
    public static void head(String path, String acceptType, Route route, ResponseTransformer transformer) { Spark.head(path, acceptType, route, transformer); }
    public static void connect(String path, Route route, ResponseTransformer transformer) { Spark.connect(path, route, transformer); }
    public static void connect(String path, String acceptType, Route route, ResponseTransformer transformer) { Spark.connect(path, acceptType, route, transformer); }
    public static void trace(String path, Route route, ResponseTransformer transformer) { Spark.trace(path, route, transformer); }
    public static void trace(String path, String acceptType, Route route, ResponseTransformer transformer) { Spark.trace(path, acceptType, route, transformer); }
    public static void options(String path, Route route, ResponseTransformer transformer) { Spark.options(path, route, transformer); }
    public static void options(String path, String acceptType, Route route, ResponseTransformer transformer) { Spark.options(path, acceptType, route, transformer); }
    public static void patch(String path, Route route, ResponseTransformer transformer) { Spark.patch(path, route, transformer); }
    public static void patch(String path, String acceptType, Route route, ResponseTransformer transformer) { Spark.patch(path, acceptType, route, transformer); }
    public static boolean unmap(String path) { return Spark.unmap(path); }
    public static boolean unmap(String path, String httpMethod) { return Spark.unmap(path, httpMethod); }
    public static <T extends Exception> void exception(Class<T> exceptionClass, ExceptionHandler<? super T> handler) { Spark.exception(exceptionClass, handler); }
    public static HaltException halt() { throw Spark.halt(); }
    public static HaltException halt(int status) { throw Spark.halt(status); }
    public static HaltException halt(String body) { throw Spark.halt(body); }
    public static HaltException halt(int status, String body) { throw Spark.halt(status, body); }
    public static void ipAddress(String ipAddress) { Spark.ipAddress(ipAddress); }
    public static void defaultResponseTransformer(ResponseTransformer transformer) { Spark.defaultResponseTransformer(transformer); }
    public static void port(int port) { Spark.port(port); }
    public static int port() { return Spark.port(); }
    public static void secure(String keystoreFile, String keystorePassword, String truststoreFile, String truststorePassword) { Spark.secure(keystoreFile, keystorePassword, truststoreFile, truststorePassword); }
    public static void secure(String keystoreFile, String keystorePassword, String certAlias, String truststoreFile, String truststorePassword) { Spark.secure(keystoreFile, keystorePassword, certAlias, truststoreFile, truststorePassword); }
    public static void initExceptionHandler(Consumer<Exception> initExceptionHandler) { Spark.initExceptionHandler(initExceptionHandler); }
    public static void secure(String keystoreFile, String keystorePassword, String truststoreFile, String truststorePassword, boolean needsClientCert) { Spark.secure(keystoreFile, keystorePassword, truststoreFile, truststorePassword, needsClientCert); }
    public static void secure(String keystoreFile, String keystorePassword, String certAlias, String truststoreFile, String truststorePassword, boolean needsClientCert) { Spark.secure(keystoreFile, keystorePassword, certAlias, truststoreFile, truststorePassword, needsClientCert); }
    public static void threadPool(int maxThreads) { Spark.threadPool(maxThreads); }
    public static void threadPool(int maxThreads, int minThreads, int idleTimeoutMillis) { Spark.threadPool(maxThreads, minThreads, idleTimeoutMillis); }
    public static void staticFileLocation(String folder) { Spark.staticFileLocation(folder); }
    public static void externalStaticFileLocation(String externalFolder) { Spark.externalStaticFileLocation(externalFolder); }
    public static void awaitInitialization() { Spark.awaitInitialization(); }
    public static void stop() { Spark.stop(); }
    public static void awaitStop() { Spark.awaitStop(); }
    public static void webSocket(String path, Class<?> handler) { Spark.webSocket(path, handler); }
    public static void webSocket(String path, Object handler) { Spark.webSocket(path, handler); }
    public static void webSocketIdleTimeoutMillis(int timeoutMillis) { Spark.webSocketIdleTimeoutMillis(timeoutMillis); }
    public static void notFound(String page) { Spark.notFound(page); }
    public static void internalServerError(String page) { Spark.internalServerError(page); }
    public static void notFound(Route route) { Spark.notFound(route); }
    public static void internalServerError(Route route) { Spark.internalServerError(route); }
    public static void init() { Spark.init(); }
    public static ModelAndView modelAndView(Object model, String viewName) { return new ModelAndView(model, viewName); }
    public static List<RouteMatch> routes() { return Spark.routes(); }
    public static int activeThreadCount() { return Spark.activeThreadCount(); }

    public static final Logger logger = LoggerFactory.getLogger(SparkScript.class);

}
