package com.zam.components.terminal;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import com.zam.ui.App;

/**
 * Custom component representing a terminal in BitCode IDE.
 *
 * Responsibilities:
 * - Displaying terminal output and errors.
 * - Accepting and executing commands.
 * - Providing a progress bar for background tasks.
 *
 * Usage:
 * - Integrate into the main UI by instantiating and adding to a container.
 * - Use `showProgressBar` and `hideProgressBar` methods to control the visibility of the progress bar.
 *
 * Example:
 * ```java
 * Terminal terminal = new Terminal(mainApp);
 * mainContainer.add(terminal, BorderLayout.SOUTH);
 * ```
 *
 * @author Muhammed Zohaib Anwer
 * @version 1.0
 * @since 2023-12-05
 */
public class Terminal extends JPanel {
    public int lastCaretPos = 0;
    public final JTextArea consolArea;

    private final JScrollPane consolePane;
    private JProgressBar progressBar;

    private App mainApp;

    /**
     * Constructor for the Terminal component.
     *
     * @param parent The main App instance.
     */
    public Terminal(App parent) {
        this.mainApp = parent;

        // Initialize text area for terminal output
        consolArea = new JTextArea("BitCode Terminal Errors And Code will be executed here..\n>>");
        consolArea.setFont(App.font);
        lastCaretPos = consolArea.getCaretPosition();
        consolArea.setCaretColor(Color.WHITE);
        consolArea.setForeground(Color.WHITE);
        consolArea.setBackground(Color.BLACK);
        consolArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
        consolArea.setSize(800, 300);
        consolArea.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 5));
        consolArea.setEditable(false);
        consolArea.setAutoscrolls(true);

        // Set up document filter for caret protection
        AbstractDocument doc = (AbstractDocument) consolArea.getDocument();
        doc.setDocumentFilter(new CaretProtectionFilter(lastCaretPos));

        // Override Enter key behavior to execute commands
        ActionMap am = consolArea.getActionMap();
        Action oldAction = am.get("insert-break");
        am.put("insert-break", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (consolArea.isEditable()) {
                    try {
                        int caretPos = consolArea.getCaretPosition();
                        String command = consolArea.getText(lastCaretPos, caretPos - lastCaretPos).trim();
                        lastCaretPos = caretPos;
                        // System.out.println("Command :" + command);
                        mainApp.menuBar.runMenu.runner.write(command + "\n");
                    } catch (BadLocationException | IOException ex) {
                        ex.printStackTrace();
                    }
                }
                oldAction.actionPerformed(e);
            }
        });

        // Set up scrollable console pane
        consolePane = new JScrollPane(consolArea);
        consolePane.setAutoscrolls(true);

        // Set up progress bar
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);

        // Set layout
        setLayout(new BorderLayout());
        add(consolePane, BorderLayout.CENTER);
        add(progressBar, BorderLayout.NORTH);
    }

    /**
     * Displays the progress bar.
     */
    public void showProgressBar() {
        progressBar.setVisible(true);
    }

    /**
     * Hides the progress bar.
     */
    public void hideProgressBar() {
        progressBar.setVisible(false);
    }

    /**
     * Document filter to protect caret position.
     */
    class CaretProtectionFilter extends DocumentFilter {
        private final int lastCaretPos;

        public CaretProtectionFilter(int lastCaretPos) {
            this.lastCaretPos = lastCaretPos;
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            if (offset >= lastCaretPos) {
                super.remove(fb, offset, length);
            }
        }
    }
}
