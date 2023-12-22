package com.zam.menubar;

import javax.swing.JMenuBar;

import com.zam.ui.App;

/**
 * The `MenuBar` class represents the menu bar for the code editor application.
 * It provides various menu options for file operations, editing, selection, view settings,
 * and running and compiling code.

 * @author Muhammed Zohaib
 * @version 1.0
 * @since 2023-11-29
 */
public class MenuBar extends JMenuBar {

    private App mainApp;

    public FileMenuHandler fileMenu;
    public EditMenuHandler editMenu;
    public SelectionMenuHandler selectionMenu;
    public ViewMenuHandler viewMenu;
    public RunMenuHandler runMenu;
    public boolean isCompiled = false;

    /**
     * Creates a new instance of the `MenuBar`.
     *
     * @param parent           The parent application or container.
     */
    public MenuBar(App parent) {
        this.mainApp = parent;
        // Create the File tab
        fileMenu = new FileMenuHandler("File", mainApp);
        add(fileMenu);
        
        // Create the Edit tab
        editMenu = new EditMenuHandler("Edit", mainApp);
        add(editMenu);
        
        // Create the Selection tab
        selectionMenu = new SelectionMenuHandler("Selection", mainApp);
        add(selectionMenu);

        // Create the View tab
        viewMenu = new ViewMenuHandler("View", mainApp);
        add(viewMenu);
        
        // Create the Run tab
        runMenu = new RunMenuHandler("Run", mainApp);
        add(runMenu);
        
    }

}