package com.zam.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.zam.components.editor.EditorTabPane;
import com.zam.components.editor.LineNumberPane;
import com.zam.menubar.MenuBar;
import com.zam.utils.SyntaxColorManager;

/**
 * The main class representing the BitCode IDE application.
 * This class extends JFrame to create the main graphical user interface.
 *
 * Responsibilities:
 * - Initializing the application.
 * - Setting up the main frame, including size, font, and icon.
 * - Creating and managing the tabbed editor pane.
 * - Handling terminal area for displaying errors and information.
 * - Managing menu bar functionality.
 * - Listening for changes in the selected tab.
 * - Applying the look and feel to the application.
 * - Providing the main method to launch the application.
 *
 * @author Muhammed Zohaib
 * @version 1.0
 * @since 2023-11-29
 */
public class App extends JFrame {

    // Constants
    private static final double SCREEN_WIDTH_RATIO = 0.5;
    private static final double SCREEN_HEIGHT_RATIO = 0.65;
    private static final int DEFAULT_FONT_SIZE = 16;

    // Static block for initializing application properties
    static {
        // This static block runs at the very beginning of the app, even before the main method.
        try {
            File file = new File(App.class.getProtectionDomain().getCodeSource()
                    .getLocation().toURI().getPath());
            String basePath = file.getParent();
            // Overrides the existing value of "user.dir"
            System.getProperties().put("user.dir", basePath);
        } catch (URISyntaxException ex) {
            // Log the error
            ex.printStackTrace();
        }
    }

    // Instance variables
    private final JSplitPane splitPane;
    private final List<LineNumberPane> lineNumberPanes = new ArrayList<>();
    public final EditorTabPane tabbedEditorPane = new EditorTabPane(lineNumberPanes, this);
    public static int currentTabIndex = 0;
    static final String currentPath = System.getProperty("user.dir");
    public static File currentTabFile = new File("");
    public static final Font font = new Font("Consolas", 0, DEFAULT_FONT_SIZE);
    public static ImageIcon jBlueImage = new ImageIcon(App.class.getResource("/icons/JBlue.png"));
    public static ImageIcon jRedImage = new ImageIcon(App.class.getResource("/icons/JRed.png"));

    /**
     * Constructor for the BitCode IDE application.
     *
     * @param args Command-line arguments passed to the application.
     */
    public App(String args) {
        // Set up the main frame
        final Toolkit tk = Toolkit.getDefaultToolkit();
        final Dimension screenSize = tk.getScreenSize();
        final Image icon = Toolkit.getDefaultToolkit().getImage(App.class.getResource("/icons/icon.png"));

        setIconImage(icon);
        setSize(new Dimension((int) (screenSize.width * SCREEN_WIDTH_RATIO),
                (int) (screenSize.height * SCREEN_HEIGHT_RATIO)));
        setFont(font);

        // Create the terminal area
        final JTextArea terminalArea = new JTextArea();
        terminalArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
        terminalArea.setText(">> THIS IS BitCode TERMINAL ERRORS AND INFO WILL BE HERE..");
        terminalArea.setFont(font);
        terminalArea.setLineWrap(false);
        terminalArea.setWrapStyleWord(false);
        terminalArea.setEditable(false);
        terminalArea.setBackground(Color.BLACK);
        terminalArea.setForeground(Color.WHITE);
        final JScrollPane scrollPane = new JScrollPane(terminalArea);
        scrollPane.setAutoscrolls(true);

        // Create a split pane with the two text areas
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tabbedEditorPane, scrollPane);
        add(splitPane);

        // Create the menu bar
        final MenuBar menuBar = new MenuBar(tabbedEditorPane, lineNumberPanes, terminalArea, this);
        setJMenuBar(menuBar);

        // Listen for changes in the selected tab
        tabbedEditorPane.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (e.getSource() instanceof EditorTabPane) {
                    EditorTabPane pane = (EditorTabPane) e.getSource();
                    currentTabIndex = pane.getSelectedIndex();
                    if (currentTabIndex < 0) {
                        MenuBar.untitledCount = 0;
                        menuBar.newFile();
                        return;
                    }
                    currentTabFile = new File(pane.getToolTipTextAt(currentTabIndex));
                }
            }
        });

        // Apply look and feel and document listener to codeArea and syntaxEditorKit
        switchLookAndFeel("com.formdev.flatlaf.FlatLightLaf");

        // Create a new tab in initiation
        if (args.length() > 0) {
            currentTabFile = new File(args);
            menuBar.loadFile();
            terminalArea.setPreferredSize(new Dimension((int) (getWidth() - 50),
                    (int) (screenSize.height * 0.22)));
        } else {
            menuBar.newFile();
        }
        splitPane.setResizeWeight(0.65);
    }

    /**
     * Switches the look and feel of the application.
     *
     * @param theme The theme to be applied.
     */
    public void switchLookAndFeel(String theme) {
        try {
            UIManager.setLookAndFeel(theme);
            SyntaxColorManager.setColors(theme);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception ex) {
            System.err.println("Failed to set look and feel: " + theme);
        }
        // Re-register all the document listeners with the codePanes
        for (LineNumberPane lineNumPane : lineNumberPanes) {
            lineNumPane.addSyntaxHighlighter(false);
        }
    }
}
