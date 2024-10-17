package com.zam.components.editor;

import java.awt.BorderLayout;
import java.awt.Font;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;

import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.CompletionCellRenderer;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;

import com.zam.ui.App;
import com.zam.utils.JavaCompletionProvider;

/**
 * Custom JPanel for creating a code editor area using RSyntaxTextArea.
 *
 * Responsibilities:
 * - Initializing RSyntaxTextArea and RTextScrollPane.
 * - Applying syntax highlighting based on the selected theme.
 * - Configuring various code editor properties such as auto-indentation, bracket matching, etc.
 *
 * Usage:
 * ```java
 * CodeTextArea codeTextArea = new CodeTextArea(mainApp);
 * ```
 *
 * @author Muhammed Zohaib
 * @version 1.0.3.1
 * @since 2024-01-06
 */
public class CodeTextArea extends JPanel {

    public RSyntaxTextArea codeTextArea;
    private RTextScrollPane codePane;

    private App mainApp;

    /**
     * Constructor for CodeTextArea.
     *
     * @param parent The main App instance.
     */
    public CodeTextArea(App parent) {

        this.mainApp = parent;

        // Initialize RSyntaxTextArea with rows and columns
        codeTextArea = new RSyntaxTextArea(20, 40);
        codeTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);

        // Initialize RTextScrollPane to host RSyntaxTextArea
        codePane = new RTextScrollPane(codeTextArea);

        // Load and apply the selected theme for syntax highlighting
        String themeName = mainApp.properties.getProperty("editorTheme");
        try {
            Theme theme = Theme.load(getClass().getResourceAsStream("/SyntaxThemes/" + themeName + ".xml"));
            theme.apply(codeTextArea);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set code area font and gutter font
        codeTextArea.setFont(UIManager.getFont("AppFont").deriveFont(mainApp.textAttributes));
        setGutterFont(UIManager.getFont("AppFont"));

        // Configure various code editor properties
        codeTextArea.setAutoIndentEnabled(true);
        codeTextArea.setBracketMatchingEnabled(mainApp.properties.getBooleanProperty("BracketMatching"));
        codeTextArea.setHighlightCurrentLine(mainApp.properties.getBooleanProperty("HighlightCurrentLine"));
        codeTextArea.setCodeFoldingEnabled(mainApp.properties.getBooleanProperty("CodeFolding"));
        codePane.setLineNumbersEnabled(mainApp.properties.getBooleanProperty("LineNumbers"));
        codeTextArea.setLineWrap(mainApp.properties.getBooleanProperty("LineWrap"));

        // Enable tooltips for code editor
        ToolTipManager.sharedInstance().registerComponent(codeTextArea);

        // Created Basic Completion Provider
        CompletionProvider provider = new JavaCompletionProvider();
        provider.setListCellRenderer(new CompletionCellRenderer());

        // Adding Provider to AutoCompletion
        AutoCompletion ac = new AutoCompletion(provider);
        ac.setAutoCompleteEnabled(true);
        ac.setParameterAssistanceEnabled(true);
        ac.setAutoCompleteSingleChoices(true);
        ac.setAutoActivationEnabled(true);
        ac.setAutoActivationDelay(10);
        ac.install(codeTextArea);

        // Set layout and add the code pane to the panel
        setLayout(new BorderLayout());
        add(codePane);
    }

    /**
     * Set the font for the gutter line numbers.
     *
     * @param font The font to set for the gutter.
     */
    public void setGutterFont(Font font) {
        codePane.getGutter().setLineNumberFont(font);
    }

    /**
     * Enable or disable line numbers in the code editor.
     *
     * @param enable Boolean value to enable or disable line numbers.
     */
    public void setLineNumbersEnabled(Boolean enable) {
        codePane.setLineNumbersEnabled(enable);
    }

    /**
     * Apply the selected theme to the code editor.
     *
     * @param textArea The RSyntaxTextArea instance to apply the theme to.
     */
    public void applyTheme(RSyntaxTextArea textArea) {
        Theme theme = new Theme(textArea);
        theme.apply(codeTextArea);
    }
}
