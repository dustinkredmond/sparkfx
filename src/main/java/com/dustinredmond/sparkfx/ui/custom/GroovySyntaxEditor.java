package com.dustinredmond.sparkfx.ui.custom;

/*
 *  Copyright 2020  Dustin K. Redmond
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import com.dustinredmond.sparkfx.groovy.SparkScript;
import com.dustinredmond.sparkfx.ui.UI;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Paint;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A custom Java code syntax editor with limited support
 * for Groovy code, more to do to fully support Groovy.
 */
@SuppressWarnings({"RegExpSingleCharAlternation", "RegExpRedundantEscape"})
public class GroovySyntaxEditor extends CodeArea {

    public GroovySyntaxEditor() {
        this.visibleProperty().addListener(e -> {
            if (!this.isVisible()) {
                this.dispose();
            }
        });
        this.setParagraphGraphicFactory(LineNumberFactory.get(this));
        this.setLineHighlighterFill(Paint.valueOf("GAINSBORO"));
        this.setLineHighlighterOn(true);

        final Pattern whiteSpace = Pattern.compile("^\\s+");
        this.addEventHandler(KeyEvent.KEY_PRESSED, KE -> {
            if (KE.getCode() == KeyCode.ENTER) {
                int caretPosition = this.getCaretPosition();
                int currentParagraph = this.getCurrentParagraph();
                Matcher m0 = whiteSpace.matcher(
                    this.getParagraph(currentParagraph - 1)
                        .getSegments().get(0));
                if (m0.find()) {
                    Platform.runLater(() ->
                        this.insertText(caretPosition, m0.group()));
                }
            }
        });
        try {
            this.getStylesheets().add(
                this.getClass().getResource("groovy-keywords.css")
                    .toExternalForm());
        } catch (Exception ignored) {
            // Better to not have syntax highlighting
            // than destroying the QueryEditor
            // Entire program is unusable if this exception
            // doesn't get caught....
            LOG.error("Unable to find groovy-keywords.css file, "
                + "editors will not highlight syntax.");
        }
        this.setOnKeyReleased(e -> {
            if (e.getCode().equals(KeyCode.F1)) {
                showAvailableMethods();
            }
        });
    }

    private void showAvailableMethods() {
        ListView<String> listView = new ListView<>();
        for (Method m : SparkScript.class.getDeclaredMethods()) {
            listView.getItems().add(
                m.toGenericString()
                    .replaceAll("com.dustinredmond.apifx.groovy.SparkScript.",
                        ""));
        }
        Collections.sort(listView.getItems());
        CustomStage stage = new CustomStage();
        stage.setTitle(UI.APP_TITLE + " - Spark Methods Available");
        stage.setScene(new Scene(listView));
        stage.show();
    }

    private static final String[] KEYWORDS = new String[] {
            "abstract",
            "as",
            "assert",
            "boolean",
            "break",
            "byte",
            "case",
            "catch",
            "char",
            "class",
            "const",
            "continue",
            "def",
            "default",
            "do",
            "double",
            "each",
            "else",
            "enum",
            "extends",
            "false",
            "final",
            "finally",
            "float",
            "for",
            "goto",
            "if",
            "implements",
            "import",
            "in",
            "instanceof",
            "int",
            "interface",
            "it",
            "long",
            "native",
            "new",
            "null",
            "package",
            "private",
            "protected",
            "public",
            "return",
            "short",
            "static",
            "strictfp",
            "super",
            "switch",
            "synchronized",
            "this",
            "throw",
            "throws",
            "trait",
            "transient",
            "true",
            "try",
            "using",
            "var",
            "void",
            "volatile",
            "while",
            "with",
    };

    private static final String KEYWORD_PATTERN =
        "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String STRING_PATTERN =
        "\"([^\"\\\\]|\\\\.)*\"";
    private static final String SINGLE_QUOTE_STRING_PATTERN =
        "'([^'\\\\]|\\\\.)*'";
    private static final String COMMENT_PATTERN =
        "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";
    private static final String TRIPLE_QUOTE_PATTERN =
        "(?:\\n[\\t ]*)\\\"{3}(.*?)\\\"{3}'";
    private static final String LITERAL_PATTERN = "\\b[0-9]+";
    private static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                    + "|(?<PAREN>" + PAREN_PATTERN + ")"
                    + "|(?<BRACE>" + BRACE_PATTERN + ")"
                    + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                    + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                    + "|(?<STRING>" + STRING_PATTERN + ")"
                    + "|(?<SINGLESTRING>" + SINGLE_QUOTE_STRING_PATTERN + ")"
                    + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
                    + "|(?<TRIPLEQUOTE>" + TRIPLE_QUOTE_PATTERN + ")"
                    + "|(?<LITERAL>" + LITERAL_PATTERN + ")"
    );

    private static StyleSpans<Collection<String>>
    computeHighlighting(final String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder =
            new StyleSpansBuilder<>();
        while (matcher.find()) {
            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword"
                        : matcher.group("PAREN") != null ? "paren"
                        : matcher.group("BRACE") != null ? "brace"
                        : matcher.group("BRACKET") != null ? "bracket"
                        : matcher.group("SEMICOLON") != null ? "semicolon"
                        : matcher.group("STRING") != null ? "string"
                        : matcher.group("SINGLESTRING") != null ? "string"
                        : matcher.group("COMMENT") != null ? "comment"
                        : matcher.group("TRIPLEQUOTE") != null ? "comment"
                        : matcher.group("LITERAL") != null ? "literal"
                        : null; assert styleClass != null;
            spansBuilder.add(
                Collections.emptyList(), matcher.start() - lastKwEnd
            );
            spansBuilder.add(
                Collections.singleton(styleClass),
                matcher.end() - matcher.start()
            );
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }

    private final Subscription highlightingSubscription =
        this.multiPlainChanges().successionEnds(
            Duration.ofMillis(DEFAULT_SUBSCRIPTION_MS))
            .subscribe(ignore ->
                this.setStyleSpans(0, computeHighlighting(this.getText())));

    /**
     * Call before disposing of the SyntaxEditor.
     */
    public void dispose() {
        LOG.debug("Disposed of {} - {}",
            this.getClass().getName(), this.toString());
        highlightingSubscription.unsubscribe();
    }

    /**
     * Sets the text of the editor, clearing any previously entered text.
     * @param text The text to set
     */
    public void setText(final String text) {
        this.clear();
        this.appendText(text);
    }

    /**
     * GroovySyntaxEditor's default logger.
     */
    private static final Logger LOG = LoggerFactory
        .getLogger(GroovySyntaxEditor.class);

    private static final int DEFAULT_SUBSCRIPTION_MS = 500;
}
