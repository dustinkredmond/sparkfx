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
        shell.evaluate(IMPORTS + "\n" + code + "\n" + GET_LIBRARY_CODE);
    }

    private static GroovyEnvironment instance;
    private GroovyEnvironment() { super(); }
    private static final GroovyShell shell = new GroovyShell();

    @SuppressWarnings("UnnecessaryQualifiedReference")
    // for some reason, the reference to Spark.get must be fully-qualified
    // we have to define this method to overload the Groovy .get() method
    private static final String GET_LIBRARY_CODE =
            "def static get(url, reqRes) {\n" +
                    "    spark.Spark.get(url, reqRes)" +
                    "}\n" +
                    "def static getLibrary(String className) {\n" +
                    "    RouteLibrary clazz = new RouteLibraryDAO().findByClassName(className)\n" +
                    "    if (clazz != null && clazz.isEnabled()) {\n" +
                    "        try {\n" +
                    "            return new GroovyClassLoader().parseClass(clazz.getCode()).getDeclaredConstructor().newInstance()\n" +
                    "        } catch (Exception ignored) { return null }\n" +
                    "    }\n" +
                    "}\n";
    private static final String IMPORTS = "\nimport com.dustinredmond.apifx.persistence.RouteLibraryDAO\n" +
            "import com.dustinredmond.apifx.model.RouteLibrary\n" +
            "import spark.Spark.*\n\n";
}
