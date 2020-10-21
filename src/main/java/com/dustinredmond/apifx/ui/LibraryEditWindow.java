package com.dustinredmond.apifx.ui;

import com.dustinredmond.apifx.model.RouteLibrary;
import com.dustinredmond.apifx.ui.custom.CustomAlert;
import com.dustinredmond.apifx.ui.custom.CustomGrid;
import com.dustinredmond.apifx.ui.custom.CustomStage;
import com.dustinredmond.apifx.ui.custom.GroovySyntaxEditor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class LibraryEditWindow {
    public void show() {
        TableView<RouteLibrary> table = RouteLibraryWindow.getTable();
        if (table.getSelectionModel().isEmpty()) {
            CustomAlert.showWarning("Please first select an item from the table.");
            return;
        }

        RouteLibrary clazz = table.getSelectionModel().getSelectedItem();
        CustomStage stage = new CustomStage();
        CustomGrid grid = new CustomGrid();
        stage.setTitle(UI.APP_TITLE + " - Edit Library");

        grid.add(new Label("Class Name:"), 0, 0);
        TextField tfClassName = new TextField(clazz.getClassName());
        grid.add(tfClassName, 1, 0);

        GroovySyntaxEditor se = new GroovySyntaxEditor();
        se.setText(clazz.getCode());
        grid.add(se, 0, 1, 2, 1);
        GridPane.setVgrow(se, Priority.ALWAYS);
        GridPane.setHgrow(se, Priority.ALWAYS);

        Button buttonAdd = new Button("Save Changes");
        buttonAdd.setMinWidth(120);
        grid.add(buttonAdd, 0, 2);
        buttonAdd.setOnAction(e -> {
            if (controller.editLibrary(clazz, tfClassName.getText(), se.getText())) {
                stage.hide();
            }
        });

        stage.setScene(new Scene(grid));
        stage.setMaximized(true);
        stage.show();
    }

    private static final RouteLibraryController controller = new RouteLibraryController();
}
