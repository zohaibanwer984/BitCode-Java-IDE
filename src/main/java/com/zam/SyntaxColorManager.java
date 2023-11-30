package com.zam;

import javax.swing.UIManager;

/**
 * A utility class for setting the colors of various syntax elements in the UI based on the current theme.
 */
public class SyntaxColorManager {

    /**
     * Sets the colors of syntax elements based on the given theme name.
     *
     * This class is responsible for managing the syntax highlighting colors of different elements
     * in the user interface based on the selected theme. It provides a method to set the colors
     * for keywords, string literals, comments, number literals, character literals, operators,
     * identifiers, and parentheses.
     *
     * Supported themes include:
     * - FlatDarkLaf
     * - FlatLightLaf
     * - FlatSolarizedDarkIJTheme
     * - FlatSolarizedLightIJTheme
     *
     * If the specified theme is not recognized, default colors are applied.
     *
     * @param theme the name of the theme for which the colors should be set
     */
    public static void setColors(String theme) {
        switch (theme) {
            case "com.formdev.flatlaf.FlatDarkLaf":
                // Set colors for the Flat Dark theme
                UIManager.put("KEYWORD_ATTRS", 0x569CD6);
                UIManager.put("STRING_LITERAL_ATTRS", 0xCE9178);
                UIManager.put("COMMENT_ATTRS", 0x6A9955);
                UIManager.put("NUMBER_LITERAL_ATTRS", 0xB5CEA8);
                UIManager.put("CHAR_LITERAL_ATTRS", 0xCE9178);
                UIManager.put("OPERATOR_ATTRS", 0xD4D4D4);
                UIManager.put("IDENTIFIER_ATTRS", 0xD4D4D4);
                UIManager.put("PARENTHESES_ATTRS", 0xFFD700);
                break;
            case "com.formdev.flatlaf.FlatLightLaf":
                // Set colors for the Flat Light theme
                UIManager.put("KEYWORD_ATTRS", 0xD73A49);
                UIManager.put("STRING_LITERAL_ATTRS", 0x032F62);
                UIManager.put("COMMENT_ATTRS", 0x6A737D);
                UIManager.put("NUMBER_LITERAL_ATTRS", 0x005CC5);
                UIManager.put("CHAR_LITERAL_ATTRS", 0x032F62);
                UIManager.put("OPERATOR_ATTRS", 0xD73A49);
                UIManager.put("IDENTIFIER_ATTRS", 0x6F42C1);
                UIManager.put("PARENTHESES_ATTRS", 0x005CC5);
                break;
            case "com.formdev.flatlaf.intellijthemes.FlatSolarizedDarkIJTheme":
                // Set colors for the Flat Solarized Dark theme
                UIManager.put("KEYWORD_ATTRS", 0x859900);
                UIManager.put("STRING_LITERAL_ATTRS", 0x2AA198);
                UIManager.put("COMMENT_ATTRS", 0x586E75);
                UIManager.put("NUMBER_LITERAL_ATTRS", 0xD33682);
                UIManager.put("CHAR_LITERAL_ATTRS", 0x2AA198);
                UIManager.put("OPERATOR_ATTRS", 0xD73A49);
                UIManager.put("IDENTIFIER_ATTRS", 0x93A1A1);
                UIManager.put("PARENTHESES_ATTRS", 0xCB4B16);
                break;
            case "com.formdev.flatlaf.intellijthemes.FlatSolarizedLightIJTheme":
                // Set colors for the Flat Solarized Light theme
                UIManager.put("KEYWORD_ATTRS", 0x586E75);
                UIManager.put("STRING_LITERAL_ATTRS", 0x2AA198);
                UIManager.put("COMMENT_ATTRS", 0x586E75);
                UIManager.put("NUMBER_LITERAL_ATTRS", 0x859900);
                UIManager.put("CHAR_LITERAL_ATTRS", 0x2AA198);
                UIManager.put("OPERATOR_ATTRS", 0xCB4B16);
                UIManager.put("IDENTIFIER_ATTRS", 0x268BD2);
                UIManager.put("PARENTHESES_ATTRS", 0x2B67FF);
                break;
            default:
                // Set default colors
                UIManager.put("KEYWORD_ATTRS", 0x000000);
                UIManager.put("STRING_LITERAL_ATTRS", 0x000000);
                UIManager.put("COMMENT_ATTRS", 0x6A737D);
                UIManager.put("NUMBER_LITERAL_ATTRS", 0x000000);
                UIManager.put("CHAR_LITERAL_ATTRS", 0x000000);
                UIManager.put("OPERATOR_ATTRS", 0x000000);
                UIManager.put("IDENTIFIER_ATTRS", 0x000000);
                UIManager.put("PARENTHESES_ATTRS", 0x000000);
        }
    }
}
