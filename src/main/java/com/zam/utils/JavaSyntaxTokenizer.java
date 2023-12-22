package com.zam.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The `JavaSyntaxTokenizer` class is a utility for tokenizing a Java code string into a list of tokens.
 * Each token represents a single lexical unit in the code, such as a keyword, string literal, comment,
 * number literal, operator, or identifier. The tokenizer uses regular expressions to identify and extract
 * the different types of tokens from the code string.
 *
 * Responsibilities:
 * - Tokenizing a Java code string into a list of tokens.
 * - Identifying and extracting keywords, string literals, comments, number literals, operators, identifiers,
 *   and parentheses from the code string.
 *
 * Usage:
 * - Call the static `tokenize` method, passing the Java code string as a parameter, to obtain a list of `Token` objects.
 *
 * Example:
 * ```java
 * String javaCode = "public class Example { public static void main(String[] args) { System.out.println(\"Hello, World!\"); } }";
 * List<JavaSyntaxTokenizer.Token> tokens = JavaSyntaxTokenizer.tokenize(javaCode);
 * ```
 *
 * @author Muahmmed Zohaib
 * @version 1.0
 * @since 2023-11-29
 */
public class JavaSyntaxTokenizer {
    // Regular expressions for different types of tokens
    private static final String KEYWORDS_REGEX = "\\b(abstract|assert|boolean|break|byte|case|catch|char|class|const|continue|default|do|double|else|enum|extends|final|finally|float|for|goto|if|implements|import|instanceof|int|interface|long|native|new|package|private|protected|public|return|short|static|strictfp|super|switch|synchronized|this|throw|throws|transient|try|void|volatile|while)\\b";
    private static final String STRING_LITERAL_REGEX = "\"([^\\\\\"\n]|\\\\[^\n])*\"";
    private static final String CHAR_LITERAL_REGEX = "'([^\\\\'\n]|\\\\[^\n])'";
    private static final String NUMBER_LITERAL_REGEX = "\\b(\\d+(\\.\\d+)?|\\.\\d+)([eE][+-]?\\d+)?\\b";
    private static final String COMMENT_REGEX = "//.*|/\\*(.|\\R)*?\\*/";
    private static final String OPERATOR_REGEX = "([+-/*%^&|~]|[<>]=?|!=|&&|=|\\|\\||\\?|:)";
    private static final String IDENTIFIER_REGEX = "\\b[A-Za-z_][A-Za-z0-9_]*\\b";
    private static final String PARENTHESES_REGEX = "\\[|\\]|\\{|\\}|\\(|\\)";

    private static final Pattern PATTERN = Pattern.compile(
            "(" + KEYWORDS_REGEX + ")"
            + "|(" + STRING_LITERAL_REGEX + ")"
            + "|(" + COMMENT_REGEX + ")"
            + "|(" + NUMBER_LITERAL_REGEX + ")"
            + "|(" + CHAR_LITERAL_REGEX + ")"
            + "|(" + OPERATOR_REGEX + ")"
            + "|(" + IDENTIFIER_REGEX + ")"
            + "|(" + PARENTHESES_REGEX + ")"
    );

    /**
     * Tokenizes the given Java code string into a list of tokens.
     *
     * @param code the Java code string to be tokenized
     * @return a list of Token objects representing the different lexical units in the code
     */
    public static List<Token> tokenize(String code) {
        List<Token> tokens = new ArrayList<>();
        Matcher matcher = PATTERN.matcher(code);
        while (matcher.find()) {
            if (matcher.group(1) != null){
                tokens.add(new Token(TokenType.KEYWORD, matcher.start(), matcher.end()));
            }
            else if (matcher.group(3) != null){
                tokens.add(new Token(TokenType.STRING_LITERAL, matcher.start(), matcher.end()));
            }
            else if (matcher.group(5) != null){
                tokens.add(new Token(TokenType.COMMENT, matcher.start(), matcher.end()));
            }
            else if (matcher.group(7) != null){
                tokens.add(new Token(TokenType.NUMBER_LITERAL, matcher.start(), matcher.end()));
            }
            else if (matcher.group(11) != null){
                tokens.add(new Token(TokenType.CHAR_LITERAL, matcher.start(), matcher.end()));
            }
            else if (matcher.group(13) != null){
                tokens.add(new Token(TokenType.OPERATOR, matcher.start(), matcher.end()));
            }
            else if (matcher.group(15) != null){
                tokens.add(new Token(TokenType.IDENTIFIER, matcher.start(), matcher.end()));
            }
            else if (matcher.group(16) != null){
                tokens.add(new Token(TokenType.PARENTHESES, matcher.start(), matcher.end()));
            }
            else{
                System.out.println("FOUND :" + code.substring(matcher.start(), matcher.end()));
            }
        }
        return tokens;
    }

    /**
     * Represents a token in a Java code string. A token is a single lexical unit in the code, such as a keyword,
     * string literal, comment, number literal, operator, or identifier.
     */
    public static class Token {
        // The type of the token
        private final TokenType type;
        // The range in the code string where the token appears
        private final Range range;

        /**
         * Creates a new Token object with the given type and range.
         *
         * @param type  the type of the token
         * @param start the start position of the token in the code string
         * @param end   the end position of the token in the code string
         */
        public Token(TokenType type, int start, int end) {
            this.type = type;
            this.range = new Range(start, end);
        }

        /**
         * Returns the type of the token.
         *
         * @return the type of the token
         */
        public TokenType getType() {
            return type;
        }

        /**
         * Returns the range in the code string where the token appears.
         *
         * @return the range in the code string where the token appears
         */
        public Range getRange() {
            return range;
        }
    }

    /**
     * Represents a range of positions in a string. The range is defined by its start and end positions, which are
     * inclusive.
     */
    public static class Range {
        // The start position of the range in the string
        private final int start;
        // The end position of the range in the string
        private final int end;

        /**
         * Creates a new Range object with the given start and end positions.
         *
         * @param start the start position of the range in the string
         * @param end   the end position of the range in the string
         */
        public Range(int start, int end) {
            this.start = start;
            this.end = end;
        }

        /**
         * Returns the start position of the range in the string.
         *
         * @return the start position of the range in the string
         */
        public int getStart() {
            return start;
        }

        /**
         * Returns the end position of the range in the string.
         *
         * @return the end position of the range in the string
         */
        public int getEnd() {
            return end;
        }
    }

    /**
     * Represents the different types of tokens that can appear in a Java code string.
     */
    public static enum TokenType {
        KEYWORD,
        STRING_LITERAL,
        CHAR_LITERAL,
        NUMBER_LITERAL,
        COMMENT,
        OPERATOR,
        IDENTIFIER,
        PARENTHESES
    }
}
