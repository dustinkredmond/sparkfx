package com.dustinredmond.sparkfx.util;

import javafx.scene.control.TableView;
import javafx.scene.text.Text;

public class FXUtils {

    public static void autoResizeColumns(TableView<?> table, double padding) {
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        table.getColumns().forEach(column -> {
            Text t = new Text(column.getText());
            double max = t.getLayoutBounds().getWidth();
            for (int i = 0; i < table.getItems().size(); i++) {
                if (column.getCellData(i) != null) {
                    t = new Text(column.getCellData(i).toString());
                    double cellWidth = t.getLayoutBounds().getWidth();
                    if (cellWidth > max) {
                        max = cellWidth;
                    }
                }
            }
            column.setPrefWidth(max + padding);
        });
    }

    public static void autoResizeColumns(TableView<?> table) {
        autoResizeColumns(table, 25);
    }
}
