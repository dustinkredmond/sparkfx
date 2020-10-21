package com.dustinredmond.apifx.ui;


import com.dustinredmond.apifx.ui.custom.CustomGrid;
import com.dustinredmond.apifx.ui.custom.CustomStage;
import com.dustinredmond.apifx.ui.custom.GroovySyntaxEditor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * Class to represent the window used to add StartupScripts
 */
public class StartupScriptAddWindow {

    public void show() {
        CustomStage stage = new CustomStage();
        stage.setTitle(UI.APP_TITLE + " - Add Startup Script");
        CustomGrid grid = new CustomGrid();

        grid.add(new Label("Description"), 0, 0);
        TextField tfDescription = new TextField();
        grid.add(tfDescription, 1, 0);

        CheckBox cbEnabled = new CheckBox("Enabled?");
        grid.add(cbEnabled, 0, 1);

        GroovySyntaxEditor gse = new GroovySyntaxEditor();
        gse.setText(PROMPT);
        grid.add(new Label("Code:"), 0, 2);
        grid.add(gse, 1, 2);
        GridPane.setVgrow(gse, Priority.ALWAYS);
        GridPane.setHgrow(gse, Priority.ALWAYS);

        Button buttonAdd = new Button("Add Script");
        buttonAdd.setOnAction(e -> {
            if (controller.addStartupScript(tfDescription.getText(), cbEnabled.isSelected(), gse.getText())) {
                stage.hide();
            }
        });
        grid.add(buttonAdd, 0, 3);

        stage.setScene(new Scene(grid));
        stage.setMaximized(true);
        stage.show();
    }

    private static final StartupScriptController controller = new StartupScriptController();
    private static final String PROMPT = "import static spark.Spark.*\n\n" +
            "// Example script: GZIP everything\n" +
            "after((req,res) -> res.header(\"Content-Encoding\", \"gzip\"));\n\n" +
            "// Example script: Enable SSL\n" +
            "def keyStoreLocation = \"path/to/keystore.jks\"\n" +
            "def keyStorePassword = \"someSecurePassw0rd\"\n" +
            "secure(keyStoreLocation, keyStorePassword, null, null);\n\n" +
            "// See Spark API documentation for more options,\n" +
            "// You can also add non-Spark code here, e.g. a ShutdownHook";

}
