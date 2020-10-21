package com.dustinredmond.apifx.ui;

import com.dustinredmond.apifx.model.RouteLibrary;
import com.dustinredmond.apifx.persistence.RouteLibraryDAO;
import com.dustinredmond.apifx.ui.custom.CustomAlert;
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
