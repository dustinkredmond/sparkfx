package com.dustinredmond.apifx.ui;

import com.dustinredmond.apifx.ui.custom.CustomGrid;
import com.dustinredmond.apifx.ui.custom.CustomStage;
import com.dustinredmond.apifx.ui.custom.GroovySyntaxEditor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * Class representing the window used to add RouteLibrary objects
 */
public class LibraryAddWindow {

    public void show() {
        CustomStage stage = new CustomStage();
        CustomGrid grid = new CustomGrid();
        stage.setTitle(UI.APP_TITLE + " - New Library");

        grid.add(new Label("Class Name:"), 0, 0);
        TextField tfClassName = new TextField();
        grid.add(tfClassName, 1, 0);

        GroovySyntaxEditor se = new GroovySyntaxEditor();
        se.setText(getPromptText());
        grid.add(se, 0, 1, 2, 1);
        GridPane.setVgrow(se, Priority.ALWAYS);
        GridPane.setHgrow(se, Priority.ALWAYS);

        Button buttonAdd = new Button("Add Library");
        buttonAdd.setMinWidth(120);
        grid.add(buttonAdd, 0, 2);
        buttonAdd.setOnAction(e -> {
            if (controller.addLibrary(tfClassName.getText(), se.getText())) {
                stage.hide();
            }
        });

        stage.setScene(new Scene(grid));
        stage.setMaximized(true);
        stage.show();
    }

    private String getPromptText() {
        return "class SomeLibrary {\n" +
                "    def someUtilityMethod() {\n" +
                "        println \"Some work was done...\";\n" +
                "    }\n" +
                "}";
    }

    private static final RouteLibraryController controller = new RouteLibraryController();
}
