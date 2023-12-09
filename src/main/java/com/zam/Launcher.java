package com.zam;

import javax.swing.JFrame;
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
        SwingUtilities.invokeLater(() -> {
            String arguments = (args.length > 0) ? args[0] : "";
            final App app = new App(arguments);
            app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            app.setTitle("BitCode - Java Editor");
            app.setVisible(true);
        });
    }   
}
