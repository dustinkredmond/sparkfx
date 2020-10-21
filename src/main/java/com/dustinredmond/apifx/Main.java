package com.dustinredmond.apifx;

import com.dustinredmond.apifx.persistence.DatabaseBootstrap;
import com.dustinredmond.apifx.ui.UI;
import com.dustinredmond.apifx.ui.custom.CustomExceptionHandler;

/**
 * Application entry point
 */
public class Main {

    public static void main(String[] args) {
        // Make sure to set for each Thread in our application
        Thread.currentThread().setUncaughtExceptionHandler(new CustomExceptionHandler());
        Thread t = new Thread(new DatabaseBootstrap());
        t.setUncaughtExceptionHandler(new CustomExceptionHandler());
        t.start();
        while (true) {
            // Wait for DB tables to be created before allowing user input
            if (!t.isAlive()) {
                new UI().startUi(args);
                break;
            }
        }
    }


}
