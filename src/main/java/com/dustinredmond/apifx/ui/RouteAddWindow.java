package com.dustinredmond.apifx.ui;

import com.dustinredmond.apifx.model.Verb;
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
        CustomStage stage = new CustomStage();
        stage.setTitle("Add Route");
        CustomGrid grid = new CustomGrid();
        stage.setScene(new Scene(grid));

        int rowIndex = 0;

        TextField tfRoute = new TextField();
        tfRoute.setPromptText("/api/someRoute");
        grid.add(new Label("API Endpoint:"), 0, rowIndex);
        grid.add(tfRoute, 1, rowIndex++);

        ComboBox<Verb> cbEndpoint = new ComboBox<>();
        cbEndpoint.getItems().addAll(Verb.values());
        cbEndpoint.getSelectionModel().select(Verb.GET);
        grid.add(new Label("HTTP Verb:"), 0, rowIndex);
        grid.add(cbEndpoint, 1, rowIndex++);


        GroovySyntaxEditor taCode = new GroovySyntaxEditor();
        taCode.setText(getPromptText(tfRoute.getPromptText(),"get"));
        grid.add(taCode, 0, rowIndex++, 2, 1);
        GridPane.setVgrow(taCode, Priority.ALWAYS);
        GridPane.setHgrow(taCode, Priority.ALWAYS);
        taCode.setPrefWidth(Double.MAX_VALUE);


        Button buttonAdd = new Button("Add Route");
        buttonAdd.setMinWidth(120);
        buttonAdd.setOnAction(e -> {
            if (controller.addRoute(tfRoute, cbEndpoint, taCode)) {
                stage.hide();
                RouteWindow.refreshTable();
            }
        });
        grid.add(buttonAdd, 0, rowIndex);

        stage.setMaximized(true);
        stage.show();
    }

    private static final RoutesController controller = new RoutesController();
    private String getPromptText(String route, String verb) {
        return  verb+"(\""+route+"\", (req,res) -> {\n" +
                "\t// Read the documentation at https://sparkjava.com/ \n" +
                "\t// You can get a RouteLibrary like so: def myLibrary = getLibrary(\"MyLibraryName\");\n" +
                "\tres.body(\"Hello, World!\");\n" +
                "\treturn res.body();\n" +
                "});";
    }
}
