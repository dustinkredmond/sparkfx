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
        taCode.setText(PROMPT_TEXT);
        grid.add(taCode, 0, rowIndex++, 2, 1);
        GridPane.setVgrow(taCode, Priority.ALWAYS);
        GridPane.setHgrow(taCode, Priority.ALWAYS);

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
    private static final String PROMPT_TEXT =
        "// Spark's Request and Response will be passed into below as 'req' and 'res'\n" +
        "// Read the documentation at https://sparkjava.com/ to understand the Request and Response API\n\n" +
        "// The following example creates an endpoint that renders the text 'Hello, World!'\n\n" +
            "// You can get a library (Menu -> Routes -> Route Libraries)\n" +
            "// by the following line\n" +
            "// def MyClassName = getLibrary(\"MyClassName\")\n" +
            "// MyClassName.someStaticMethod();\n" +
            "// MyClassName.someInstanceMethod();\n\n" +
            "res.body(\"Hello, World!\");\n" +
            "return res.body();";
}
