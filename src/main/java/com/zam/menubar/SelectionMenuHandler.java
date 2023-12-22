package com.zam.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;

import com.zam.ui.App;

/**
 * Custom menu handler for the Selection menu in BitCode IDE.
 *
 * Responsibilities:
 * - Handling text selection operations.
 * - Handling comment toggling operations.
 * - Integrating with the main application to perform actions on the current text area.
 *
 * Usage:
 * - Integrate into the main menu bar by creating an instance and adding to the appropriate menu.
 *
 * Example:
 * ```java
 * SelectionMenuHandler selectionMenu = new SelectionMenuHandler("Selection", mainApp);
 * mainMenuBar.add(selectionMenu);
 * ```
 *
 * @author Muhammed Zohaib
 * @version 1.0
 * @since 2023-12-12
 */
public class SelectionMenuHandler extends JMenu {

    private final JMenuItem selectAllItem = new JMenuItem("Select All");
    private final JMenuItem deselectAllItem = new JMenuItem("Deselect All");
    private final JMenuItem toggleLineCommentItem = new JMenuItem("Toggle Comment");
    private final JMenuItem toggleBlockCommentItem = new JMenuItem("Toggle Block Comment");

    final private App mainApp;

    /**
     * Constructor for the SelectionMenuHandler.
     *
     * @param title  The title of the menu.
     * @param parent The main App instance.
     */
    public SelectionMenuHandler(String title, App parent) {
        super(title);

        this.mainApp = parent;
        // Add items to the Selection tab
        add(selectAllItem);
        add(deselectAllItem);
        addSeparator(); // Add a separator line
        add(toggleLineCommentItem);
        add(toggleBlockCommentItem);

        // Add action listeners and accelerators
        configureMenuItems();
    }

    /**
     * Configures action listeners and accelerators for menu items.
     */
    private void configureMenuItems() {
        // Add an action listener to the "Select All" menu item
        selectAllItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        selectAllItem.addActionListener(e -> selectAll(mainApp.lineNumberPanes.get(App.currentTabIndex).codetextPane));

        // Add an action listener to the "Deselect All" menu item
        deselectAllItem.setAccelerator(KeyStroke.getKeyStroke("ESCAPE"));
        deselectAllItem.addActionListener(e -> deselectAll(mainApp.lineNumberPanes.get(App.currentTabIndex).codetextPane));

        // Add an action listener to the "Toggle Comment" menu item
        toggleLineCommentItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, ActionEvent.CTRL_MASK));
        toggleLineCommentItem.addActionListener(e -> toggleComment(mainApp.lineNumberPanes.get(App.currentTabIndex).codetextPane));

        // Add an action listener to the "Toggle Block Comment" menu item
        toggleBlockCommentItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK | ActionEvent.SHIFT_MASK));
        toggleBlockCommentItem.addActionListener(e -> toggleBlockComment(mainApp.lineNumberPanes.get(App.currentTabIndex).codetextPane));
    }

    /**
     * Selects the entire content of the current text area.
     */
    public void selectAll(JTextPane codeTextArea) {
        codeTextArea.setSelectionStart(0);
        codeTextArea.setSelectionEnd(codeTextArea.getText().length());
        codeTextArea.requestFocus();
    }

    /**
     * Deselects any selected text in the current text area.
     */
    public void deselectAll(JTextPane codeTextArea) {
        codeTextArea.setSelectionStart(codeTextArea.getSelectionEnd());
        codeTextArea.requestFocus();
    }

    /**
     * Toggles the comment status of the selected text in the code area.
     * If the selected text is already commented, removes the "//" from each line.
     * If not commented, adds "//" at the beginning of each line.
     */
    public void toggleComment(JTextPane codeTextArea) {
        String selectedText = codeTextArea.getSelectedText();
        if (selectedText == null) {
            return;
        }
        boolean isCommented = selectedText.startsWith("//");

        if (isCommented) {
            selectedText = selectedText.replaceAll("(?m)^(\\s*)//\\s?", "$1");
        } else {
            selectedText = selectedText.replaceAll("(?m)^(\\s*)", "$1//");
        }

        codeTextArea.replaceSelection(selectedText);
    }

    /**
     * Toggles the block comment status of the selected text in the code area.
     * If the selected text is already commented, removes the comment block from the text.
     * If not commented, adds block comment at the beginning and at the end of the text.
     */
    public void toggleBlockComment(JTextPane codeTextArea) {
        String selectedText = codeTextArea.getSelectedText();
        if (selectedText == null) {
            return;
        }
        boolean isCommented = selectedText.startsWith("/*") && selectedText.endsWith("*/");

        if (isCommented) {
            selectedText = selectedText.replaceAll("(?m)^(\\s*)/\\*\\s?|\\s?\\*/\\s*$", "$1");
        } else {
            selectedText = "/* " + selectedText + "*/";
        }

        codeTextArea.replaceSelection(selectedText);
    }
}
