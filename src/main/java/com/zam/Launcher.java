package com.zam;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.zam.ui.App;

/**
 *  Launcher Class to Run The App
 *
 * @author Muhammed Zohaib
 * @version 1.0
 * @since 2023-12-09
 */
public class Launcher {
     /**
     * The main method to launch the BitCode IDE application.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        String jdkBinPath = findJdkBinPath();
        if (jdkBinPath == null) {
            JOptionPane.showMessageDialog(null, "JDK bin directory not found.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        else{
            SwingUtilities.invokeLater(() -> {
                String arguments = (args.length > 0) ? args[0] : "";
                final App app = new App(jdkBinPath ,arguments);
                app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                app.setTitle("BitCode - Java Editor");
                app.setVisible(true);
            });
        }
    }
    public static String findJdkBinPath() {
        // Look for the JDK bin directory relative to the current working directory
        File currentDir = new File(".\\JDK");
        File[] jdkDirs = currentDir.listFiles(file -> file.isDirectory() && file.getName().startsWith("jdk"));

        Optional<File> firstJdkDir = Arrays.stream(jdkDirs).findFirst();
        return firstJdkDir.map(jdkDir -> Paths.get(jdkDir.getPath(), "bin").toString()).orElse(null);
    }
}
