package com.zam.components.editor;

import java.awt.Color;
import java.util.List;
import java.util.function.BiConsumer;

import javax.swing.Icon;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.zam.ui.App;

/**
 * Custom extension of JTabbedPane for managing code editor tabs in BitCode IDE.
 *
 * Responsibilities:
 * - Handling the creation and removal of code editor tabs.
 * - Associating each tab with a CodeTextArea.
 * - Customizing tab appearance and behavior, including tab closing.
 *
 * Usage:
 * - Use the `addCodeAreaTab` method to add a new code editor tab with the specified properties.
 * - The tab can be closed by clicking the close button on the tab.
 *
 * Example:
 * ```java
 * EditorTabPane editorTabPane = new EditorTabPane(mainApp);
 * editorTabPane.addCodeAreaTab("Untitled", icon, "tooltip", "Initial content");
 * ```
 *
 * @author Muhammed Zohaib
 * @version 1.0.2
 * @since 2023-11-29
 */
public class EditorTabPane extends JTabbedPane {

    private List<CodeTextArea> codeAreaPanes;
    private App mainApp;

    /**
     * Constructor for the EditorTabPane.
     *
     * @param parent The main App instance.
     */
    public EditorTabPane(App parent) {
        this.mainApp = parent;

        // Set custom UI properties for tab appearance and behavior
        UIManager.put("TabbedPane.closeHoverForeground", Color.red);
        UIManager.put("TabbedPane.closePressedForeground", Color.red);
        UIManager.put("TabbedPane.closeHoverBackground", new Color(0, true));
        UIManager.put("TabbedPane.showTabSeparators", true);

        this.codeAreaPanes = mainApp.codeAreaPanes;

        // Enable tab closing and set tab layout policy
        this.putClientProperty("JTabbedPane.tabClosable", true);
        this.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        // Set the tab close callback to handle tab removal
        this.putClientProperty("JTabbedPane.tabCloseCallback", (BiConsumer<JTabbedPane, Integer>) (tabbedPane, tabIndex) -> {
            // Close tab logic
            if (getTitleAt(tabIndex).startsWith("untitled")) {
                mainApp.menuBar.fileMenu.untitledCount--;
            }
            codeAreaPanes.remove((int) tabIndex);
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
        codeAreaPanes.add(new CodeTextArea(mainApp));
        CodeTextArea codePanel = codeAreaPanes.get(codeAreaPanes.size() - 1);
        this.insertTab(title, icon, codePanel, tooltip, this.getTabCount());
        setSelectedIndex(this.getTabCount()-1);
        codePanel.codeTextArea.setText(content);
        codePanel.codeTextArea.discardAllEdits();
        codePanel.codeTextArea.setCaretPosition(codePanel.codeTextArea.getDocument().getLength() - 2);
        codePanel.codeTextArea.requestFocus();
        addListener(codePanel);
        this.updateUI();
    }

    /**
     * Adds a document listener to track changes in the code editor.
     *
     * @param codeTextArea The CodeTextArea instance to attach the listener to.
     */
    private void addListener(CodeTextArea codeTextArea){
        codeTextArea.codeTextArea.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                mainApp.tabbedEditorPane.setIconAt(App.currentTabIndex, App.jRedImage);
            }

            public void removeUpdate(DocumentEvent e) {
                mainApp.tabbedEditorPane.setIconAt(App.currentTabIndex, App.jRedImage);
            }

            public void changedUpdate(DocumentEvent e) {
                mainApp.tabbedEditorPane.setIconAt(App.currentTabIndex, App.jRedImage);
            }
        });
    }
}
