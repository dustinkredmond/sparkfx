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

import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.SecureASTCustomizer;

import java.util.Arrays;
import java.util.Collections;

/**
 * The environment through which all executed Groovy code should be
 * run, the script run will inherit from {@code com.dustinredmond.apifx.groovy.SparkScript}
 */
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
            config.setScriptBaseClass("com.dustinredmond.sparkfx.groovy.SparkScript");

            // Disable calls to java.lang.System methods
            // This is by no means foolproof e.g.
            //      def c = java.lang.System
            //      c.exit(-1)
            // will succeed with no problem, but is enough to
            // deter the average developer without malicious intent
            SecureASTCustomizer customizer = new SecureASTCustomizer();
            customizer.setReceiversBlackList(Collections.singletonList(System.class.getName()));
            config.addCompilationCustomizers(customizer);

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
