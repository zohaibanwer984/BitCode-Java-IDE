package com.zam.menubar;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import com.zam.components.terminal.Terminal;
import com.zam.ui.App;
import com.zam.utils.CompileFile;
import com.zam.utils.RunFile;

/**
 * Custom menu handler for the Run menu in BitCode IDE.
 *
 * Responsibilities:
 * - Handling compile and run operations.
 * - Integrating with the main application to perform actions on the current text area and terminal.
 *
 * Usage:
 * - Integrate into the main menu bar by creating an instance and adding to the appropriate menu.
 *
 * Example:
 * ```java
 * RunMenuHandler runMenu = new RunMenuHandler("Run", mainApp);
 * mainMenuBar.add(runMenu);
 * ```
 *
 * @author Muhammed Zohaib
 * @version 1.0.2
 * @since 2023-12-12
 */
public class RunMenuHandler extends JMenu {

    private final JMenuItem compileItem = new JMenuItem("Compile");
    private final JMenuItem runItem = new JMenuItem("Run");

    private final App mainApp;
    public RunFile runner;

    /**
     * Constructor for the RunMenuHandler.
     *
     * @param title  The title of the menu.
     * @param parent The main App instance.
     */
    public RunMenuHandler(String title, App parent) {
        super(title);

        this.mainApp = parent;
        // Add items to the Run tab
        add(compileItem);
        add(runItem);

        // Add action listeners and accelerators
        configureMenuItems();
    }

    /**
     * Configures action listeners and accelerators for menu items.
     */
    private void configureMenuItems() {
        // Add an action listener to the "Compile" menu item
        compileItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        compileItem.addActionListener(e -> compileCode(mainApp.codeAreaPanes.get(App.currentTabIndex).codeTextArea, mainApp.terminalArea));

        // Add an action listener to the "Run" menu item
        runItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0));
        runItem.addActionListener(e -> runCode());
    }

    /**
     * Opens and loads the content of a file into the specified JTextPane.
     *
     * @param fileLocation The path to the file to be opened.
     * @param codeTextArea The JTextPane where the content of the file will be loaded.
     */
    public void fileOpener(String fileLocation, JTextPane codeTextArea) {
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
        mainApp.menuBar.isCompiled = false;
    }

    /**
     * Compiles the code in the code area in a separate thread.
     * Displays compilation output in the terminal text area.
     */
    public void compileCode(RSyntaxTextArea codeTextArea, Terminal terminal) {
        CompileFile compiler = new CompileFile(App.currentTabFile, mainApp);
        Thread compileThread = new Thread(() -> {
            codeTextArea.setEnabled(false);
            terminal.showProgressBar();
            String name = App.currentTabFile.getName();

            if (name.length() == 0) {
                JOptionPane.showMessageDialog(mainApp, "NO FILE OPENED", "ERROR", JOptionPane.ERROR_MESSAGE);
                codeTextArea.setEnabled(true);
                terminal.hideProgressBar();
                mainApp.menuBar.isCompiled = false;
                return;
            } else if (name.equals("untitled")) {
                JOptionPane.showMessageDialog(mainApp, "Please Save this file first !", "ERROR", JOptionPane.ERROR_MESSAGE);
                codeTextArea.setEnabled(true);
                terminal.hideProgressBar();
                mainApp.menuBar.isCompiled = false;
                return;
            }
            mainApp.menuBar.fileMenu.saveFile(codeTextArea);
            boolean isCompiled = compiler.compile();
            if (isCompiled) {
                mainApp.menuBar.isCompiled = true;
                mainApp.terminalArea.consolArea.append("COMPILED SUCCESSFULLY \n");
            } else {
                mainApp.menuBar.isCompiled = false;
            }
            codeTextArea.setEnabled(true);
            terminal.hideProgressBar();
        });

        compileThread.start();
    }

    /**
     * Runs the compiled code.
     * Displays the output in the terminal text area.
     */
    public void runCode() {
        if (!mainApp.menuBar.isCompiled) {
            JOptionPane.showMessageDialog(mainApp, "COMPILE FIRST BEFORE RUNNING", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Thread compileThread = new Thread(() -> {
            // CommandLine.run(App.currentTabFile);
            mainApp.terminalArea.consolArea.append(">> Running :\n");
            mainApp.terminalArea.consolArea.setEditable(true);
            try {
                runner = new RunFile(mainApp, App.currentTabFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mainApp.terminalArea.consolArea.requestFocus();
        });
        compileThread.start();
    }
}
