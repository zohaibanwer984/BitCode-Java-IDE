package com.zam;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

/**
 * A dialog for selecting a theme from a list of available themes.
 * The selected theme can be applied by clicking the "Apply" button.
 * The dialog can be closed by clicking the "Close" button.
 */
public class ThemeDialog extends JDialog {

    private ButtonGroup themeGroup;
    private JRadioButton flatDarkButton;
    private JRadioButton flatLightButton;
    private JRadioButton solarizedDarkButton;
    private JRadioButton solarizedLighButton;
    private JButton applyButton;
    private JButton closeButton;

    private App mainApp;

    /**
     * Constructs a new theme dialog.
     *
     * @param parent the main parent window of the dialog
     */
    public ThemeDialog(App parent) {
        super(parent, "Theme Selection", true);
        this.mainApp = parent;
        setLayout(new BorderLayout());

        // Create the radio button panel
        JPanel radioButtonPanel = new JPanel();
        radioButtonPanel.setLayout(new BoxLayout(radioButtonPanel, BoxLayout.Y_AXIS));
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Themes :");
        radioButtonPanel.setBorder(titledBorder);
        themeGroup = new ButtonGroup();
        flatDarkButton = new JRadioButton("Dark Flat");
        flatLightButton = new JRadioButton("Light Flat");
        solarizedDarkButton = new JRadioButton("Solarized Dark");
        solarizedLighButton = new JRadioButton("Solarized Light");
        String currentLookAndFeelDescription = UIManager.getLookAndFeel().getDescription();
        
        switch(currentLookAndFeelDescription) {
            case "FlatLaf Dark Look and Feel":
                flatDarkButton.setSelected(true);
                break;
            case "FlatLaf Light Look and Feel":
                flatLightButton.setSelected(true);
                break;
            case "Solarized Dark":
                solarizedDarkButton.setSelected(true);
                break;
            case "Solarized Light":
                solarizedLighButton.setSelected(true);
                break;
        }

        themeGroup.add(flatDarkButton);
        themeGroup.add(flatLightButton);
        themeGroup.add(solarizedDarkButton);
        themeGroup.add(solarizedLighButton);

        // Add the radio buttons to the panel
        radioButtonPanel.add(flatDarkButton, BorderLayout.CENTER);
        radioButtonPanel.add(flatLightButton, BorderLayout.CENTER);
        radioButtonPanel.add(solarizedDarkButton, BorderLayout.CENTER);
        radioButtonPanel.add(solarizedLighButton, BorderLayout.CENTER);
            // Create the button panel
        JPanel buttonPanel = new JPanel();

        // Create the apply and close buttons
        applyButton = new JButton("Apply");
        closeButton = new JButton("Close");

        // Add action listeners to the buttons
        applyButton.addActionListener(new ApplyThemeListener());
        closeButton.addActionListener(new CloseDialogListener());

        // Add the buttons to the panel
        buttonPanel.add(applyButton);
        buttonPanel.add(closeButton);

        // Add the radio button panel and button panel to the dialog
        add(radioButtonPanel, BorderLayout.PAGE_START);
        add(buttonPanel, BorderLayout.PAGE_END);

        pack();
        setLocationRelativeTo(mainApp);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    /**
     * An action listener that applies the selected theme when the "Apply" button is clicked.
     */
    private class ApplyThemeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Get the selected theme from the radio buttons
            String theme = "";
            if (flatDarkButton.isSelected()) {
                theme = "com.formdev.flatlaf.FlatDarkLaf";
            } else if (flatLightButton.isSelected()) {
                theme = "com.formdev.flatlaf.FlatLightLaf";
            } else if (solarizedDarkButton.isSelected()) {
                theme = "com.formdev.flatlaf.intellijthemes.FlatSolarizedDarkIJTheme";
            } else if (solarizedLighButton.isSelected()) {
                theme = "com.formdev.flatlaf.intellijthemes.FlatSolarizedLightIJTheme";
            }

            // Change the look and feel of the main window
            mainApp.switchLookAndFeel(theme);
        }
    }

    /**
     * An action listener that closes the dialog when the "Close" button is clicked.
     */
    private class CloseDialogListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            dispose();
        }
    }
}