package com.dustinredmond.sparkfx.util;

import javafx.scene.control.TableView;
import javafx.scene.text.Text;

public final class FXUtils {
    private FXUtils() {
        super();
    }

    public static void autoResizeColumns(
        final TableView<?> table, final double padding) {
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

    public static void autoResizeColumns(final TableView<?> table) {
        autoResizeColumns(table, DEFAULT_PADDING);
    }

    private static final int DEFAULT_PADDING = 25;
}
