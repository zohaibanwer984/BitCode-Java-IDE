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

import com.zam.components.terminal.Terminal;
import com.zam.ui.App;
import com.zam.utils.CommandLine;

// Wrote on 12 - DEC - 2023

public class RunMenuHandler extends JMenu{

    private final JMenuItem compileItem = new JMenuItem("Compile");
    private final JMenuItem runItem = new JMenuItem("Run");

    final private App mainApp;

    RunMenuHandler(String title, App parent){
        super(title);

        this.mainApp = parent;
        // Add items to the Run tab
        add(compileItem);
        add(runItem);

        // Add an action listener to the "Compile" menu item
        compileItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        compileItem.addActionListener(e -> compileCode( mainApp.lineNumberPanes.get(App.currentTabIndex).codetextPane, mainApp.terminalArea));

        // Add an action listener to the "Compile" menu item
        runItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0));
        runItem.addActionListener(e -> runCode());
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
        mainApp.menuBar.isCompiled = false;
    }
    /**
     * Compiles the code in the code area in a separate thread.
     * Displays compilation output in the terminal text area.
     */
    public void compileCode(JTextPane codeTextArea, Terminal terminal) {
        Thread t = new Thread(() -> {
            codeTextArea.setEnabled(false);
            terminal.showProgressBar();
            String name = App.currentTabFile.getName();

            if (name.length() == 0) {
                JOptionPane.showMessageDialog(mainApp, "NO FILE OPENED", "ERROR", JOptionPane.ERROR_MESSAGE);
                codeTextArea.setEnabled(true);
                mainApp.menuBar.isCompiled = false;
                return;
            } else if (name.equals("untitled")) {
                JOptionPane.showMessageDialog(mainApp, "Please Save this file first !", "ERROR", JOptionPane.ERROR_MESSAGE);
                codeTextArea.setEnabled(true);
                mainApp.menuBar.isCompiled = false;
                return;
            }
            mainApp.menuBar.fileMenu.saveFile(codeTextArea);
            String output = CommandLine.compile(App.currentTabFile);

            if (output.equals("")) {
                mainApp.menuBar.isCompiled = true;
            } else {
                mainApp.menuBar.isCompiled = false;
            }

            mainApp.terminalArea.consolArea.setText(mainApp.terminalArea.consolArea.getText() + "\n>> " + ((output.equals("")) ? "COMPILED SUCCESSFULLY" : output));
            mainApp.terminalArea.consolArea.setCaretPosition(mainApp.terminalArea.consolArea.getDocument().getLength());
            codeTextArea.setEnabled(true);
            terminal.hideProgressBar();
        });

        t.start();
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

        CommandLine.run(App.currentTabFile);
    }

}