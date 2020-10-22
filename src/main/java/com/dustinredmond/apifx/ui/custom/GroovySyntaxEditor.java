package com.dustinredmond.apifx.ui.custom;

import javafx.application.Platform;
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

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A custom Java code syntax editor with limited support
 * for Groovy code, more to do to fully support Groovy
 */
public class GroovySyntaxEditor extends CodeArea {


    public GroovySyntaxEditor() {
        this.setParagraphGraphicFactory(LineNumberFactory.get(this));
        this.setLineHighlighterFill(Paint.valueOf("GAINSBORO"));
        this.setLineHighlighterOn(true);

        final Pattern whiteSpace = Pattern.compile( "^\\s+" );
        this.addEventHandler(KeyEvent.KEY_PRESSED, KE -> {
            if (KE.getCode() == KeyCode.ENTER) {
                int caretPosition = this.getCaretPosition();
                int currentParagraph = this.getCurrentParagraph();
                Matcher m0 = whiteSpace.matcher(this.getParagraph(currentParagraph-1).getSegments().get(0) );
                if (m0.find()) Platform.runLater(() -> this.insertText(caretPosition, m0.group()));
            }
        });
        try {
            this.getStylesheets().add(this.getClass().getResource("groovy-keywords.css").toExternalForm());
        } catch (Exception ignored) {
            // Better to not have syntax highlighting than destroying the QueryEditor
            // Entire program is unusable if this exception doesn't get caught....
            System.err.println("Unable to find sql-keywords.css file, editors will not highlight syntax.");
        }
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

    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String SINGLE_QUOTE_STRING_PATTERN = "'([^'\\\\]|\\\\.)*'";
    private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";
    private static final String TRIPLE_QUOTE_PATTERN = "(?:\\n[\\t ]*)\\\"{3}(.*?)\\\"{3}'";
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

    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while(matcher.find()) {
            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
                    matcher.group("PAREN") != null ? "paren" :
                    matcher.group("BRACE") != null ? "brace" :
                    matcher.group("BRACKET") != null ? "bracket" :
                    matcher.group("SEMICOLON") != null ? "semicolon" :
                    matcher.group("STRING") != null ? "string" :
                    matcher.group("SINGLESTRING") != null ? "string" :
                    matcher.group("COMMENT") != null ? "comment" :
                    matcher.group("LITERAL") != null ? "literal" :
                    null; assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }

    public Subscription highlightingSubscription = this.multiPlainChanges().successionEnds(Duration.ofMillis(500))
            .subscribe(ignore -> this.setStyleSpans(0, computeHighlighting(this.getText())));

    /**
     * Call before disposing of the SyntaxEditor
     */
    public void dispose() {
        highlightingSubscription.unsubscribe();
    }

    /**
     * Sets the text of the editor, clearing any previously entered text
     * @param text The text to set
     */
    public void setText(String text) {
        this.clear();
        this.appendText(text);
    }

    /**
     * FIXME Deprecated in Java9+, handle on the front end
     *  when we migrate to Java9
     * @throws Throwable the throwable
     */
    @Override
    protected void finalize() throws Throwable {
        try {
            this.highlightingSubscription.unsubscribe();
        } finally {
            super.finalize();
        }
    }

    private static final Logger log = LoggerFactory.getLogger(GroovySyntaxEditor.class);
}
