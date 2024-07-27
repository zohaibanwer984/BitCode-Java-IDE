package com.zam.dialogboxes;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;

import com.formdev.flatlaf.FlatLaf;
import com.zam.ui.App;

/**
 * A dialog box for changing the theme and settings of the code editor.
 * Allows users to customize the appearance and features of the editor.
 *
 * Responsibilities:
 * - Displaying a preview of the code editor with selected settings.
 * - Allowing users to change the editor theme, font size, and various features.
 * - Applying the selected theme and settings to the code editor.
 * - Providing an OK button to confirm changes and a Cancel button to revert changes.
 *
 * @author Muhammed Zohaib
 * @version 1.0.3
 * @since 2024-01-07
 */
public class ThemeChanger extends JDialog {

    // Components for the dialog box
    private final JPanel contentPanel;
    private final RTextScrollPane scrollPane;
    private final RSyntaxTextArea syntaxTextArea;
    private final JPanel optionsPanel;
    private final JLabel themeLabel;
    private final JComboBox<String> themeComboBox;
    private final JLabel fontLabel;
    private final JComboBox<String> fontComboBox;
    private final JLabel fontSizeLabel;
    private final SpinnerNumberModel model;
    private final JSpinner spinner;
    private final JPanel featuresPanel;
    private final JCheckBox bracketMatchingCheckBox;
    private final JCheckBox currentLineHighlightCheckBox;
    private final JCheckBox foldingCheckBox;
    private final JCheckBox lineNumbersCheckBox;
    private final JCheckBox lineWrapCheckBox;
    private final JPanel buttonPanel;
    private final JButton okButton;
    private final JButton cancelButton;

    // Variables to store theme and settings information
    private String lastTheme;
    private String currentLAF;
    private String newLAF;
    private String editorTheme;
    // Reference to the main application
    private App mainApp;

    /**
     * Constructor for the ThemeChanger dialog.
     *
     * @param parent The main application instance.
     */
    public ThemeChanger(App parent) {
        super(parent);
        this.mainApp = parent;

        // Set up the dialog properties
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            cancelButtonFunc();
        }
        });

        // Initialize components and layout
        contentPanel = new JPanel();
        syntaxTextArea = new RSyntaxTextArea();
        scrollPane = new RTextScrollPane(syntaxTextArea);
        optionsPanel = new JPanel();
        themeLabel = new JLabel();
        String[] themes = {"Flat Light", "Flat Dark", "Solarized Light", "Solarized Dark", "Monokai Pro", "Space Grey"}; 
        themeComboBox = new JComboBox<String>(themes);
        fontLabel = new JLabel();
        // Get all available fonts
        String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontComboBox = new JComboBox<String>(fontNames);
        fontComboBox.setSelectedItem(UIManager.getFont("AppFont").getName());
        fontSizeLabel = new JLabel();
        model = new SpinnerNumberModel(UIManager.getFont("AppFont").getSize(), 6, 48, 1);
        model.addChangeListener(e -> fontSize());
        spinner = new JSpinner(model);
        featuresPanel = new JPanel();
        bracketMatchingCheckBox = new JCheckBox();
        bracketMatchingCheckBox.setSelected(mainApp.properties.getBooleanProperty("BracketMatching"));
        bracketMatchingCheckBox.addActionListener(e -> syntaxTextArea.setBracketMatchingEnabled(bracketMatchingCheckBox.isSelected()));
        currentLineHighlightCheckBox = new JCheckBox();
        currentLineHighlightCheckBox.setSelected(mainApp.properties.getBooleanProperty("HighlightCurrentLine"));
        currentLineHighlightCheckBox.addActionListener(e -> syntaxTextArea.setHighlightCurrentLine(currentLineHighlightCheckBox.isSelected()));
        foldingCheckBox = new JCheckBox();
        foldingCheckBox.setSelected(mainApp.properties.getBooleanProperty("CodeFolding"));
        foldingCheckBox.addActionListener(e -> syntaxTextArea.setCodeFoldingEnabled(foldingCheckBox.isSelected()));
        lineNumbersCheckBox = new JCheckBox();
        lineNumbersCheckBox.setSelected(mainApp.properties.getBooleanProperty("LineNumbers"));
        lineNumbersCheckBox.addActionListener(e -> scrollPane.setLineNumbersEnabled(lineNumbersCheckBox.isSelected()));
        lineWrapCheckBox = new JCheckBox();
        lineWrapCheckBox.setSelected(mainApp.properties.getBooleanProperty("LineWrap"));
        lineWrapCheckBox.addActionListener(e -> syntaxTextArea.setLineWrap(lineWrapCheckBox.isSelected()));
        buttonPanel = new JPanel();
        okButton = new JButton();
        okButton.addActionListener(e -> okButtonFunc());
        cancelButton = new JButton();
        cancelButton.addActionListener(e -> cancelButtonFunc());
        themeComboBox.addActionListener(e -> changeTheme());
        fontComboBox.addActionListener(e -> setUIFont());

        setTitle("Editor Settings");
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPanel.setLayout(new BorderLayout(5, 5));
        scrollPane.setBorder(new TitledBorder(null, "Preview:", TitledBorder.LEFT, TitledBorder.ABOVE_TOP,
                                               new Font("Segoe UI", Font.BOLD, 12)));
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        syntaxTextArea.setCodeFoldingEnabled(true);
        String code = "public class Example {\n" +
        "   public static void main(String[] args) {\n" +
        "       // Declare a variable\n" +
        "       int x = 5;\n" +
        "       /*\n" +
        "       Use a for loop to print the variable\n" +
        "       multiple times\n" +
        "       */\n" +
        "       for (int i = 0; i <= x; i++) {\n" +
        "           System.out.println(\"x = \" + x);\n" +
        "       }\n" +
        "   }\n" +
        "}";
        syntaxTextArea.setText(code);
        syntaxTextArea.setBracketMatchingEnabled(mainApp.properties.getBooleanProperty("BracketMatching"));
        syntaxTextArea.setHighlightCurrentLine(mainApp.properties.getBooleanProperty("HighlightCurrentLine"));
        syntaxTextArea.setCodeFoldingEnabled(mainApp.properties.getBooleanProperty("CodeFolding"));
        scrollPane.setLineNumbersEnabled(mainApp.properties.getBooleanProperty("LineNumbers"));
        syntaxTextArea.setLineWrap(mainApp.properties.getBooleanProperty("LineWrap"));
        syntaxTextArea.setFont(UIManager.getFont("AppFont"));
        syntaxTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);

        optionsPanel.setBorder(new TitledBorder(null, "Options:", TitledBorder.LEADING, TitledBorder.ABOVE_TOP,
                                        new Font("Segoe UI", Font.BOLD, 12)));
        optionsPanel.setLayout(new GridBagLayout());

        themeLabel.setText("Theme:");
        optionsPanel.add(themeLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                                                            GridBagConstraints.WEST, GridBagConstraints.NONE,
                                                            new Insets(0, 0, 5, 5), 0, 0));
        optionsPanel.add(themeComboBox, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0,
                                                               GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                                                               new Insets(0, 0, 5, 5), 0, 0));

        fontLabel.setText("Font :");
        optionsPanel.add(fontLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                                                            GridBagConstraints.WEST, GridBagConstraints.NONE,
                                                            new Insets(0, 0, 5, 5), 0, 0));
        optionsPanel.add(fontComboBox, new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0,
                                                                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                                                                new Insets(0, 0, 5, 5), 0, 0));

        fontSizeLabel.setText("Font Size:");
        optionsPanel.add(fontSizeLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                                                               GridBagConstraints.WEST, GridBagConstraints.NONE,
                                                               new Insets(0, 0, 5, 5), 0, 0));
        optionsPanel.add(spinner, new GridBagConstraints(1, 3, 2, 1, 0.0, 0.0,
                                                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                                                        new Insets(0, 0, 5, 5), 0, 0));

        featuresPanel.setBorder(new TitledBorder(null, "Features:", TitledBorder.LEADING, TitledBorder.BELOW_TOP,
                                                 new Font("Segoe UI", Font.BOLD, 12)));
        featuresPanel.setLayout(new GridBagLayout());
        
        bracketMatchingCheckBox.setText("Bracket Matching");
        featuresPanel.add(bracketMatchingCheckBox, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                                                                            GridBagConstraints.WEST, GridBagConstraints.NONE,
                                                                            new Insets(0, 0, 5, 5), 0, 0));

        foldingCheckBox.setText("Code Folding");
        featuresPanel.add(foldingCheckBox, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                                                                    GridBagConstraints.WEST, GridBagConstraints.NONE,
                                                                    new Insets(0, 0, 5, 5), 0, 0));
        
        currentLineHighlightCheckBox.setText("Current Line Highlight");
        featuresPanel.add(currentLineHighlightCheckBox, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                                                                                GridBagConstraints.WEST, GridBagConstraints.NONE,
                                                                                new Insets(0, 0, 5, 5), 0, 0));
                                                                                                                        
        lineNumbersCheckBox.setText("Line Numbers");
        featuresPanel.add(lineNumbersCheckBox, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
        GridBagConstraints.WEST, GridBagConstraints.NONE,
                                                                        new Insets(0, 0, 5, 5), 0, 0));

        lineWrapCheckBox.setText("Line Wrap");
        featuresPanel.add(lineWrapCheckBox, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
                                                                   GridBagConstraints.WEST, GridBagConstraints.NONE,
                                                                   new Insets(0, 0, 5, 5), 0, 0));

        optionsPanel.add(featuresPanel, new GridBagConstraints(0, 4, 3, 1, 0.0, 0.0,
                                                               GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                                               new Insets(0, 0, 5, 5), 0, 0));

        contentPanel.add(optionsPanel, BorderLayout.WEST);

        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        okButton.setText("OK");
        buttonPanel.add(okButton);

        cancelButton.setText("Cancel");
        buttonPanel.add(cancelButton);

        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        contentPane.add(contentPanel, BorderLayout.CENTER);

        currentTheme();
        String selection = (String) themeComboBox.getSelectedItem();
        lastTheme = selection;

        // Set default size and location
        setSize(600, 400);
        setLocationRelativeTo(getOwner());
    }
    private void setUIFont(){
        String selection = (String) fontComboBox.getSelectedItem();
        int currentVal = (int) spinner.getValue();
        Font font = new Font(selection, Font.PLAIN, currentVal);        
		syntaxTextArea.setFont(font.deriveFont(mainApp.textAttributes));
        scrollPane.getGutter().setLineNumberFont(font);
        UIManager.put("AppFont", font);
    }
    /**
     * Handles changes in font size.
     */
    private void fontSize() {
        int currentVal = (int) spinner.getValue();
        Font font = syntaxTextArea.getFont().deriveFont(syntaxTextArea.getFont().getStyle(), currentVal);
        syntaxTextArea.setFont(font.deriveFont(mainApp.textAttributes));
        scrollPane.getGutter().setLineNumberFont(font);
    }
    /**
     * Handles the change of the selected theme.
     */
    private void changeTheme() {
        String selection = (String) themeComboBox.getSelectedItem();
        if (lastTheme != selection){
            // System.out.println(selection);
            lastTheme = selection;
            try {
                switch (selection) {
                    case "Flat Light":
                        newLAF = "com.formdev.flatlaf.FlatLightLaf";
                        editorTheme = "default";
                        break;
                    case "Flat Dark":
                        newLAF = "com.formdev.flatlaf.FlatDarkLaf";
                        editorTheme = "dark";
                        break;
                    case "Solarized Light":
                        newLAF = "com.formdev.flatlaf.intellijthemes.FlatSolarizedLightIJTheme";
                        editorTheme = "solarizedLight";
                        break;
                    case "Solarized Dark":
                        newLAF = "com.formdev.flatlaf.intellijthemes.FlatSolarizedDarkIJTheme";
                        editorTheme = "solarizedDark";
                        break;
                    case "Monokai Pro":
                        newLAF = "com.formdev.flatlaf.intellijthemes.FlatMonokaiProIJTheme";
                        editorTheme = "monokai";
                        break;
                    case "Space Grey":
                        newLAF = "com.formdev.flatlaf.intellijthemes.FlatSpacegrayIJTheme";
                        editorTheme = "spaceGrey";
                        break;
                    default:
                        newLAF = "com.formdev.flatlaf.FlatLightLaf";
                        break;
                }
                UIManager.setLookAndFeel(newLAF);
                SwingUtilities.updateComponentTreeUI(this);
                Theme theme = Theme.load(getClass().getResourceAsStream("/SyntaxThemes/"+ editorTheme +".xml"));
                theme.apply(syntaxTextArea); // its reset the font too!
                Font font = (Font) UIManager.getFont("AppFont");
                syntaxTextArea.setFont(font.deriveFont(mainApp.textAttributes)); //lazy Fix to put the font back :P
                scrollPane.getGutter().setLineNumberFont(UIManager.getFont("AppFont"));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * Sets the current theme based on the look and feel of the editor.
     */
    private void currentTheme(){
        String themeName =  UIManager.getLookAndFeel().getName();
        switch (themeName) {
            case "FlatLaf Light":
                themeComboBox.setSelectedIndex(0);
                currentLAF = "com.formdev.flatlaf.FlatLightLaf";
                break;
            case "FlatLaf Dark":
                themeComboBox.setSelectedIndex(1);
                currentLAF = "com.formdev.flatlaf.FlatDarkLaf";
                break;
            case "Solarized Light":
                themeComboBox.setSelectedIndex(2);
                currentLAF = "com.formdev.flatlaf.intellijthemes.FlatSolarizedLightIJTheme";
                break;
            case "Solarized Dark":
                themeComboBox.setSelectedIndex(3);
                currentLAF = "com.formdev.flatlaf.intellijthemes.FlatSolarizedDarkIJTheme";
                break;
            case "Monokai Pro":
                themeComboBox.setSelectedIndex(4);
                currentLAF = "com.formdev.flatlaf.intellijthemes.FlatMonokaiProIJTheme";
                break;
            case "Spacegray":
                themeComboBox.setSelectedIndex(5);
                currentLAF = "com.formdev.flatlaf.intellijthemes.FlatSpacegrayIJTheme";
                break;
            default:
                System.out.println(themeName);
                themeComboBox.setSelectedIndex(0);
                break;
        }
    }
    /**
     * Handles actions when the Cancel button is clicked.
     */
    private void cancelButtonFunc(){
        try {
            UIManager.setLookAndFeel(currentLAF);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.dispose();
    }
    /**
     * Handles actions when the Ok button is clicked.
     */
    private void okButtonFunc() {
        FlatLaf.updateUI(); //update for the whole UI
        Theme theme = new Theme(syntaxTextArea);
        
        Font font = ((Font) UIManager.get("AppFont")).deriveFont(mainApp.textAttributes);
        for (int i = 0; i < mainApp.tabbedEditorPane.getTabCount(); i++) {
            theme.apply(mainApp.codeAreaPanes.get(i).codeTextArea);
            mainApp.codeAreaPanes.get(i).codeTextArea.setFont(font);
            mainApp.codeAreaPanes.get(i).codeTextArea.setBracketMatchingEnabled(bracketMatchingCheckBox.isSelected());
            mainApp.codeAreaPanes.get(i).codeTextArea.setHighlightCurrentLine(currentLineHighlightCheckBox.isSelected());
            mainApp.codeAreaPanes.get(i).codeTextArea.setCodeFoldingEnabled(foldingCheckBox.isSelected());
            mainApp.codeAreaPanes.get(i).setLineNumbersEnabled(lineNumbersCheckBox.isSelected());
            mainApp.codeAreaPanes.get(i).codeTextArea.setLineWrap(lineWrapCheckBox.isSelected());
            
        }
        mainApp.properties.setProperty("lookAndFeel", newLAF);
        mainApp.properties.setProperty("editorTheme", editorTheme);
        mainApp.properties.setBooleanProperty("BracketMatching", bracketMatchingCheckBox.isSelected());
        mainApp.properties.setBooleanProperty("HighlightCurrentLine", currentLineHighlightCheckBox.isSelected());
        mainApp.properties.setBooleanProperty("CodeFolding", foldingCheckBox.isSelected());
        mainApp.properties.setBooleanProperty("LineNumbers", lineNumbersCheckBox.isSelected());
        mainApp.properties.setBooleanProperty("LineWrap", lineWrapCheckBox.isSelected());
        mainApp.properties.setIntegerProperty("fontSize", font.getSize());
        mainApp.properties.setProperty("FontFamily", font.getName());
        this.dispose();
    }
}