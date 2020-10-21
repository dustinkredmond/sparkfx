package com.dustinredmond.apifx.ui;

import com.dustinredmond.apifx.model.StartupScript;
import com.dustinredmond.apifx.persistence.StartupScriptDAO;
import com.dustinredmond.apifx.ui.custom.CustomAlert;
import javafx.scene.control.TableView;

/**
 * Class to represent the window used to Delete StartupScripts
 */
public class StartupScriptDeleteWindow {

    public void show() {
        if (table.getSelectionModel().isEmpty()) {
            CustomAlert.showWarning("Please first select an item from the table.");
            return;
        }

        StartupScript script = table.getSelectionModel().getSelectedItem();
        String prompt = "Are you sure you wish to delete the Startup Script?\n\nDescription: " + script.getDescription();

        if (CustomAlert.showConfirmation(prompt)) {
            new StartupScriptDAO().remove(script);
            StartupScriptWindow.refreshTable();
        }
    }

    private static final TableView<StartupScript> table = StartupScriptWindow.getTable();
}
