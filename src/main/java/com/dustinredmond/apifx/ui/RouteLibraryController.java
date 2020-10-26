package com.dustinredmond.apifx.ui;

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

import com.dustinredmond.apifx.model.RouteLibrary;
import com.dustinredmond.apifx.persistence.RouteDAO;
import com.dustinredmond.apifx.persistence.RouteLibraryDAO;
import com.dustinredmond.apifx.ui.custom.CustomAlert;
import javafx.scene.control.TableView;

import java.util.Date;
import java.util.StringJoiner;

/**
 * Form controller class for the following windows
 * <ul>
 *     <li>RouteLibraryWindow</li>
 *     <li>LibraryAddWindow</li>
 *     <li>LibraryEditWindow</li>
 *     <li>LibraryDeleteWindow</li>
 * </ul>
 */
public class RouteLibraryController {

    public void enableDisableLibrary() {
        TableView<RouteLibrary> table = RouteLibraryWindow.getTable();
        if (table.getSelectionModel().isEmpty()) {
            CustomAlert.showWarning("Please first select a library from the table.");
            return;
        }

        RouteLibrary lib = table.getSelectionModel().getSelectedItem();
        String prompt = String.format("Class %s is currently %s, are you sure you wish to %s it?",
                lib.getClassName(),
                lib.isEnabled() ? "enabled" : "disabled",
                lib.isEnabled() ? "disable" : "enable");
        if (!CustomAlert.showConfirmation(prompt)) {
            return;
        }
        lib.setEnabled(!lib.isEnabled());
        new RouteLibraryDAO().update(lib);
        RouteLibraryWindow.refreshTableView();
    }

    public boolean addLibrary(String className, String code) {
        if (className == null || code == null ||
                className.trim().isEmpty() || code.trim().isEmpty()) {
            CustomAlert.showWarning("Both Class Name and Code are required.");
            return false;
        }
        RouteLibrary lib = new RouteLibrary();
        lib.setEnabled(true);
        lib.setCreated(new Date());
        lib.setCode(code);
        lib.setClassName(className);
        if (new RouteLibraryDAO().create(lib)) {
            RouteLibraryWindow.refreshTableView();
            return true;
        }

        return false;
    }

    public boolean editLibrary(RouteLibrary lib, String className, String code) {
        if (className == null || code == null ||
            className.trim().isEmpty() || code.trim().isEmpty()) {
            CustomAlert.showWarning("Class Name and code are both required.");
            return false;
        }

        lib.setClassName(className);
        lib.setCode(code);
        if (new RouteLibraryDAO().update(lib)) {
            return true;
        } else {
            CustomAlert.showWarning("Unable to save changes.");
            return false;
        }
    }

    public String getAssociatedRoutes(RouteLibrary lib) {
        StringJoiner sb = new StringJoiner("\n");
        new RouteDAO().findAll().forEach(e -> {
            if (e.getCode().contains("getLibrary(\""+lib.getClassName()+"\")")) {
                sb.add("\t- "+e.getUrl());
            }
        });
        return sb.toString();
    }
}
