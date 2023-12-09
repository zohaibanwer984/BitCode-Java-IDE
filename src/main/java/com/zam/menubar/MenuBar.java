package com.zam.menubar;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Utilities;

import com.zam.components.editor.EditorTabPane;
import com.zam.components.editor.LineNumberPane;
import com.zam.dialogboxes.ThemeDialog;
import com.zam.ui.App;
import com.zam.utils.CommandLine;

/**
 * The `MenuBar` class represents the menu bar for the code editor application.
 * It provides various menu options for file operations, editing, selection, view settings,
 * and running and compiling code.
 *
 * Responsibilities:
 * - Handling file operations (new, open, save, save as, exit).
 * - Performing editing operations (undo, redo, cut, copy, paste, go to line).
 * - Handling selection operations (select all, deselect all, toggle comment, toggle block comment).
 * - Managing view settings (font size, theme).
 * - Running and compiling code.
 *
 * Usage:
 * - Create an instance of `MenuBar` and add it to your application's main frame.
 *
 * Example:
 * ```java
 * EditorTabPane tabbedEditorPane = new EditorTabPane();
 * List<LineNumberPane> lineNumberPanes = new ArrayList<>();
 * JTextArea terminalArea = new JTextArea();
 * App mainApp = new App();
 * MenuBar menuBar = new MenuBar(tabbedEditorPane, lineNumberPanes, terminalArea, mainApp);
 * mainApp.setJMenuBar(menuBar);
 * ```
 *
 * @author Your Name
 * @version 1.0
 * @since yyyy-mm-dd
 */
public class MenuBar extends JMenuBar {

    private boolean isCompiled = false;
    private List<LineNumberPane> lineNumberPanes;
    private JTextArea terminaltextArea;
    private EditorTabPane tabbedEditorPane;
    private App mainApp;

    public static int untitledCount = 0;

    /**
     * Creates a new instance of the `MenuBar`.
     *
     * @param tabbedEditorPane The editor tab pane for managing code tabs.
     * @param lineNumberPanes  The list of line number panes associated with code tabs.
     * @param terminalArea     The text area for displaying terminal output.
     * @param parent           The parent application or container.
     */
    public MenuBar(EditorTabPane tabbedEditorPane, List<LineNumberPane> ApplineNumberPanes, JTextArea terminalArea, App parent) {
        this.tabbedEditorPane = tabbedEditorPane;
        this.lineNumberPanes = ApplineNumberPanes;
        this.terminaltextArea = terminalArea;
        this.mainApp = parent;
        
        // Create the File tab
        JMenu fileMenu = new JMenu("File");
        // Add items to the File tab
        fileMenu.add(new JMenuItem("New"));
        fileMenu.add(new JMenuItem("Open"));
        fileMenu.add(new JMenuItem("Save"));
        fileMenu.add(new JMenuItem("Save As"));
        fileMenu.addSeparator(); // Add a separator line
        fileMenu.add(new JMenuItem("Exit"));
        // Add the File tab to the menu bar
        add(fileMenu);
        
        // Create the Edit tab
        JMenu editMenu = new JMenu("Edit");
        // Add items to the Edit tab
        editMenu.add(new JMenuItem("Undo"));
        editMenu.add(new JMenuItem("Redo"));
        editMenu.addSeparator(); // Add a separator line
        editMenu.add(new JMenuItem("Cut"));
        editMenu.add(new JMenuItem("Copy"));
        editMenu.add(new JMenuItem("Paste"));
        editMenu.addSeparator(); // Add a separator line
        editMenu.add(new JMenuItem("Go-to line")); // Add the "Go to line" menu item
        // Add the Edit tab to the menu bar
        add(editMenu);
        
        // Create the Selection tab
        JMenu selectionMenu = new JMenu("Selection");
        // Add items to the Selection tab
        selectionMenu.add(new JMenuItem("Select All"));
        selectionMenu.add(new JMenuItem("Deselect All"));
        selectionMenu.addSeparator();
        selectionMenu.add(new JMenuItem("Toggle Comment"));
        selectionMenu.add(new JMenuItem("Toggle Block Comment"));
        // Add the Selection tab to the menu bar
        add(selectionMenu);

        // Create the View tab
        JMenu viewMenu = new JMenu("View");
        // Add items to the View tab
        viewMenu.add(new JMenuItem("Increase Font Size"));
        viewMenu.add(new JMenuItem("Decrease Font Size"));
        viewMenu.add(new JMenuItem("Change Theme"));

        // Add the View tab to the menu bar
        add(viewMenu);
        
        // Create the Run tab
        JMenu runMenu = new JMenu("Run");
        // Add items to the Run tab
        runMenu.add(new JMenuItem("Compile"));
        runMenu.add(new JMenuItem("Run"));
        // Add the Run tab to the menu bar
        add(runMenu);

        // Add an action listener to the "New" menu item
        JMenuItem newMenuItem = fileMenu.getItem(0);
        newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        newMenuItem.addActionListener(e -> newFile());

        // Add an action listener to the "Open" menu item
        JMenuItem openMenuItem = fileMenu.getItem(1);
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        openMenuItem.addActionListener(e -> openFile());

        // Add an action listener to the "Save" menu item
        JMenuItem saveMenuItem = fileMenu.getItem(2);
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        saveMenuItem.addActionListener(e -> saveFile(lineNumberPanes.get(App.currentTabIndex).codetextPane));

        // Add an action listener to the "Save As" menu item
        JMenuItem saveAsMenuItem = fileMenu.getItem(3);
        saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
        saveAsMenuItem.addActionListener(e -> saveAsFile(lineNumberPanes.get(App.currentTabIndex).codetextPane));

        // Add an action listener to the "Save As" menu item
        JMenuItem exitMenuItem = fileMenu.getItem(5);
        exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
        exitMenuItem.addActionListener(e -> System.exit(0));

        // Add an action listener to the "Undo" menu item
        JMenuItem undoMenuItem = editMenu.getItem(0);
        undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
        undoMenuItem.addActionListener(e -> undo());

        // Add an action listener to the "Redo" menu item
        JMenuItem redoMenuItem = editMenu.getItem(1);
        redoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
        redoMenuItem.addActionListener(e -> redo());

        // Add an action listener to the "Cut" menu item
        JMenuItem cutMenuItem = editMenu.getItem(3);
        cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        cutMenuItem.addActionListener(e -> lineNumberPanes.get(App.currentTabIndex).codetextPane.cut());

        // Add an action listener to the "Copy" menu item
        JMenuItem copyMenuItem = editMenu.getItem(4);
        copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        copyMenuItem.addActionListener(e -> lineNumberPanes.get(App.currentTabIndex).codetextPane.copy());

        // Add an action listener to the "Paste" menu item
        JMenuItem pasteMenuItem = editMenu.getItem(5);
        pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        pasteMenuItem.addActionListener(e -> lineNumberPanes.get(App.currentTabIndex).codetextPane.paste());

        // Add an action listener to the "GO-TO" menu item
        JMenuItem gotoMenuItem = editMenu.getItem(7);
        gotoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
        gotoMenuItem.addActionListener(e -> gotoLine(lineNumberPanes.get(App.currentTabIndex).codetextPane));

        // Add an action listener to the "Select All" menu item
        JMenuItem selectAllMenuItem = selectionMenu.getItem(0);
        selectAllMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        selectAllMenuItem.addActionListener(e -> selectAll(lineNumberPanes.get(App.currentTabIndex).codetextPane)); 
        
        // Add an action listener to the "Deselect All" menu item
        JMenuItem deselectAllMenuItem = selectionMenu.getItem(1);
        deselectAllMenuItem.setAccelerator(KeyStroke.getKeyStroke("ESCAPE"));
        deselectAllMenuItem.addActionListener(e -> deselectAll(lineNumberPanes.get(App.currentTabIndex).codetextPane));

        // Add an action listener to the "Toggle Comment" menu item
        JMenuItem ToggleCommentMenuItem = selectionMenu.getItem(3);
        ToggleCommentMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, ActionEvent.CTRL_MASK));
        ToggleCommentMenuItem.addActionListener(e -> toggleComment(lineNumberPanes.get(App.currentTabIndex).codetextPane));

        // Add an action listener to the "Toggle Block Comment" menu item
        JMenuItem ToggleBlockCommentMenuItem = selectionMenu.getItem(4);
        ToggleBlockCommentMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK | ActionEvent.SHIFT_MASK));
        ToggleBlockCommentMenuItem.addActionListener(e -> toggleBlockComment(lineNumberPanes.get(App.currentTabIndex).codetextPane));

        // Add an action listener to the "Increase Font" menu item
        JMenuItem increaseFontSizeMenuItem = viewMenu.getItem(0);
        increaseFontSizeMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, ActionEvent.CTRL_MASK));
        increaseFontSizeMenuItem.addActionListener(e -> increaseFontSize(lineNumberPanes.get(App.currentTabIndex).codetextPane));

        // Add an action listener to the "Decrease Font" menu item
        JMenuItem decreaseFontSizeMenuItem = viewMenu.getItem(1);
        decreaseFontSizeMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, ActionEvent.CTRL_MASK));
        decreaseFontSizeMenuItem.addActionListener(e -> decreaseFontSize(lineNumberPanes.get(App.currentTabIndex).codetextPane));

        // Add an action listener to the "Change Theme" menu item
        JMenuItem changeThemeMenuItem = viewMenu.getItem(2);
        changeThemeMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
        changeThemeMenuItem.addActionListener(e -> toggleDarkMode(true));

        // Add an action listener to the "Compile" menu item
        JMenuItem compileMenuItem = runMenu.getItem(0);
        compileMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        compileMenuItem.addActionListener(e -> compileCode(lineNumberPanes.get(App.currentTabIndex).codetextPane));

        // Add an action listener to the "Compile" menu item
        JMenuItem runMenuItem = runMenu.getItem(1);
        runMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0));
        runMenuItem.addActionListener(e -> runCode());
    }
    /**
     * Opens and loads the content of a file into the specified JTextPane.
     *
     * @param fileLocation The path to the file to be opened.
     * @param codeTextArea The JTextPane where the content of the file will be loaded.
    */
    public void fileOpener(String fileLocation, JTextPane codeTextArea){
        // Create a File object from the provided file location
        File file = new File(fileLocation);

        try {
            // Read all bytes from the file and convert them to a string
            byte[] fileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
            String fileContent = new String(fileBytes);

            // Set the content of the JTextPane to the content of the file
            codeTextArea.setText(fileContent);
        } catch (IOException e) {
            // Handle the exception by printing the stack trace
            e.printStackTrace();
        }

        // Update the currentTabFile and isCompiled status
        App.currentTabFile = file;
        isCompiled = false;
    }
    /**
     * Opens a file using a file chooser dialog and loads its content.
     * Updates the currentTabFile and sets isCompiled to false.
     */
    public void openFile() {
        // Create a file chooser dialog
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Open Source File");

        // Show the open file dialog and get the user's selection
        int result = fileChooser.showOpenDialog(mainApp);

        // Check if the user selected a file
        if (result == JFileChooser.APPROVE_OPTION) {
            // Update the currentTabFile with the selected file
            App.currentTabFile = fileChooser.getSelectedFile();

            // Load the content of the selected file
            loadFile();

            // Set isCompiled to false
            isCompiled = false;
        }
    }
    /**
     * Loads the content of the currentTabFile into the editor.
     * Adds a new code area tab, sets its content, and adds syntax highlighting.
     */
    public void loadFile() {
        try {
            // Read all bytes from the currentTabFile
            byte[] fileBytes = Files.readAllBytes(Paths.get(App.currentTabFile.getAbsolutePath()));

            // Convert the byte array to a string (file content)
            String fileContent = new String(fileBytes);

            // Add a new code area tab with the file name, path, and content
            tabbedEditorPane.addCodeAreaTab(
                App.currentTabFile.getName(),
                App.jBlueImage,
                App.currentTabFile.getPath(),
                fileContent
            );

            // Get the LineNumberPane associated with the new tab
            LineNumberPane lineNumberPane = lineNumberPanes.get(lineNumberPanes.size() - 1);

            // Add syntax highlighting to the code area
            lineNumberPane.addSyntaxHighlighter(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new untitled file with default code and adds it to the editor.
     * Increments the untitledCount.
     */
    public void newFile() {
        final String code = 
            "/*\n * Save this file as Main.java before compiling\n*/" +
            "\npublic class Main {\n" +
            "    public static void main(String[] args) {\n" +
            "        //Start your code here\n" +
            "    }\n" +
            "}";
        tabbedEditorPane.addCodeAreaTab("untitled " + untitledCount, App.jRedImage, "untitled", code);
        LineNumberPane lineNumberPane = lineNumberPanes.get(lineNumberPanes.size() - 1);
        lineNumberPane.addSyntaxHighlighter(true);
        isCompiled = false;
        untitledCount++;
    }

    /**
     * Saves the content of the codeTextArea to a new file selected by the user.
     * The file is chosen using a file dialog.
     */
    public void saveAsFile(JTextPane codeTextArea) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save As");
        int result = fileChooser.showSaveDialog(mainApp);
        if (result == JFileChooser.APPROVE_OPTION) {
            App.currentTabFile = fileChooser.getSelectedFile();
            String fileContent = codeTextArea.getText();
            try {
                Files.write(Paths.get(App.currentTabFile.getAbsolutePath()), fileContent.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Saves the content of the codeTextArea to the currentTabFile.
     * If the current file is untitled, prompts the user to choose a location to save the file.
     * Updates the editor's tab title, tooltip, and icon.
     */
    public void saveFile(JTextPane codeTextArea) {
        String currentFileName = null, currentFileLocation = null;
        try {
            currentFileName = App.currentTabFile.getName();
            currentFileLocation = App.currentTabFile.getPath();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (currentFileName == null || currentFileName.equals("untitled") || currentFileLocation == null) {
            // No current file or untitled file - show the file chooser
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save File");
            int result = fileChooser.showSaveDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                App.currentTabFile = fileChooser.getSelectedFile();
                System.out.println("CurrentFile: " + App.currentTabFile);
                currentFileName = App.currentTabFile.getName();
                currentFileLocation = App.currentTabFile.getParent();
            } else {
                // User cancelled - return without saving
                return;
            }
        }

        // Save the file using the current file name and location
        String fileContent = codeTextArea.getText();
        try {
            Files.write(App.currentTabFile.toPath(), fileContent.getBytes());
            tabbedEditorPane.setTitleAt(App.currentTabIndex, currentFileName);
            tabbedEditorPane.setToolTipTextAt(App.currentTabIndex, currentFileLocation);
            tabbedEditorPane.setIconAt(App.currentTabIndex, App.jBlueImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Performs an undo operation on the current text area.
     */
    public void undo() {
        LineNumberPane singlePane = lineNumberPanes.get(App.currentTabIndex);
        if (singlePane.undoManager.canUndo()) {
            singlePane.undoManager.undo();
        }
    }

    /**
     * Performs a redo operation on the current text area.
     */
    public void redo() {
        LineNumberPane singlePane = lineNumberPanes.get(App.currentTabIndex);
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
        boolean isCommented = selectedText.startsWith("/*") && selectedText.endsWith("*/");

        if (isCommented) {
            selectedText = selectedText.replaceAll("(?m)^(\\s*)/\\*\\s?|\\s?\\*/\\s*$", "$1");
        } else {
            selectedText = "/* " + selectedText + "*/";
        }

        codeTextArea.replaceSelection(selectedText);
    }

    /**
     * Increases the font size of the code area by 2.
     */
    public void increaseFontSize(JTextPane codeTextArea) {
        Font font = new Font(codeTextArea.getFont().getName(), Font.PLAIN, codeTextArea.getFont().getSize() + 2);
        codeTextArea.setFont(font);
    }

    /**
     * Decreases the font size of the code area by 2, with a minimum font size of 6.
     */
    public void decreaseFontSize(JTextPane codeTextArea) {
        int newSize = Math.max(6, codeTextArea.getFont().getSize() - 2);
        Font font = new Font(codeTextArea.getFont().getName(), Font.PLAIN, newSize);
        codeTextArea.setFont(font);
    }

    /**
     * Opens a theme selection dialog to toggle dark mode.
     */
    public void toggleDarkMode(boolean enabled) {
        ThemeDialog themeDialog = new ThemeDialog(mainApp);
        themeDialog.setVisible(true);
    }

    /**
     * Compiles the code in the code area in a separate thread.
     * Displays compilation output in the terminal text area.
     */
    public void compileCode(JTextPane codeTextArea) {
        Thread t = new Thread(() -> {
            codeTextArea.setEnabled(false);
            String name = App.currentTabFile.getName();

            if (name.length() == 0) {
                JOptionPane.showMessageDialog(mainApp, "NO FILE OPENED", "ERROR", JOptionPane.ERROR_MESSAGE);
                codeTextArea.setEnabled(true);
                isCompiled = false;
                return;
            } else if (name.equals("untitled")) {
                JOptionPane.showMessageDialog(mainApp, "Please Save this file first !", "ERROR", JOptionPane.ERROR_MESSAGE);
                codeTextArea.setEnabled(true);
                isCompiled = false;
                return;
            }

            saveFile(codeTextArea);
            String output = CommandLine.compile(App.currentTabFile);

            if (output.equals("")) {
                isCompiled = true;
            } else {
                isCompiled = false;
            }

            terminaltextArea.setText(terminaltextArea.getText() + "\n>> " + ((output.equals("")) ? "COMPILED SUCCESSFULLY" : output));
            terminaltextArea.setCaretPosition(terminaltextArea.getDocument().getLength());
            codeTextArea.setEnabled(true);
        });

        t.start();
    }

    /**
     * Runs the compiled code.
     * Displays the output in the terminal text area.
     */
    public void runCode() {
        if (!isCompiled) {
            JOptionPane.showMessageDialog(mainApp, "COMPILE FIRST BEFORE RUNNING", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        CommandLine.run(App.currentTabFile);
    }

}