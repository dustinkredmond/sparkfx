package com.dustinredmond.apifx.ui;

import com.dustinredmond.apifx.groovy.GroovyEnvironment;
import com.dustinredmond.apifx.model.StartupScript;
import com.dustinredmond.apifx.persistence.StartupScriptDAO;
import com.dustinredmond.apifx.ui.custom.CustomAlert;
import javafx.scene.control.TableView;

/**
 * Form controller for windows dealing with StartupScripts
 * Handles control for the following windows:
 * <ul>
 *     <li>StartupScriptAddWindow</li>
 *     <li>StartupScriptEditWindow</li>
 *     <li>StartupScriptDeleteWindow</li>
 *     <li>StartupScriptWindow</li>
 * </ul>
 */
public class StartupScriptController {

    public void enableDisableScript() {
        if (table.getSelectionModel().isEmpty()) {
            CustomAlert.showWarning("Please first select a script from the table.");
            return;
        }

        StartupScript script = table.getSelectionModel().getSelectedItem();
        String prompt = String.format("The startup script is %s, do you wish to %s it? This will take effect the next " +
                        "time the server is restarted.",
                script.isEnabled() ? "enabled" : "disabled",
                script.isEnabled() ? "disable" : "enable");

        if (CustomAlert.showConfirmation(prompt)) {
            script.setEnabled(!script.isEnabled());
            new StartupScriptDAO().update(script);
            StartupScriptWindow.refreshTable();
        }
    }

    private final TableView<StartupScript> table = StartupScriptWindow.getTable();

    public boolean addStartupScript(String description, boolean enabled, String code) {
        if (description.trim().isEmpty() || code.trim().isEmpty()) {
            CustomAlert.showWarning("You must enter a description and code.");
            return false;
        }

        StartupScript script = new StartupScript(description, code, enabled);
        if (new StartupScriptDAO().create(script)) {
            CustomAlert.showInfo("The script has been added successfully. It will be run " +
                    "on next server startup.");
            StartupScriptWindow.refreshTable();
            return true;
        }
        return false;
    }

    public boolean editStartupScript(StartupScript script, String description, String code, boolean enabled) {
        if (description.trim().isEmpty() || code.trim().isEmpty()) {
            CustomAlert.showWarning("Both description and code are required.");
            return false;
        }
        script.setEnabled(enabled);
        script.setDescription(description);
        script.setCode(code);
        return new StartupScriptDAO().update(script);
    }

    public void runOnDemand() {
        if (table.getSelectionModel().isEmpty()) {
            CustomAlert.showWarning("Please first select a script to run.");
            return;
        }

        StartupScript script = table.getSelectionModel().getSelectedItem();
        String prompt = String.format("Are you sure you wish to run the below script now?\n\nDescription: %s",
                script.getDescription());
        if (CustomAlert.showConfirmation(prompt)) {
            try {
                GroovyEnvironment.getInstance().evaluate(script.getCode());
                CustomAlert.showInfo("Script executed successfully.");
            } catch (Exception e) {
                CustomAlert.showExceptionDialog(e, "Exception occurred when running script.");
            }
        }
    }
}
