package com.zam.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.font.TextAttribute;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.zam.components.editor.CodeTextArea;
import com.zam.components.editor.EditorTabPane;
// import com.zam.components.editor.LineNumberPane;
import com.zam.components.terminal.Terminal;
import com.zam.menubar.MenuBar;
import com.zam.utils.PropertiesHandler;

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
 * @version 1.0.3
 * @since 2023-11-29
 */
public class App extends JFrame {

    // Constants
    private static final double SCREEN_WIDTH_RATIO = 0.5;
    private static final double SCREEN_HEIGHT_RATIO = 0.65;

    // Private Componnets
    private final JSplitPane splitPane;
    
    // Public Componnets
    public final List<CodeTextArea> codeAreaPanes;
    public final EditorTabPane tabbedEditorPane;
    public final Terminal terminalArea;
    public final MenuBar menuBar;
    public Font font;
    // Public Resources
    public final Map<TextAttribute, Object> textAttributes;
    public PropertiesHandler properties = new PropertiesHandler("./App.properties");
    public static ImageIcon jBlueImage = new ImageIcon(App.class.getResource("/icons/JBlue.png"));
    public static ImageIcon jRedImage = new ImageIcon(App.class.getResource("/icons/JRed.png"));
    public static int currentTabIndex = 0;
    public static File currentTabFile = new File("");
    public final String jdkPath;
    /**
     * Constructor for the BitCode IDE application.
     * @param jdkBinPath JDK path which will be used to run and compile the program
     * @param args Command-line arguments passed to the application.
     */
    public App(String jdkBinPath, String args) {
        this.jdkPath = jdkBinPath;
        // Set up the main frame
        final Toolkit tk = Toolkit.getDefaultToolkit();
        final Dimension screenSize = tk.getScreenSize();
        final Image icon = Toolkit.getDefaultToolkit().getImage(App.class.getResource("/icons/icon.png"));

        setIconImage(icon);
        setSize(new Dimension((int) (screenSize.width * SCREEN_WIDTH_RATIO),
                (int) (screenSize.height * SCREEN_HEIGHT_RATIO)));
        textAttributes = new HashMap<>();
        textAttributes.put(TextAttribute.KERNING, TextAttribute.KERNING_ON);
		textAttributes.put(TextAttribute.LIGATURES, TextAttribute.LIGATURES_ON);
        font = new Font(this.properties.getProperty("FontFamily"), 0, this.properties.getIntegerProperty("fontSize"));
        setFont(font);
        UIManager.put("AppFont", font);

        codeAreaPanes = new ArrayList<>();
        tabbedEditorPane = new EditorTabPane(this);

        // Create the terminal area
        terminalArea = new Terminal(this);
        final JScrollPane scrollPane = new JScrollPane(terminalArea);
        scrollPane.setAutoscrolls(true);

        // Create a split pane with the two text areas
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tabbedEditorPane, scrollPane);
        add(splitPane);

        // Create the menu bar
        menuBar = new MenuBar(this);
        setJMenuBar(menuBar);

        // Listen for changes in the selected tab
        tabbedEditorPane.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (e.getSource() instanceof EditorTabPane) {
                    EditorTabPane pane = (EditorTabPane) e.getSource();
                    currentTabIndex = pane.getSelectedIndex();
                    if (currentTabIndex < 0) {
                        menuBar.fileMenu.untitledCount = 0;
                        menuBar.fileMenu.newFile();
                        return;
                    }
                    currentTabFile = new File(pane.getToolTipTextAt(currentTabIndex));
                }
            }
        });

        // Apply look and feel and document listener to codeArea and syntaxEditorKit
        String themeName = this.properties.getProperty("lookAndFeel");
        switchLookAndFeel(themeName);

        // Create a new tab in initiation
        if (args.length() > 0) {
            currentTabFile = new File(args);
            menuBar.fileMenu.loadFile();
            terminalArea.setPreferredSize(new Dimension((int) (getWidth() - 50),
                    (int) (screenSize.height * 0.22)));
        } else {
            menuBar.fileMenu.newFile();
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
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception ex) {
            System.err.println("Failed to set look and feel: " + theme);
        }
    }
}
