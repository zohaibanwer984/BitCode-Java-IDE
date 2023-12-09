package com.zam.components.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.undo.UndoManager;

import com.zam.ui.App;
import com.zam.utils.JavaSyntaxHighlightingKit;

/**
 * The `LineNumberPane` class is a custom Swing component that combines a text area for code editing
 * (using `JTextPane`) with a line number area to display line numbers. It also supports undo functionality
 * through `UndoManager` and syntax highlighting using a `JavaSyntaxHighlightingKit`.
 *
 * Responsibilities:
 * - Displaying a code editor with line numbers.
 * - Managing undo functionality.
 * - Applying syntax highlighting to the code.
 *
 * Usage:
 * - Create an instance of `LineNumberPane` and add it to your GUI.
 * - Use the `updateLineNumbers` method to update the line numbers based on the content.
 * - Use the `addUndoManager` method to enable undo functionality.
 * - Use the `addSyntaxHighlighter` method to enable syntax highlighting.
 *
 * Example:
 * ```java
 * LineNumberPane lineNumberPane = new LineNumberPane(mainApp);
 * lineNumberPane.addSyntaxHighlighter(true);
 * mainPanel.add(lineNumberPane, BorderLayout.CENTER);
 * ```
 *
 * @author Muhammed Zohaib
 * @version 1.0
 * @since 2023-11-29
 */
public class LineNumberPane extends JPanel {
    private JScrollPane scrollPane;
    private int lineCount;
    private JTextPane lineNumberArea;
    private Document lineDoc;
    private App mainApp;

    public JTextPane codetextPane;
    public UndoManager undoManager = new UndoManager(){
        @Override
        public void undoableEditHappened(UndoableEditEvent e)
        {
            AbstractDocument.DefaultDocumentEvent event =
                (AbstractDocument.DefaultDocumentEvent)e.getEdit();

            if  (event.getType().equals(DocumentEvent.EventType.CHANGE))
                return;
            else
                super.undoableEditHappened(e);
        }
    }; 

    /**
     * Creates a new instance of the `LineNumberPane`.
     *
     * @param parent the parent application or container
     */
    public LineNumberPane(App parent) {
        this.mainApp = parent;
        codetextPane = new JTextPane();
        scrollPane = new JScrollPane(codetextPane);

        lineNumberArea = new JTextPane();
        lineNumberArea.setEditable(false);
        lineNumberArea.setOpaque(true);
        lineNumberArea.setSize(new Dimension(50, codetextPane.getHeight()));
        lineNumberArea.setAutoscrolls(false);

        Border paddingBorder = BorderFactory.createEmptyBorder(0, 3, 0, 3);
        Border Border = BorderFactory.createEtchedBorder();
        lineNumberArea.setBorder(BorderFactory.createCompoundBorder(Border, paddingBorder));

        SimpleAttributeSet rightAlign = new SimpleAttributeSet();
        StyleConstants.setAlignment(rightAlign, StyleConstants.ALIGN_CENTER);
        lineNumberArea.setParagraphAttributes(rightAlign, true);

        scrollPane.getViewport().add(codetextPane);
        scrollPane.setRowHeaderView(lineNumberArea);
        setLayout(new BorderLayout());
        add(scrollPane);
        updateLineNumbers();
    }

    /**
     * Updates the line numbers based on the content of the code text area.
     */
    public void updateLineNumbers() {
        SimpleAttributeSet plain = new SimpleAttributeSet();
        StyleConstants.setFontFamily(plain, codetextPane.getFont().getFontName());
        StyleConstants.setFontSize(plain, codetextPane.getFont().getSize());
        StyleConstants.setLineSpacing(plain, 5.0F);
        StyleConstants.setForeground(plain, codetextPane.getForeground());
        lineDoc = lineNumberArea.getDocument();
        String text = codetextPane.getText();
        try {
            lineDoc.remove(0, lineDoc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        lineCount = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '\n') {
                lineCount++;
            }
        }
        lineCount++;
        StringBuilder lineNumbers = new StringBuilder();
        for (int i = 1; i <= lineCount; i++) {
            lineNumbers.append(i).append("\n");
        }
        try {
            lineDoc.insertString(lineDoc.getLength(), lineNumbers.toString(), plain);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds an `UndoManager` to enable undo functionality for the code text area.
     */
    public void addUndoManager() {
        codetextPane.getDocument().addUndoableEditListener(undoManager);
    }

    /**
     * Adds a syntax highlighter to the code text area.
     *
     * @param isNewFile true if the file is new, false otherwise
     */
    public void addSyntaxHighlighter(boolean isNewFile){
        String content = codetextPane.getText();
        codetextPane.setEditorKit(new StyledEditorKit() {
            @Override
            public javax.swing.text.Document createDefaultDocument() {
                return new JavaSyntaxHighlightingKit(false);
            }
        });
        codetextPane.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateLineNumbers();
                mainApp.tabbedEditorPane.setIconAt(App.currentTabIndex, App.jRedImage);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                mainApp.tabbedEditorPane.setIconAt(App.currentTabIndex, App.jRedImage);
                updateLineNumbers();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                //updateLineNumbers();
            }
        });
        codetextPane.setText(content);
        updateLineNumbers();
        addUndoManager();
        if (isNewFile == false){
            mainApp.tabbedEditorPane.setIconAt(App.currentTabIndex, App.jBlueImage);
        }
        else{
            mainApp.tabbedEditorPane.setIconAt(App.currentTabIndex, App.jRedImage);
        }
    }
}