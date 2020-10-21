package com.dustinredmond.apifx.ui;

import com.dustinredmond.apifx.ServerContext;
import com.dustinredmond.apifx.model.Route;
import com.dustinredmond.apifx.model.Verb;
import com.dustinredmond.apifx.persistence.RouteDAO;
import com.dustinredmond.apifx.ui.custom.CustomGrid;
import com.dustinredmond.apifx.ui.custom.CustomMenuBar;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.util.Date;

/**
 * Class to represent the window used to view Routes
 */
public class RouteWindow {

    public void show() {
        UI.getPrimaryStage().setTitle(UI.APP_TITLE + " - Routes Overview - " + ServerContext.getDescription());
        CustomGrid grid = new CustomGrid();
        BorderPane root = new BorderPane();
        CustomMenuBar mb = new CustomMenuBar(this);
        mb.enableServerStopItem(ServerContext.isActive());
        mb.enableServerStartItem(!ServerContext.isActive());
        root.setTop(mb);
        root.setCenter(grid);

        ButtonBar buttons = new ButtonBar();
        Button buttonAdd = new Button("Add Route");
        buttonAdd.setOnAction(e -> new RouteAddWindow().show());
        Button buttonEdit = new Button("Edit Route");
        buttonEdit.setOnAction(e -> new RouteEditWindow().show(table));
        Button buttonDelete = new Button("Delete Route");
        buttonDelete.setOnAction(e -> new RouteDeleteWindow().show(table));
        buttons.getButtons().addAll(buttonAdd, buttonEdit, buttonDelete);

        grid.add(buttons, 0, 0);

        grid.add(table, 0, 1);
        GridPane.setHgrow(table, Priority.ALWAYS);
        GridPane.setVgrow(table, Priority.ALWAYS);

        UI.getPrimaryStage().getScene().setRoot(root);
    }

    private static final TableView<Route> table = getEndpointsTable();

    public TableView<Route> getTable() { return table; }

    private static TableView<Route> getEndpointsTable() {
        TableView<Route> table = new TableView<>();

        TableColumn<Route, Date> columnCreated = new TableColumn<>("Created");
        columnCreated.setCellValueFactory(new PropertyValueFactory<>("created"));
        table.getColumns().add(columnCreated);

        TableColumn<Route, String> columnUrl = new TableColumn<>("URL");
        columnUrl.setCellValueFactory(new PropertyValueFactory<>("url"));
        table.getColumns().add(columnUrl);

        TableColumn<Route, Boolean> columnEnabled = new TableColumn<>("Enabled");
        columnEnabled.setCellValueFactory(e -> new SimpleBooleanProperty(e.getValue().isEnabled()));
        columnEnabled.setCellFactory(cell -> new CheckBoxTableCell<>());
        table.getColumns().add(columnEnabled);

        TableColumn<Route, Verb> columnVerb = new TableColumn<>("Verb");
        columnVerb.setCellValueFactory(new PropertyValueFactory<>("verb"));
        table.getColumns().add(columnVerb);

        table.setItems(FXCollections.observableArrayList(new RouteDAO().findAll()));

        ContextMenu cm = new ContextMenu();

        MenuItem miBrowse = new MenuItem("Open in Browser");
        miBrowse.setOnAction(e -> controller.browseRoute(table));
        MenuItem miDisable = new MenuItem("Disable/Enable Route");
        miDisable.setOnAction(e -> controller.disableRoute(table));
        MenuItem miAdd = new MenuItem("Add Route");
        miAdd.setOnAction(e -> new RouteAddWindow().show());
        MenuItem miEdit = new MenuItem("Edit Route");
        miEdit.setOnAction(e -> new RouteEditWindow().show(table));
        MenuItem miDelete = new MenuItem("Delete Route");
        miDelete.setOnAction(e -> new RouteDeleteWindow().show(table));

        cm.getItems().addAll(miBrowse, miDisable, new SeparatorMenuItem(), miAdd, miEdit, miDelete);
        table.setContextMenu(cm);

        table.setOnContextMenuRequested(e -> {
            if (!ServerContext.isActive()) {
                cm.hide();
            }
            if (table.getSelectionModel().isEmpty()) {
                miBrowse.setDisable(true);
                miEdit.setDisable(true);
                miDelete.setDisable(true);
                miDisable.setDisable(true);
            } else {
                miBrowse.setDisable(false);
                miEdit.setDisable(false);
                miDelete.setDisable(false);
                miDisable.setDisable(false);
            }
        });

        table.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && MouseButton.PRIMARY.equals(e.getButton())) {
                new RouteEditWindow().show(table);
            }
        });

        table.setPlaceholder(new Label("Welcome to "+UI.APP_TITLE+" click on \"Add Route\" to get started!"));

        return table;

    }

    public static void refreshTable() {
        table.setItems(FXCollections.observableArrayList(new RouteDAO().findAll()));
    }

    private static final RoutesController controller = new RoutesController();
}
