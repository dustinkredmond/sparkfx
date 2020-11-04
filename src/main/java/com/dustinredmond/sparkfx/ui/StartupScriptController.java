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

import com.dustinredmond.sparkfx.groovy.GroovyEnvironment;
import com.dustinredmond.sparkfx.model.StartupScript;
import com.dustinredmond.sparkfx.persistence.StartupScriptDAO;
import com.dustinredmond.sparkfx.ui.custom.CustomAlert;
import javafx.scene.control.TableView;

/**
 * Form controller for windows dealing with StartupScripts
 * Handles control for the following windows.
 * <ul>
 *     <li>StartupScriptAddWindow</li>
 *     <li>StartupScriptEditWindow</li>
 *     <li>StartupScriptDeleteWindow</li>
 *     <li>StartupScriptWindow</li>
 * </ul>
 */
public final class StartupScriptController {

    public void enableDisableScript() {
        if (table.getSelectionModel().isEmpty()) {
            CustomAlert.showWarning(
                "Please first select a script from the table.");
            return;
        }

        StartupScript script = table.getSelectionModel().getSelectedItem();
        String prompt = String.format(
            "The startup script is %s, do you wish to %s it? "
            + "This will take effect the next "
            + "time the server is restarted.",
                script.isEnabled() ? "enabled" : "disabled",
                script.isEnabled() ? "disable" : "enable");

        if (CustomAlert.showConfirmation(prompt)) {
            script.setEnabled(!script.isEnabled());
            new StartupScriptDAO().update(script);
            StartupScriptWindow.refreshTable();
        }
    }

    private final TableView<StartupScript> table =
        StartupScriptWindow.getTable();

    public boolean addStartupScript(final String description,
        final boolean enabled,
        final String code) {
        if (description.trim().isEmpty() || code.trim().isEmpty()) {
            CustomAlert.showWarning(
                "You must enter a description and code.");
            return false;
        }

        StartupScript script = new StartupScript(description, code, enabled);
        if (new StartupScriptDAO().create(script)) {
            CustomAlert.showInfo(
                "The script has been added successfully. It will be run "
                    + "on next server startup.");
            StartupScriptWindow.refreshTable();
            return true;
        }
        return false;
    }

    public boolean editStartupScript(final StartupScript script,
        final String description, final String code, final boolean enabled) {
        if (description.trim().isEmpty() || code.trim().isEmpty()) {
            CustomAlert.showWarning(
                "Both description and code are required.");
            return false;
        }
        script.setEnabled(enabled);
        script.setDescription(description);
        script.setCode(code);
        return new StartupScriptDAO().update(script);
    }

    public void runOnDemand() {
        if (table.getSelectionModel().isEmpty()) {
            CustomAlert.showWarning(
                "Please first select a script to run.");
            return;
        }

        StartupScript script = table.getSelectionModel().getSelectedItem();
        String prompt = String.format("Are you sure you wish to run "
                + "the below script now?\n\nDescription: %s",
                script.getDescription());
        if (CustomAlert.showConfirmation(prompt)) {
            try {
                GroovyEnvironment.getInstance().evaluate(script.getCode());
                CustomAlert.showInfo("Script executed successfully.");
            } catch (Exception e) {
                CustomAlert.showExceptionDialog(e,
                    "Exception occurred when running script.");
            }
        }
    }
}
