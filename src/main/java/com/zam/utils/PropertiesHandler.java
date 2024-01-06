package com.zam.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Utility class for handling properties files.
 *
 * Responsibilities:
 * - Loading and saving properties from/to a file.
 * - Providing methods to get and set string, boolean, and integer properties.
 *
 * Usage:
 * ```java
 * // Create an instance of PropertiesHandler with the path to the properties file
 * File propertiesFile = new File("path/to/your/properties/file.properties");
 * PropertiesHandler propertiesHandler = new PropertiesHandler(propertiesFile);
 *
 * // Get a string property
 * String value = propertiesHandler.getProperty("key");
 *
 * // Set a string property
 * propertiesHandler.setProperty("key", "value");
 *
 * // Get a boolean property
 * boolean boolValue = propertiesHandler.getBooleanProperty("boolKey");
 *
 * // Set a boolean property
 * propertiesHandler.setBooleanProperty("boolKey", true);
 *
 * // Get an integer property
 * int intValue = propertiesHandler.getIntegerProperty("intKey");
 *
 * // Set an integer property
 * propertiesHandler.setIntegerProperty("intKey", 42);
 * ```
 *
 * @author Muhammed Zohaib
 * @version 1.0
 * @since 2023-01-06
 */
public class PropertiesHandler {
    private Properties properties;
    private String propertiesFile;

    /**
     * Constructor for PropertiesHandler.
     *
     * @param propertiesFile The properties file String to handle.
     */
    public PropertiesHandler(String propertiesFile) {
        this.propertiesFile = propertiesFile;
        properties = new Properties();
        loadProperties();
    }

    /**
     * Load properties from the file.
     */
    private void loadProperties() {
        try (FileInputStream fileInputStream = new FileInputStream(this.propertiesFile)) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            System.out.println("Error loading properties file: " + e.getMessage());
        }
    }

    /**
     * Get a string property.
     *
     * @param key The key of the property.
     * @return The value of the property as a string.
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Set a string property.
     *
     * @param key   The key of the property.
     * @param value The value to set.
     */
    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
        saveProperties();
    }

    /**
     * Get a boolean property.
     *
     * @param key The key of the property.
     * @return The value of the property as a boolean.
     */
    public boolean getBooleanProperty(String key) {
        String value = properties.getProperty(key);
        return Boolean.parseBoolean(value);
    }

    /**
     * Set a boolean property.
     *
     * @param key   The key of the property.
     * @param value The value to set.
     */
    public void setBooleanProperty(String key, boolean value) {
        properties.setProperty(key, Boolean.toString(value));
        saveProperties();
    }

    /**
     * Get an integer property.
     *
     * @param key The key of the property.
     * @return The value of the property as an integer.
     */
    public int getIntegerProperty(String key) {
        String value = properties.getProperty(key);
        return Integer.parseInt(value);
    }

    /**
     * Set an integer property.
     *
     * @param key   The key of the property.
     * @param value The value to set.
     */
    public void setIntegerProperty(String key, int value) {
        properties.setProperty(key, Integer.toString(value));
        saveProperties();
    }

    /**
     * Save properties to the file.
     */
    private void saveProperties() {
        try (FileOutputStream fileOutputStream = new FileOutputStream(this.propertiesFile)) {
            properties.store(fileOutputStream, null);
        } catch (IOException e) {
            System.out.println("Error saving properties file: " + e.getMessage());
        }
    }
}
