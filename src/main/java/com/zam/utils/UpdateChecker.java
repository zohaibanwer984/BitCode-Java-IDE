package com.zam.utils;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Properties;

public class UpdateChecker {
    private static final String UPDATE_URL = "https://raw.githubusercontent.com/zohaibanwer984/BitCode-Java-IDE/development/App.properties";
    private static final String CURRENT_VERSION = "1.0.0";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(UpdateChecker::checkForUpdate);
    }

    public static void checkForUpdate() {
        try {
            // Fetch the properties file
            Properties remoteProperties = new Properties();
            HttpURLConnection connection = (HttpURLConnection) new URI(UPDATE_URL).toURL().openConnection();
            connection.setRequestMethod("GET");

            try (InputStream inputStream = connection.getInputStream()) {
                remoteProperties.load(inputStream);
            }

            // Get version
            String latestVersion = remoteProperties.getProperty("version");
            // String downloadUrl = remoteProperties.getProperty("downloadUrl");
            String downloadUrl = "LINK TO DOWNLOAD";

            // Compare versions
            if (latestVersion != null && !CURRENT_VERSION.equals(latestVersion)) {
                int choice = JOptionPane.showConfirmDialog(
                        null,
                        "A new version (" + latestVersion + ") is available. Do you want to download it?",
                        "Update Available",
                        JOptionPane.YES_NO_OPTION
                );

                if (choice == JOptionPane.YES_OPTION && downloadUrl != null) {
                    Desktop.getDesktop().browse(new URI(downloadUrl));
                }
            } else {
                JOptionPane.showMessageDialog(null, "You are already using the latest version.", "No Updates", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to check for updates: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
