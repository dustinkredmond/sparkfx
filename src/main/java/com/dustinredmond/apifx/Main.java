package com.dustinredmond.apifx;

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
