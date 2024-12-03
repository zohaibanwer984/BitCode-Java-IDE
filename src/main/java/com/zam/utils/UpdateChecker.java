package com.zam.utils;

import javax.swing.*;
import com.zam.ui.App;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

/**
 * Utility class for checking updates for the BitCode IDE application.
 * 
 * This class fetches the latest version information from a remote properties file
 * and compares it with the current application version. If a new version is 
 * available, it prompts the user to download it.
 *
 * Usage:
 * - Call `UpdateChecker.checkForUpdate(App parent)` from your application.
 *
 * Example:
 * ```java
 * UpdateChecker.checkForUpdate(mainApp);
 * ```
 *
 * @author Muhammed Zohaib
 * @version 1.0.4
 * @since 2024-12-03
 */
public class UpdateChecker {

    /**
     * URL of the remote properties file containing version information.
     */
    private static final String UPDATE_URL = "https://raw.githubusercontent.com/zohaibanwer984/BitCode-Java-IDE/development/App.properties";

    /**
     * Checks for updates by comparing the current application version with the latest version available online.
     * 
     * @param parent The parent component for displaying dialog boxes.
     */
    public static void checkForUpdate(App parent) {
        try {
            // Fetch the properties file from the remote server
            Properties remoteProperties = fetchRemoteProperties();

            // Retrieve the latest version from the remote properties
            String latestVersion = remoteProperties.getProperty("version");
            String downloadUrl = "https://sourceforge.net/projects/bitcode-java-ide/";

            // Compare versions and notify the user
            if (latestVersion != null && !App.APP_VERSION.equals(latestVersion)) {
                promptForUpdate(parent, latestVersion, downloadUrl);
            } else {
                JOptionPane.showMessageDialog(
                    parent,
                    "You are already using the latest version.",
                    "No Updates",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                parent,
                "Failed to check for updates: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
    }

    /**
     * Fetches the remote properties file containing version information.
     * 
     * @return The loaded Properties object.
     * @throws IOException If an I/O error occurs during the fetch.
     */
    private static Properties fetchRemoteProperties() throws IOException, URISyntaxException {
        Properties remoteProperties = new Properties();
        HttpURLConnection connection = (HttpURLConnection) new URI(UPDATE_URL).toURL().openConnection();
        connection.setRequestMethod("GET");

        // Read the response content from the server
        StringBuilder responseContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                responseContent.append(line).append("\n");
            }
        }

        // Load properties from the fetched content
        try (InputStream inputStream = new ByteArrayInputStream(responseContent.toString().getBytes())) {
            remoteProperties.load(inputStream);
        }

        return remoteProperties;
    }

    /**
     * Prompts the user with an update dialog if a new version is available.
     * 
     * @param parent        The parent component for the dialog.
     * @param latestVersion The latest version available online.
     * @param downloadUrl   The URL to download the new version.
     * @throws Exception If an error occurs while opening the download link.
     */
    private static void promptForUpdate(Component parent, String latestVersion, String downloadUrl) throws Exception {
        int choice = JOptionPane.showConfirmDialog(
            parent,
            "A new version (" + latestVersion + ") is available. Do you want to download it?",
            "Update Available",
            JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            Desktop.getDesktop().browse(new URI(downloadUrl));
        }
    }
}
