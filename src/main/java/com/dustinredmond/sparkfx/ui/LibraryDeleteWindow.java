package com.dustinredmond.sparkfx.ui;

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
import com.dustinredmond.sparkfx.ui.custom.CustomAlert;
import javafx.scene.control.TableView;

/**
 * Class representing the window used to delete RouteLibrary objects
 */
public class LibraryDeleteWindow {

    public void show() {
        TableView<RouteLibrary> table = RouteLibraryWindow.getTable();
        if (table.getSelectionModel().isEmpty()) {
            CustomAlert.showWarning("Please select an item from the table first.");
            return;
        }
        RouteLibrary clazz = table.getSelectionModel().getSelectedItem();
        String associatedClasses = controller.getAssociatedRoutes(clazz);
        String displayClasses = associatedClasses.length() > 0 ? associatedClasses : "None";
        String prompt = String.format("Are you sure you want to remove %s? The following routes " +
                "currently depend on this library:\n\n%s", clazz.getClassName(), displayClasses);
        if (!CustomAlert.showConfirmation(prompt)) {
            return;
        }

        new RouteLibraryDAO().remove(clazz);
        RouteLibraryWindow.refreshTableView();
    }

    private static final RouteLibraryController controller = new RouteLibraryController();

}
