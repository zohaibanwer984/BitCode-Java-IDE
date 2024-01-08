package com.zam.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import com.zam.ui.App;

/**
 * Custom menu handler for the File menu in BitCode IDE.
 *
 * Responsibilities:
 * - Handling new, open, save, save as, and exit operations.
 * - Integrating with the main application to perform actions on the current text area.
 *
 * Usage:
 * - Integrate into the main menu bar by creating an instance and adding to the appropriate menu.
 *
 * Example:
 * ```java
 * FileMenuHandler fileMenu = new FileMenuHandler("File", mainApp);
 * mainMenuBar.add(fileMenu);
 * ```
 *
 * @author Muhammed Zohaib
 * @version 1.0.2
 * @since 2023-12-12
 */
public class FileMenuHandler extends JMenu {

    private final JMenuItem newFileItem = new JMenuItem("New");
    private final JMenuItem openFileItem = new JMenuItem("Open");
    private final JMenuItem saveFileItem = new JMenuItem("Save");
    private final JMenuItem saveAsFileItem = new JMenuItem("Save As");
    private final JMenuItem exitProgramItem = new JMenuItem("Exit");

    private final App mainApp;
    public int untitledCount = 0;

    /**
     * Constructor for the FileMenuHandler.
     *
     * @param title  The title of the menu.
     * @param parent The main App instance.
     */
    public FileMenuHandler(String title, App parent) {
        super(title);

        this.mainApp = parent;
        // Add items to the File tab
        add(newFileItem);
        add(openFileItem);
        add(saveFileItem);
        add(saveAsFileItem);
        addSeparator(); // Add a separator line
        add(exitProgramItem);

        // Add action listeners and accelerators
        configureMenuItems();
    }

    /**
     * Configures action listeners and accelerators for menu items.
     */
    private void configureMenuItems() {
        // Add an action listener to the "New" menu item
        newFileItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        newFileItem.addActionListener(e -> newFile());

        // Add an action listener to the "Open" menu item
        openFileItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        openFileItem.addActionListener(e -> openFile());

        // Add an action listener to the "Save" menu item
        saveFileItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        saveFileItem.addActionListener(e -> saveFile(mainApp.codeAreaPanes.get(App.currentTabIndex).codeTextArea));

        // Add an action listener to the "Save As" menu item
        saveAsFileItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
        saveAsFileItem.addActionListener(e -> saveAsFile(mainApp.codeAreaPanes.get(App.currentTabIndex).codeTextArea));

        // Add an action listener to the "Exit" menu item
        exitProgramItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
        exitProgramItem.addActionListener(e -> System.exit(0));
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
        mainApp.tabbedEditorPane.addCodeAreaTab("untitled " + untitledCount, App.jRedImage, "untitled", code);
        mainApp.menuBar.isCompiled = false;
        untitledCount++;
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
            mainApp.menuBar.isCompiled = false;
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
            mainApp.tabbedEditorPane.addCodeAreaTab(
                App.currentTabFile.getName(),
                App.jBlueImage,
                App.currentTabFile.getPath(),
                fileContent
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the content of the codeTextArea to a new file selected by the user.
     * The file is chosen using a file dialog.
     */
    public void saveAsFile(RSyntaxTextArea codeTextArea) {
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
    public void saveFile(RSyntaxTextArea codeTextArea) {
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
            mainApp.tabbedEditorPane.setTitleAt(App.currentTabIndex, currentFileName);
            mainApp.tabbedEditorPane.setToolTipTextAt(App.currentTabIndex, currentFileLocation);
            mainApp.tabbedEditorPane.setIconAt(App.currentTabIndex, App.jBlueImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
