package com.zam.utils;

import java.awt.Color;
import java.util.List;

import javax.swing.UIManager;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.zam.utils.JavaSyntaxTokenizer.Token;
import com.zam.utils.JavaSyntaxTokenizer.TokenType;

/**
 * A class that extends DefaultStyledDocument and can be used to highlight the syntax of
 * a Java code string using the Swing text framework.
 *
 * Responsibilities:
 * - Highlighting syntax elements such as keywords, string literals, comments, etc.
 * - Updating syntax highlighting when text is inserted or removed.
 * - Providing customizable color schemes for different syntax elements.
 *
 * Usage:
 * - Create an instance of JavaSyntaxHighlightingKit and set it as the document for your JTextPane.
 * - The syntax highlighting is automatically updated when text is inserted or removed.
 *
 * Example:
 * ```java
 * JavaSyntaxHighlightingKit syntaxHighlighter = new JavaSyntaxHighlightingKit(true);
 * JTextPane codePane = new JTextPane(syntaxHighlighter);
 * ```
 *
 * @author Muhammed Zohaib
 * @version 1.0
 * @since 2023-11-29
 */
public class JavaSyntaxHighlightingKit extends DefaultStyledDocument {

    private static AttributeSet KEYWORD_ATTRS;
    private static AttributeSet STRING_LITERAL_ATTRS;
    private static AttributeSet COMMENT_ATTRS;
    private static AttributeSet NUMBER_LITERAL_ATTRS;
    private static AttributeSet CHAR_LITERAL_ATTRS;
    private static AttributeSet OPERATOR_ATTRS;
    private static AttributeSet IDENTIFIER_ATTRS;
    private static AttributeSet PARENTHESES_ATTRS;

    /**
     * Constructor for JavaSyntaxHighlightingKit.
     *
     * @param setDefault Set it true to use the default color scheme.
     */
    public JavaSyntaxHighlightingKit(boolean setDefault) {
        if (setDefault) {
            SyntaxColorManager.setColors("default");
        }
        KEYWORD_ATTRS = createAttributeSet(new Color((int) UIManager.get("KEYWORD_ATTRS")), true, false);
        STRING_LITERAL_ATTRS = createAttributeSet(new Color((int) UIManager.get("STRING_LITERAL_ATTRS")), false, false);
        COMMENT_ATTRS = createAttributeSet(new Color((int) UIManager.get("COMMENT_ATTRS")), false, true);
        NUMBER_LITERAL_ATTRS = createAttributeSet(new Color((int) UIManager.get("NUMBER_LITERAL_ATTRS")), false, false);
        CHAR_LITERAL_ATTRS = createAttributeSet(new Color((int) UIManager.get("CHAR_LITERAL_ATTRS")), false, false);
        OPERATOR_ATTRS = createAttributeSet(new Color((int) UIManager.get("OPERATOR_ATTRS")), false, false);
        IDENTIFIER_ATTRS = createAttributeSet(new Color((int) UIManager.get("IDENTIFIER_ATTRS")), false, false);
        PARENTHESES_ATTRS = createAttributeSet(new Color((int) UIManager.get("PARENTHESES_ATTRS")), false, false);
    }

    /**
     * Creates an attribute set with the specified foreground color, bold flag, and italic flag.
     *
     * @param color the foreground color
     * @param bold  the bold flag
     * @param italic the italic flag
     * @return the attribute set
     */
    private static AttributeSet createAttributeSet(Color color, boolean bold, boolean italic) {
        SimpleAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setForeground(attrs, color);
        StyleConstants.setBold(attrs, bold);
        StyleConstants.setItalic(attrs, italic);
        return attrs;
    }

    /**
     * Inserts a string into the document and updates the syntax highlighting.
     *
     * @param offs the offset at which to insert the string
     * @param str  the string to insert
     * @param a    the attribute set for the inserted string
     * @throws BadLocationException if the offset is invalid
     */
    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        super.insertString(offs, str, a);
        highlightSyntax();
    }

    /**
     * Removes a range of characters from the document and updates the syntax highlighting.
     *
     * @param offs the start offset of the range to remove
     * @param len  the number of characters to remove
     * @throws BadLocationException if the specified range is invalid
     */
    @Override
    public void remove(int offs, int len) throws BadLocationException {
        super.remove(offs, len);
        highlightSyntax();
    }

    /**
     * Updates the syntax highlighting for the code in the document.
     *
     * @throws BadLocationException if the specified range is invalid
     */
    private void highlightSyntax() throws BadLocationException {
        // Retrieve the entire text in the document as a string
        String code = getText(0, getLength());

        // Tokenize the code using the JavaSyntaxTokenizer class
        List<Token> tokens = JavaSyntaxTokenizer.tokenize(code);

        // Set the attributes for the entire document to the IDENTIFIER_ATTRS attribute set
        setCharacterAttributes(0, getLength(), PARENTHESES_ATTRS, false);

        // Iterate over the list of tokens
        for (Token token : tokens) {
            // Get the attributes for the token type
            AttributeSet attrs = getAttributesForTokenType(token.getType());

            // Apply the attributes to the token
            setCharacterAttributes(token.getRange().getStart(),
                    token.getRange().getEnd() - token.getRange().getStart(),
                    attrs, false);
        }
    }

    /**
     * Returns the attributes for the specified token type.
     *
     * @param type the token type
     * @return the attributes for the token type
     */
    private AttributeSet getAttributesForTokenType(TokenType type) {
        switch (type) {
            case KEYWORD:
                return KEYWORD_ATTRS;
            case STRING_LITERAL:
                return STRING_LITERAL_ATTRS;
            case COMMENT:
                return COMMENT_ATTRS;
            case NUMBER_LITERAL:
                return NUMBER_LITERAL_ATTRS;
            case CHAR_LITERAL:
                return CHAR_LITERAL_ATTRS;
            case OPERATOR:
                return OPERATOR_ATTRS;
            case IDENTIFIER:
                return IDENTIFIER_ATTRS;
            case PARENTHESES:
                return PARENTHESES_ATTRS;
            default:
                return IDENTIFIER_ATTRS;
        }
    }
}
