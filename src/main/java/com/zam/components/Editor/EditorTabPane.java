package com.zam.components.editor;

import java.awt.Color;
import java.util.List;
import java.util.function.BiConsumer;

import javax.swing.Icon;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;

import com.zam.ui.App;

/**
 * Custom extension of JTabbedPane for managing code editor tabs in BitCode IDE.
 *
 * Responsibilities:
 * - Handling the creation and removal of code editor tabs.
 * - Associating each tab with a LineNumberPane and JTextPane.
 * - Customizing tab appearance and behavior, including tab closing.
 *
 * Usage:
 * - Use the `addCodeAreaTab` method to add a new code editor tab with the specified properties.
 * - The tab can be closed by clicking the close button on the tab.
 *
 * Example:
 * ```java
 * EditorTabPane editorTabPane = new EditorTabPane(lineNumberPanes, mainApp);
 * editorTabPane.addCodeAreaTab("Untitled", icon, "tooltip", "Initial content");
 * ```
 *
 * @author Muhammed Zohaib
 * @version 1.0
 * @since 2023-11-29
 */
public class EditorTabPane extends JTabbedPane {

    private List<LineNumberPane> lineNumberPanes;
    private App mainApp;

    /**
     * Constructor for the EditorTabPane.
     *
     * @param parent      The main App instance.
     */
    public EditorTabPane(App parent) {
        this.mainApp = parent;

        // Set custom UI properties for tab appearance and behavior
        UIManager.put("TabbedPane.closeHoverForeground", Color.red);
        UIManager.put("TabbedPane.closePressedForeground", Color.red);
        UIManager.put("TabbedPane.closeHoverBackground", new Color(0, true));
        UIManager.put("TabbedPane.showTabSeparators", true);

        this.lineNumberPanes = mainApp.lineNumberPanes;

        // Enable tab closing and set tab layout policy
        this.putClientProperty("JTabbedPane.tabClosable", true);
        this.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        // Set the tab close callback to handle tab removal
        this.putClientProperty("JTabbedPane.tabCloseCallback", (BiConsumer<JTabbedPane, Integer>) (tabbedPane, tabIndex) -> {
            // Close tab logic
            if (getTitleAt(tabIndex).startsWith("untitled")) {
                mainApp.menuBar.fileMenu.untitledCount--;
            }
            lineNumberPanes.remove((int) tabIndex);
            this.remove(tabIndex);
        });
    }

    /**
     * Adds a new code editor tab to the tabbed pane.
     *
     * @param title   The title of the tab.
     * @param icon    The icon for the tab.
     * @param tooltip The tooltip for the tab.
     * @param content The initial content of the code editor.
     */
    public void addCodeAreaTab(String title, Icon icon, String tooltip, String content) {
        lineNumberPanes.add(new LineNumberPane(mainApp));
        JTextPane codePane = lineNumberPanes.get(this.getTabCount()).codetextPane;
        codePane.setFont(App.font);
        codePane.setText(content);
        LineNumberPane lineNumberPane = lineNumberPanes.get(lineNumberPanes.size() - 1);
        this.insertTab(title, icon, lineNumberPane, tooltip, this.getTabCount());
        this.setSelectedIndex(this.getTabCount() - 1);
        codePane.requestFocus();
    }
}
