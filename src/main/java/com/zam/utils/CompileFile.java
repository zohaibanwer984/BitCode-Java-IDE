package com.zam.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;

import com.zam.ui.App;

/**
 * Utility class for compiling Java source files using the javac command.
 *
 * Responsibilities:
 * - Compiling Java source files.
 * - Redirecting compilation output to the terminal.
 *
 * Usage:
 * - Create an instance by providing the source file and the main App instance.
 * - Call the compile() method to initiate the compilation process.
 *
 * Example:
 * ```java
 * File sourceFile = new File("path/to/your/sourcefile.java");
 * CompileFile compiler = new CompileFile(sourceFile, mainApp);
 * boolean isCompiled = compiler.compile();
 * ```
 *
 * @author Muhammed Zohaib
 * @version 1.0
 * @since 2023-12-20
 */
public class CompileFile {

    private File sourceJavaFile;
    private App mainApp;

    /**
     * Constructor for the CompileFile class.
     *
     * @param sourceFile The Java source file to be compiled.
     * @param parent     The main App instance.
     */
    public CompileFile(File sourceFile, App parent) {
        this.mainApp = parent;
        this.sourceJavaFile = sourceFile;
    }

    /**
     * Compiles the Java source file using the javac command.
     *
     * @return true if the compilation is successful, false otherwise.
     */
    public boolean compile() {
        try {
            // Build the path to the javac executable
            String javacExecutablePath = Paths.get(mainApp.jdkPath, "javac").toString();

            // Build the command to compile the Java file
            String javaFilePath = sourceJavaFile.getAbsolutePath();
            String[] compilerArgs = { javacExecutablePath, javaFilePath };

            ProcessBuilder builder = new ProcessBuilder(compilerArgs);
            builder.redirectErrorStream(true);
            Process process = builder.start();

            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                mainApp.terminalArea.consolArea.append(line + "\n");
            }

            return process.waitFor() == 0;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}
