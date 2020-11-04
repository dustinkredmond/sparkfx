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

import com.dustinredmond.sparkfx.model.StartupScript;
import com.dustinredmond.sparkfx.persistence.StartupScriptDAO;
import com.dustinredmond.sparkfx.ui.custom.CustomAlert;
import javafx.scene.control.TableView;

/**
 * Class to represent the window used to Delete StartupScripts.
 */
public final class StartupScriptDeleteWindow {

    public void show() {
        if (table.getSelectionModel().isEmpty()) {
            CustomAlert.showWarning(
                "Please first select an item from the table.");
            return;
        }

        StartupScript script = table.getSelectionModel().getSelectedItem();
        String prompt = "Are you sure you wish to delete "
            + "the Startup Script?\n\nDescription: " + script.getDescription();

        if (CustomAlert.showConfirmation(prompt)) {
            new StartupScriptDAO().remove(script);
            StartupScriptWindow.refreshTable();
        }
    }

    private static final TableView<StartupScript> table =
        StartupScriptWindow.getTable();
}
