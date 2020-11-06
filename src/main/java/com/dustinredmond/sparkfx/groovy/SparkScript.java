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
import com.dustinredmond.sparkfx.util.SparkMethod;
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

    // Begin methods implemented in spark framework
    // These should be annotated with @SparkMethod
    // to further aid in recognition

    @SparkMethod
    public static void path(final String path, final RouteGroup routeGroup) {
        Spark.path(path, routeGroup);
    }

    @SparkMethod
    public static void get(final String path, final Route route) {
        Spark.get(path, route);
    }

    @SparkMethod
    public static void post(final String path, final Route route) {
        Spark.post(path, route);
    }

    @SparkMethod
    public static void put(final String path, final Route route) {
        Spark.put(path, route);
    }

    @SparkMethod
    public static void patch(final String path, final Route route) {
        Spark.patch(path, route);
    }

    @SparkMethod
    public static void delete(final String path, final Route route) {
        Spark.delete(path, route);
    }

    @SparkMethod
    public static void head(final String path, final Route route) {
        Spark.head(path, route);
    }

    @SparkMethod
    public static void trace(final String path, final Route route) {
        Spark.trace(path, route);
    }

    @SparkMethod
    public static void connect(final String path, final Route route) {
        Spark.connect(path, route);
    }

    @SparkMethod
    public static void options(final String path, final Route route) {
        Spark.options(path, route);
    }

    @SparkMethod
    public static void before(final String path, final Filter filter) {
        Spark.before(path, filter);
    }

    @SparkMethod
    public static void before(final String path, final Filter... filters) {
        Spark.before(path, filters);
    }

    @SparkMethod
    public static void after(final String path, final Filter filter) {
        Spark.after(path, filter);
    }

    @SparkMethod
    public static void after(final String path, final Filter... filters) {
        Spark.after(path, filters);
    }

    @SparkMethod
    public static void get(final String path,
        final String acceptType,
        final Route route) {
        Spark.get(path, acceptType, route);
    }

    @SparkMethod
    public static void post(final String path,
        final String acceptType,
        final Route route) {
        Spark.post(path, acceptType, route);
    }

    @SparkMethod
    public static void put(final String path,
        final String acceptType,
        final Route route) {
        Spark.put(path, acceptType, route);
    }

    @SparkMethod
    public static void patch(final String path,
        final String acceptType,
        final Route route) {
        Spark.patch(path, acceptType, route);
    }

    @SparkMethod
    public static void delete(final String path,
        final String acceptType,
        final Route route) {
        Spark.delete(path, acceptType, route);
    }

    @SparkMethod
    public static void head(final String path,
        final String acceptType,
        final Route route) {
        Spark.head(path, acceptType, route);
    }

    @SparkMethod
    public static void trace(final String path,
        final String acceptType,
        final Route route) {
        Spark.trace(path, acceptType, route);
    }

    @SparkMethod
    public static void connect(final String path,
        final String acceptType,
        final Route route) {
        Spark.connect(path, acceptType, route);
    }

    @SparkMethod
    public static void options(final String path,
        final String acceptType,
        final Route route) {
        Spark.options(path, acceptType, route);
    }

    @SparkMethod
    public static void before(final Filter... filters) {
        Spark.before(filters);
    }

    @SparkMethod
    public static void after(final Filter... filters) {
        Spark.after(filters);
    }

    @SparkMethod
    public static void before(final String path,
        final String acceptType,
        final Filter... filters) {
        Spark.before(path, acceptType, filters);
    }

    @SparkMethod
    public static void after(final String path,
        final String acceptType,
        final Filter... filters) {
        Spark.after(path, acceptType, filters);
    }

    @SparkMethod
    public static void afterAfter(final String path,
        final Filter filter) {
        Spark.afterAfter(path, filter);
    }

    @SparkMethod
    public static void afterAfter(final Filter filter) {
        Spark.afterAfter(filter);
    }

    @SparkMethod
    public static void get(final String path,
        final TemplateViewRoute route,
        final TemplateEngine engine) {
        Spark.get(path, route, engine);
    }

    @SparkMethod
    public static void get(final String path,
        final String acceptType,
        final TemplateViewRoute route,
        final TemplateEngine engine) {
        Spark.get(path, acceptType, route, engine);
    }

    @SparkMethod
    public static void post(final String path,
        final TemplateViewRoute route,
        final TemplateEngine engine) {
        Spark.post(path, route, engine);
    }

    @SparkMethod
    public static void post(final String path,
        final String acceptType,
        final TemplateViewRoute route,
        final TemplateEngine engine) {
        Spark.post(path, acceptType, route, engine);
    }

    @SparkMethod
    public static void put(final String path,
        final TemplateViewRoute route,
        final TemplateEngine engine) {
        Spark.put(path, route, engine);
    }

    @SparkMethod
    public static void put(final String path,
        final String acceptType,
        final TemplateViewRoute route,
        final TemplateEngine engine) {
        Spark.put(path, acceptType, route, engine);
    }

    @SparkMethod
    public static void delete(final String path,
        final TemplateViewRoute route,
        final TemplateEngine engine) {
        Spark.delete(path, route, engine);
    }

    @SparkMethod
    public static void delete(final String path,
        final String acceptType,
        final TemplateViewRoute route,
        final TemplateEngine engine) {
        Spark.delete(path, acceptType, route, engine);
    }

    @SparkMethod
    public static void patch(final String path,
        final TemplateViewRoute route,
        final TemplateEngine engine) {
        Spark.patch(path, route, engine);
    }

    @SparkMethod
    public static void patch(final String path,
        final String acceptType,
        final TemplateViewRoute route,
        final TemplateEngine engine) {
        Spark.patch(path, acceptType, route, engine);
    }

    @SparkMethod
    public static void head(final String path,
        final TemplateViewRoute route,
        final TemplateEngine engine) {
        Spark.head(path, route, engine);
    }

    @SparkMethod
    public static void head(final String path,
        final String acceptType,
        final TemplateViewRoute route,
        final TemplateEngine engine) {
        Spark.head(path, acceptType, route, engine);
    }

    @SparkMethod
    public static void trace(final String path,
        final TemplateViewRoute route,
        final TemplateEngine engine) {
        Spark.trace(path, route, engine);
    }

    @SparkMethod
    public static void trace(final String path,
        final String acceptType,
        final TemplateViewRoute route,
        final TemplateEngine engine) {
        Spark.trace(path, acceptType, route, engine);
    }

    @SparkMethod
    public static void connect(final String path,
        final TemplateViewRoute route,
        final TemplateEngine engine) {
        Spark.connect(path, route, engine);
    }

    @SparkMethod
    public static void connect(final String path,
        final String acceptType,
        final TemplateViewRoute route,
        final TemplateEngine engine) {
        Spark.connect(path, acceptType, route, engine);
    }

    @SparkMethod
    public static void options(final String path,
        final TemplateViewRoute route,
        final TemplateEngine engine) {
        Spark.options(path, route, engine);
    }

    @SparkMethod
    public static void options(final String path,
        final String acceptType,
        final TemplateViewRoute route,
        final TemplateEngine engine) {
        Spark.options(path, acceptType, route, engine);
    }

    @SparkMethod
    public static void get(final String path,
        final Route route,
        final ResponseTransformer transformer) {
        Spark.get(path, route, transformer);
    }

    @SparkMethod
    public static void get(final String path,
        final String acceptType,
        final Route route,
        final ResponseTransformer transformer) {
        Spark.get(path, acceptType, route, transformer);
    }

    @SparkMethod
    public static void post(final String path,
        final Route route,
        final ResponseTransformer transformer) {
        Spark.post(path, route, transformer);
    }

    @SparkMethod
    public static void post(final String path,
        final String acceptType,
        final Route route,
        final ResponseTransformer transformer) {
        Spark.post(path, acceptType, route, transformer);
    }

    @SparkMethod
    public static void put(final String path,
        final Route route,
        final ResponseTransformer transformer) {
        Spark.put(path, route, transformer);
    }

    @SparkMethod
    public static void put(final String path,
        final String acceptType,
        final Route route,
        final ResponseTransformer transformer) {
        Spark.put(path, acceptType, route, transformer);
    }

    @SparkMethod
    public static void delete(final String path,
        final Route route,
        final ResponseTransformer transformer) {
        Spark.delete(path, route, transformer);
    }

    @SparkMethod
    public static void delete(final String path,
        final String acceptType,
        final Route route,
        final ResponseTransformer transformer) {
        Spark.delete(path, acceptType, route, transformer);
    }

    @SparkMethod
    public static void head(final String path,
        final Route route,
        final ResponseTransformer transformer) {
        Spark.head(path, route, transformer);
    }

    @SparkMethod
    public static void head(final String path,
        final String acceptType,
        final Route route,
        final ResponseTransformer transformer) {
        Spark.head(path, acceptType, route, transformer);
    }

    @SparkMethod
    public static void connect(final String path,
        final Route route,
        final ResponseTransformer transformer) {
        Spark.connect(path, route, transformer);
    }

    @SparkMethod
    public static void connect(final String path,
        final String acceptType,
        final Route route,
        final ResponseTransformer transformer) {
        Spark.connect(path, acceptType, route, transformer);
    }

    @SparkMethod
    public static void trace(final String path,
        final Route route,
        final ResponseTransformer transformer) {
        Spark.trace(path, route, transformer);
    }

    @SparkMethod
    public static void trace(final String path,
        final String acceptType,
        final Route route,
        final ResponseTransformer transformer) {
        Spark.trace(path, acceptType, route, transformer);
    }

    @SparkMethod
    public static void options(final String path,
        final Route route,
        final ResponseTransformer transformer) {
        Spark.options(path, route, transformer);
    }

    @SparkMethod
    public static void options(final String path,
        final String acceptType,
        final Route route,
        final ResponseTransformer transformer) {
        Spark.options(path, acceptType, route, transformer);
    }

    @SparkMethod
    public static void patch(final String path,
        final Route route,
        final ResponseTransformer transformer) {
        Spark.patch(path, route, transformer);
    }

    @SparkMethod
    public static void patch(final String path,
        final String acceptType,
        final Route route,
        final ResponseTransformer transformer) {
        Spark.patch(path, acceptType, route, transformer);
    }

    @SparkMethod
    public static boolean unmap(final String path) {
        return Spark.unmap(path);
    }

    @SparkMethod
    public static boolean unmap(final String path, final String httpMethod) {
        return Spark.unmap(path, httpMethod);
    }

    @SparkMethod
    public static <T extends Exception> void exception(
        final Class<T> exceptionClass,
        final ExceptionHandler<? super T> handler) {
        Spark.exception(exceptionClass, handler);
    }

    @SparkMethod
    public static HaltException halt() {
        throw Spark.halt();
    }

    @SparkMethod
    public static HaltException halt(final int status) {
        throw Spark.halt(status);
    }

    @SparkMethod
    public static HaltException halt(final String body) {
        throw Spark.halt(body);
    }

    @SparkMethod
    public static HaltException halt(final int status,
        final String body) {
        throw Spark.halt(status, body);
    }

    @SparkMethod
    public static void ipAddress(final String ipAddress) {
        Spark.ipAddress(ipAddress);
    }

    @SparkMethod
    public static void defaultResponseTransformer(
        final ResponseTransformer transformer) {
        Spark.defaultResponseTransformer(transformer);
    }

    @SparkMethod
    public static void port(final int port) {
        Spark.port(port);
    }

    @SparkMethod
    public static int port() {
        return Spark.port();
    }

    @SparkMethod
    public static void secure(final String keystoreFile,
        final String keystorePassword,
        final String truststoreFile,
        final String truststorePassword) {
        Spark.secure(keystoreFile,
            keystorePassword,
            truststoreFile,
            truststorePassword);
    }

    @SparkMethod
    public static void secure(final String keystoreFile,
        final String keystorePassword,
        final String certAlias,
        final String truststoreFile,
        final String truststorePassword) {
        Spark.secure(keystoreFile,
            keystorePassword,
            certAlias,
            truststoreFile,
            truststorePassword);
    }

    @SparkMethod
    public static void initExceptionHandler(
        final Consumer<Exception> initExceptionHandler) {
        Spark.initExceptionHandler(initExceptionHandler);
    }

    @SparkMethod
    public static void secure(final String keystoreFile,
        final String keystorePassword,
        final String truststoreFile,
        final String truststorePassword,
        final boolean needsClientCert) {
        Spark.secure(keystoreFile,
            keystorePassword,
            truststoreFile,
            truststorePassword,
            needsClientCert);
    }

    @SparkMethod
    public static void secure(final String keystoreFile,
        final String keystorePassword,
        final String certAlias,
        final String truststoreFile,
        final String truststorePassword,
        final boolean needsClientCert) {
        Spark.secure(keystoreFile,
            keystorePassword,
            certAlias,
            truststoreFile,
            truststorePassword,
            needsClientCert);
    }

    @SparkMethod
    public static void threadPool(final int maxThreads) {
        Spark.threadPool(maxThreads);
    }

    @SparkMethod
    public static void threadPool(final int maxThreads,
        final int minThreads,
        final int idleTimeoutMillis) {
        Spark.threadPool(maxThreads, minThreads, idleTimeoutMillis);
    }

    @SparkMethod
    public static void staticFileLocation(final String folder) {
        Spark.staticFileLocation(folder);
    }

    @SparkMethod
    public static void externalStaticFileLocation(
        final String externalFolder) {
        Spark.externalStaticFileLocation(externalFolder);
    }

    @SparkMethod
    public static void awaitInitialization() {
        Spark.awaitInitialization();
    }

    @SparkMethod
    public static void stop() {
        Spark.stop();
    }

    @SparkMethod
    public static void awaitStop() {
        Spark.awaitStop();
    }

    @SparkMethod
    public static void webSocket(final String path, final Class<?> handler) {
        Spark.webSocket(path, handler);
    }

    @SparkMethod
    public static void webSocket(final String path, final Object handler) {
        Spark.webSocket(path, handler);
    }

    @SparkMethod
    public static void webSocketIdleTimeoutMillis(final int timeoutMillis) {
        Spark.webSocketIdleTimeoutMillis(timeoutMillis);
    }

    @SparkMethod
    public static void notFound(final String page) {
        Spark.notFound(page);
    }

    @SparkMethod
    public static void internalServerError(final String page) {
        Spark.internalServerError(page);
    }

    @SparkMethod
    public static void notFound(final Route route) {
        Spark.notFound(route);
    }

    @SparkMethod
    public static void internalServerError(final Route route) {
        Spark.internalServerError(route);
    }

    @SparkMethod
    public static void init() {
        Spark.init();
    }

    @SparkMethod
    public static ModelAndView modelAndView(
        final Object model,
        final String viewName) {
        return new ModelAndView(model, viewName);
    }

    @SparkMethod
    public static List<RouteMatch> routes() {
        return Spark.routes();
    }

    @SparkMethod
    public static int activeThreadCount() {
        return Spark.activeThreadCount();
    }

    public static final Logger LOG = LoggerFactory
        .getLogger(SparkScript.class);

}
