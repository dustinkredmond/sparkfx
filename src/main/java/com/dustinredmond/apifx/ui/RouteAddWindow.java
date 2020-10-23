package com.dustinredmond.apifx.ui;

import com.dustinredmond.apifx.ServerContext;
import com.dustinredmond.apifx.ui.custom.CustomAlert;
import com.dustinredmond.apifx.ui.custom.CustomGrid;
import com.dustinredmond.apifx.ui.custom.CustomStage;
import com.dustinredmond.apifx.ui.custom.GroovySyntaxEditor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * Class representing the window used to add Route objects
 */
public class RouteAddWindow {

    public void show() {
        if (!ServerContext.isActive()) {
            CustomAlert.showInfo("The server is not running, please start it before adding/modifying routes");
            return;
        }
        CustomStage stage = new CustomStage();
        stage.setTitle("Add Route");
        CustomGrid grid = new CustomGrid();
        stage.setScene(new Scene(grid));

        int rowIndex = 0;

        TextField tfRoute = new TextField();
        tfRoute.setPromptText("/api/someRoute");
        grid.add(new Label("URL:"), 0, rowIndex);
        grid.add(tfRoute, 1, rowIndex++);

        GroovySyntaxEditor taCode = new GroovySyntaxEditor();
        taCode.setText(getPromptText(tfRoute.getPromptText()));
        grid.add(taCode, 0, rowIndex++, 2, 1);
        GridPane.setVgrow(taCode, Priority.ALWAYS);
        GridPane.setHgrow(taCode, Priority.ALWAYS);
        taCode.setPrefWidth(Double.MAX_VALUE);


        Button buttonAdd = new Button("Add Route");

        taCode.setDisable(true);
        buttonAdd.setDisable(true);
        tfRoute.textProperty().addListener(e -> {
            if (!tfRoute.getText().isEmpty()) {
                taCode.setDisable(false);
            } else {
                taCode.setDisable(true);
            }
            if (taCode.getText().trim().isEmpty() || tfRoute.getText().trim().isEmpty()) {
                buttonAdd.setDisable(true);
            } else {
                buttonAdd.setDisable(false);
            }
        });
        buttonAdd.setMinWidth(120);
        buttonAdd.setOnAction(e -> {
            if (controller.addRoute(tfRoute,taCode)) {
                stage.hide();
                RouteWindow.refreshTable();
            }
        });
        grid.add(buttonAdd, 0, rowIndex);

        stage.setMaximized(true);
        stage.show();
    }

    private static final RoutesController controller = new RoutesController();
    private String getPromptText(String route) {
        return  "get(\""+route+"\", (req,res) -> {\n" +
                "\t// Read the documentation at https://sparkjava.com/ \n" +
                "\t// You can get a RouteLibrary like so: def myLibrary = getLibrary(\"MyLibraryName\");\n" +
                "\tres.body(\"Hello, World!\");\n" +
                "\treturn res.body();\n" +
                "});";
    }
}
