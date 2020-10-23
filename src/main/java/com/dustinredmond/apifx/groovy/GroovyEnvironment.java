package com.dustinredmond.apifx.groovy;

import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;

public class GroovyEnvironment {

    /**
     * Gets an instance of the application's Groovy runtime environment.
     * @return GroovyEnvironment
     */
    public static GroovyEnvironment getInstance() {
        if (instance == null) {
            instance = new GroovyEnvironment();
            CompilerConfiguration config = new CompilerConfiguration();
            // Set SparkScript as base class this way we can add special methods
            // and expose exactly which Spark methods we choose (i.e. leave out deprecated ones)
            config.setScriptBaseClass("com.dustinredmond.apifx.groovy.SparkScript");
            shell = new GroovyShell(config);
        }
        return instance;

    }

    /**
     * Evaluates Groovy Code within the context of the application's default GroovyShell
     * Code can be a script, implement Runnable, or be a class with a main() method.
     * @param code Code to evaluate
     */
    public void evaluate(String code) {
        shell.evaluate(code);
    }

    private static GroovyEnvironment instance;
    private GroovyEnvironment() { super(); }
    private static GroovyShell shell = new GroovyShell();

}
