package com.zam.dialogboxes;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

import com.zam.ui.App;

/**
 * Custom JDialog for displaying information about the BitCode IDE.
 *
 * Responsibilities:
 * - Displaying application icon, title, version, and description.
 * - Providing a hyperlink to the source code repository.
 *
 * Usage:
 * ```java
 * AboutDialog aboutDialog = new AboutDialog(mainAppFrame);
 * aboutDialog.setVisible(true);
 * ```
 *
 * @author Muhammed Zohaib
 * @version 1.0
 * @since 2024-01-07
 */
public class AboutDialog extends JDialog {
    private JLabel iconLabel = new JLabel();
    private JLabel titleLabel = new JLabel();
    private JLabel descriptionLabel = new JLabel();

    /**
     * Constructor for AboutDialog.
     *
     * @param parent The main JFrame instance.
     */
    public AboutDialog(JFrame parent) {
        super(parent, "About BitCode", true);

        Container contentPane = getContentPane();
        contentPane.setLayout(new GridBagLayout());
        ((GridBagLayout) contentPane.getLayout()).columnWidths = new int[]{93, 285, 0};
        ((GridBagLayout) contentPane.getLayout()).rowHeights = new int[]{85, 0, 0};
        ((GridBagLayout) contentPane.getLayout()).columnWeights = new double[]{0.0, 0.0, 1.0E-4};
        ((GridBagLayout) contentPane.getLayout()).rowWeights = new double[]{0.0, 0.0, 1.0E-4};

        // Load and scale the application icon
        ImageIcon icon = new ImageIcon(App.class.getResource("/icons/icon.png"));
        Image scaledImage = icon.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
        icon = new ImageIcon(scaledImage);

        iconLabel.setIcon(icon);
        iconLabel.setBorder(new EmptyBorder(0, 15, 5, 15));
        iconLabel.setFocusable(false);
        contentPane.add(iconLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));

        titleLabel.setText("<html><h1>BitCode</h1>Version 1.0.2.0</html>");
        titleLabel.setFont(titleLabel.getFont().deriveFont(titleLabel.getFont().getSize() + 5f));
        contentPane.add(titleLabel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));

        descriptionLabel.setText(
                "<html><div style='text-align:left;'>" +
                        "BitCode is a code editor written in Java that primarily supports Java programming language." +
                        "It has features such as syntax highlighting, basic code completion and supports multiple themes for user's interface customization. " +
                        "But it does not have built-in compiler, it means you will have to use external compiler to compile your Java code. Other features that it may not have include debugging, version control integration, or customizable shortcuts. " +
                        "It is designed to provide a simple and efficient code editing experience for Java developers.<br>" +
                        "Developed by Muhammed Zohaib</div>\n" +
                        "</html>");
        descriptionLabel.setBorder(new EmptyBorder(0, 5, 5, 5));
        contentPane.add(descriptionLabel, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));

        JLabel hyperlink = new JLabel("Source Code");
        hyperlink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        hyperlink.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://github.com/zohaibanwer984/BitCode-Java-IDE"));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hyperlink.setText("Source Code");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                hyperlink.setText("<html><a href=''>Source Code</a></html>");
            }

        });

        hyperlink.setBorder(new EmptyBorder(0, 5, 5, 5));
        contentPane.add(hyperlink, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));

        setSize(380, 320);
        setResizable(false);
        setLocationRelativeTo(parent);
    }
}
