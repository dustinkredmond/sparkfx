package com.dustinredmond.apifx.groovy;

import groovy.lang.GroovyShell;

public class GroovyEnvironment {

    /**
     * Gets an instance of the application's Groovy runtime environment.
     * @return GroovyEnvironment
     */
    public static GroovyEnvironment getInstance() {
        if (instance == null) {
            instance = new GroovyEnvironment();
        }
        return instance;

    }

    /**
     * Evaluates Groovy Code within the context of the application's default GroovyShell
     * Code can be a script, implement Runnable, or be a class with a main() method.
     * @param code Code to evaluate
     */
    public void evaluate(String code) {
        shell.evaluate(IMPORTS + code + GET_LIBRARY_CODE);
    }

    private static GroovyEnvironment instance;
    private GroovyEnvironment() { super(); }
    private static final GroovyShell shell = new GroovyShell();
    /**
     * Code that provides a getLibrary function, to be used in Route definitions to get RouteLibraries
     * as executable Groovy/Java objects
     */
    @SuppressWarnings("GrUnnecessarySemicolon") // seriously, one of those semicolons is needed
    private static final String GET_LIBRARY_CODE =
            "\ndef static getLibrary(String className) {\n" +
            "    RouteLibrary clazz = new RouteLibraryDAO().findByClassName(className)\n" +
            "    if (clazz != null && clazz.isEnabled()) {\n" +
            "        try {\n" +
            "            return new GroovyClassLoader().parseClass(clazz.getCode()).getDeclaredConstructor().newInstance()\n" +
            "        } catch (Exception ignored) { return null; }\n" +
            "    }\n" +
            "}\n";

    private static final String IMPORTS = "import com.dustinredmond.apifx.persistence.RouteLibraryDAO\n" +
            "import com.dustinredmond.apifx.model.RouteLibrary\n" +
            "import spark.Spark.*\n\n";
}
