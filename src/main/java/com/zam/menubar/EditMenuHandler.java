package com.zam.menubar;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Utilities;

import com.zam.components.editor.LineNumberPane;
import com.zam.ui.App;

// Wrote on 12 - DEC - 2023

public class EditMenuHandler extends JMenu{

    private final JMenuItem undoItem = new JMenuItem("Undo");
    private final JMenuItem redoItem = new JMenuItem("Redo");
    private final JMenuItem cutItem = new JMenuItem("Cut");
    private final JMenuItem copyItem = new JMenuItem("Copy");
    private final JMenuItem pasteItem = new JMenuItem("Paste");
    private final JMenuItem gotoItem = new JMenuItem("Go-to line");

    final private App mainApp;

    EditMenuHandler(String title, App parent){
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
        // Add an action listener to the "Undo" menu item
        undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
        undoItem.addActionListener(e -> undo());

        // Add an action listener to the "Redo" menu item
        redoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
        redoItem.addActionListener(e -> redo());

        // Add an action listener to the "Cut" menu item
        cutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        cutItem.addActionListener(e -> mainApp.lineNumberPanes.get(App.currentTabIndex).codetextPane.cut());

        // Add an action listener to the "Copy" menu item
        copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        copyItem.addActionListener(e -> mainApp.lineNumberPanes.get(App.currentTabIndex).codetextPane.copy());

        // Add an action listener to the "Paste" menu item
        pasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        pasteItem.addActionListener(e -> mainApp.lineNumberPanes.get(App.currentTabIndex).codetextPane.paste());

        // Add an action listener to the "GO-TO" menu item
        gotoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
        gotoItem.addActionListener(e -> gotoLine(mainApp.lineNumberPanes.get(App.currentTabIndex).codetextPane));

    }
    /**
     * Performs an undo operation on the current text area.
     */
    public void undo() {
        LineNumberPane singlePane = mainApp.lineNumberPanes.get(App.currentTabIndex);
        if (singlePane.undoManager.canUndo()) {
            singlePane.undoManager.undo();
        }
    }

    /**
     * Performs a redo operation on the current text area.
     */
    public void redo() {
        LineNumberPane singlePane = mainApp.lineNumberPanes.get(App.currentTabIndex);
        if (singlePane.undoManager.canRedo()) {
            singlePane.undoManager.redo();
        }
    }

    /**
     * Opens a dialog to get the line number input from the user and moves the caret to that line.
     * Highlights the selected line for 1 second.
     */
    public void gotoLine(JTextPane codeTextArea) {
        // Show a dialog box to get the line number input
        String input = JOptionPane.showInputDialog(mainApp, "Enter line number:", "Go to line", JOptionPane.QUESTION_MESSAGE);
        if (input != null) {
            try {
                // Parse the input string to an integer
                int lineNumber = Integer.parseInt(input);
                // Set the selection color to gray
                codeTextArea.setSelectionColor(Color.GRAY);

                // Select the line
                int pos = 0;
                for (int i = 1; i < lineNumber; i++) {
                    pos = Utilities.getRowEnd(codeTextArea, pos) + 1;
                }
                int start = pos;
                int end = Utilities.getRowEnd(codeTextArea, pos);
                // Create a highlight painter to use for the highlighting
                Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.GRAY);

                // Highlight the line using the highlight painter
                codeTextArea.getHighlighter().addHighlight(start, end, painter);
                codeTextArea.requestFocus();
                // Schedule a task to remove the highlight after 1 second
                Timer timer = new Timer(1000, e -> codeTextArea.getHighlighter().removeAllHighlights());
                timer.setRepeats(false); // Make the timer stop after one execution
                timer.start();

                // Move the caret to the position of the line number
                codeTextArea.setCaretPosition(pos);
            } catch (NumberFormatException | BadLocationException ex) {
                // Handle invalid input or line number
                JOptionPane.showMessageDialog(mainApp, "Invalid input. Enter a valid line number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}