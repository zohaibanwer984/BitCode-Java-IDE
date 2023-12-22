package com.zam.menubar;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;

import com.zam.dialogboxes.ThemeDialog;
import com.zam.ui.App;

/**
 * Custom menu handler for the View menu in BitCode IDE.
 *
 * Responsibilities:
 * - Handling font size adjustments.
 * - Handling theme changes.
 * - Integrating with the main application to perform actions on the current text area.
 *
 * Usage:
 * - Integrate into the main menu bar by creating an instance and adding to the appropriate menu.
 *
 * Example:
 * ```java
 * ViewMenuHandler viewMenu = new ViewMenuHandler("View", mainApp);
 * mainMenuBar.add(viewMenu);
 * ```
 *
 * @author Muhammed Zohaib
 * @version 1.0
 * @since 2023-12-12
 */
public class ViewMenuHandler extends JMenu {

    private final JMenuItem increaseFontSizeItem = new JMenuItem("Increase Font Size");
    private final JMenuItem decreaseFontSizeItem = new JMenuItem("Decrease Font Size");
    private final JMenuItem changeThemeItem = new JMenuItem("Change Theme");

    final private App mainApp;

    /**
     * Constructor for the ViewMenuHandler.
     *
     * @param title  The title of the menu.
     * @param parent The main App instance.
     */
    public ViewMenuHandler(String title, App parent) {
        super(title);

        this.mainApp = parent;
        // Add items to the View tab
        add(increaseFontSizeItem);
        add(decreaseFontSizeItem);
        add(changeThemeItem);

        // Add action listeners and accelerators
        configureMenuItems();
    }

    /**
     * Configures action listeners and accelerators for menu items.
     */
    private void configureMenuItems() {
        // Add an action listener to the "Increase Font" menu item
        increaseFontSizeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, ActionEvent.CTRL_MASK));
        increaseFontSizeItem.addActionListener(e -> increaseFontSize(mainApp.lineNumberPanes.get(App.currentTabIndex).codetextPane));

        // Add an action listener to the "Decrease Font" menu item
        decreaseFontSizeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, ActionEvent.CTRL_MASK));
        decreaseFontSizeItem.addActionListener(e -> decreaseFontSize(mainApp.lineNumberPanes.get(App.currentTabIndex).codetextPane));

        // Add an action listener to the "Change Theme" menu item
        changeThemeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
        changeThemeItem.addActionListener(e -> openThemeDialog(true));
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
    public void openThemeDialog(boolean enabled) {
        ThemeDialog themeDialog = new ThemeDialog(mainApp);
        themeDialog.setVisible(true);
    }
}
