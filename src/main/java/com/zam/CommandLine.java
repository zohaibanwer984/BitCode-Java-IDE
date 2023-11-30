package com.zam;

import java.io.File;
import java.util.Scanner;

/**
 * A utility class for running and compiling Java programs from the command line.
 *
 * Responsibilities:
 * - Running Java programs from the command line.
 * - Compiling Java source files from the command line.
 *
 * Usage:
 * - The `run` method executes a Java program specified by a given file.
 * - The `compile` method compiles a Java source file using an external script.
 *
 * Note: This class assumes the presence of external scripts (`script.bat` and `compile.bat`)
 *       for running and compiling Java programs. The scripts are expected to be located in
 *       the "resources" directory.
 *
 * Example:
 * ```java
 * File javaFile = new File("path/to/YourJavaProgram.java");
 * CommandLine.run(javaFile);
 * String compileResult = CommandLine.compile(javaFile);
 * System.out.println("Compilation Result: " + compileResult);
 * ```
 *
 * @author Muhammed Zohaib
 * @version 1.0
 * @since 2023-11-29
 */
public class CommandLine {

    /**
     * Runs a Java program specified by the given file.
     *
     * @param file The Java file to be executed.
     */
    public static void run(File file) {
        String fileName = file.getName();
        // Access a resource file
        File scriptFile = new File(CommandLine.class.getResource("/scripts/runjava.exe").getFile());
        String fileDirectory = file.getParent();
        try {
           Runtime.getRuntime().exec(new String[]{
                "cmd.exe",
                "/r",
                "Start " + scriptFile.getAbsolutePath() + " \"" + fileDirectory + "\" " + fileName.substring(0, fileName.indexOf('.'))});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Compiles a Java source file using an external script.
     *
     * @param file The Java source file to be compiled.
     * @return The compilation result or error message.
     */
    public static String compile(File file) {
        String result;
        String fileDirectory = file.getParent();
        // Access a resource file
        File Scripfile = new File(CommandLine.class.getResource("/scripts/compile.bat").getFile());
        
        try {
            String[] command = new String[]{"cmd", "/c", Scripfile.getAbsolutePath() + " \"" +
                    fileDirectory + "\" " + file.getName()};
            Process pr = Runtime.getRuntime().exec(command);
            try (Scanner s = new Scanner(pr.getErrorStream()).useDelimiter("\\A")) {
                result = s.hasNext() ? s.next() : "";
                s.close();
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
