package com.zam.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;

import com.zam.components.editor.CodeTextArea;
import com.zam.ui.App;

/**
 * Custom menu handler for the Edit menu in BitCode IDE.
 *
 * Responsibilities:
 * - Handling undo, redo, cut, copy, paste, and go-to-line operations.
 * - Integrating with the main application to perform actions on the current text area.
 *
 * Usage:
 * - Integrate into the main menu bar by creating an instance and adding to the appropriate menu.
 *
 * Example:
 * ```java
 * EditMenuHandler editMenu = new EditMenuHandler("Edit", mainApp);
 * mainMenuBar.add(editMenu);
 * ```
 *
 * @author Muhammed Zohaib
 * @version 1.0.2
 * @since 2023-12-12
 */
public class EditMenuHandler extends JMenu {

    private final JMenuItem undoItem = new JMenuItem("Undo");
    private final JMenuItem redoItem = new JMenuItem("Redo");
    private final JMenuItem cutItem = new JMenuItem("Cut");
    private final JMenuItem copyItem = new JMenuItem("Copy");
    private final JMenuItem pasteItem = new JMenuItem("Paste");
    private final JMenuItem gotoItem = new JMenuItem("Go-to line");

    private final App mainApp;

    /**
     * Constructor for the EditMenuHandler.
     *
     * @param title  The title of the menu.
     * @param parent The main App instance.
     */
    EditMenuHandler(String title, App parent) {
        super(title);

        this.mainApp = parent;

        // Add items to the Edit tab
        add(undoItem);
        add(redoItem);
        addSeparator(); // Add a separator line
        add(cutItem);
        add(copyItem);
        add(pasteItem);
        addSeparator(); // Add a separator line
        add(gotoItem);

        // Add action listeners and accelerators
        configureMenuItems();
    }

    /**
     * Configures action listeners and accelerators for menu items.
     */
    private void configureMenuItems() {
        // Add an action listener to the "Undo" menu item
        undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
        undoItem.addActionListener(e -> undo());

        // Add an action listener to the "Redo" menu item
        redoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
        redoItem.addActionListener(e -> redo());

        // Add an action listener to the "Cut" menu item
        cutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        cutItem.addActionListener(e -> mainApp.codeAreaPanes.get(App.currentTabIndex).codeTextArea.cut());

        // Add an action listener to the "Copy" menu item
        copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        copyItem.addActionListener(e -> mainApp.codeAreaPanes.get(App.currentTabIndex).codeTextArea.copy());

        // Add an action listener to the "Paste" menu item
        pasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        pasteItem.addActionListener(e -> mainApp.codeAreaPanes.get(App.currentTabIndex).codeTextArea.paste());

        // Add an action listener to the "GO-TO" menu item
        gotoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
        gotoItem.addActionListener(e -> gotoLine());
    }

    /**
     * Performs an undo operation on the current text area.
     */
    private void undo() {
        if (getCurrentCodePanel().codeTextArea.canUndo()){
            getCurrentCodePanel().codeTextArea.undoLastAction();
        }
    }

    /**
     * Performs a redo operation on the current text area.
     */
    private void redo(){
        if (getCurrentCodePanel().codeTextArea.canRedo()){
            getCurrentCodePanel().codeTextArea.redoLastAction();
        }
    }

    /**
     * Opens a dialog to get the line number input from the user and moves the caret to that line.
     * Highlights the selected line for 1 second.
     */
    private void gotoLine() {
        String input = JOptionPane.showInputDialog(mainApp, "Enter line number:", "Go to line", JOptionPane.QUESTION_MESSAGE);
        if (input != null) {
            try {
                // Parse the input string to an integer
                int lineNumber = Integer.parseInt(input);
                if(lineNumber <= 0){
                    return;
                }
                CodeTextArea cp = getCurrentCodePanel();
                if (lineNumber > cp.codeTextArea.getLineCount()){
                    return;
                }
                cp.codeTextArea.setCaretPosition(getCurrentCodePanel().codeTextArea.getLineStartOffset(lineNumber-1));
            } catch (BadLocationException ble) { // Never happens
                UIManager.getLookAndFeel().provideErrorFeedback(getCurrentCodePanel().codeTextArea);
                ble.printStackTrace();
            }
        }
    }
    private CodeTextArea getCurrentCodePanel(){
        return mainApp.codeAreaPanes.get(App.currentTabIndex);
    }
}
