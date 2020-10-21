package com.dustinredmond.apifx.ui.custom;

import javafx.embed.swing.SwingNode;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;

/**
 * A custom AWT syntax editor, embedded in a JavaFX SwingNode
 * that provides formatting and syntax highlighting for Groovy code.
 */
public class GroovySyntaxEditor extends SwingNode {

    static {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
        });
    }


    public GroovySyntaxEditor() {
        textArea = new RSyntaxTextArea(Integer.MAX_VALUE, 250);
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_GROOVY);
        textArea.setCodeFoldingEnabled(true);
        RTextScrollPane sp = new RTextScrollPane(textArea);
        this.setContent(sp);
    }

    /**
     * Sets the editor's displayed text
     * @param text The text to set
     */
    public void setText(String text) {
        textArea.setText(text);
    }

    /**
     * Gets the editor's currently set text
     * @return The editor's text
     */
    public String getText() {
        return textArea.getText();
    }

    private final RSyntaxTextArea textArea;
}
