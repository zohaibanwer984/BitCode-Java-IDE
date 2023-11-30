package com.zam;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class CommandLine {

    public static void run(File file) {
        String fileName = file.getName();
        try {
            // Use relative path to the "scripts" folder
            String scriptPath = Paths.get("scripts", "runjava.exe").toString();
            File scriptFile = new File(scriptPath);

            if (!scriptFile.exists()) {
                System.err.println("Script file not found: " + scriptFile.getAbsolutePath());
                return;
            }

            // Build the command to run the script
            String fileDirectory = file.getParent();
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", "Start", scriptPath, fileDirectory, fileName.substring(0, fileName.indexOf('.')));
            processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String compile(File file) {
        String result;
        String fileDirectory = file.getParent();
        try {
            // Use relative path to the "scripts" folder
            String scriptPath = Paths.get("scripts", "compile.bat").toString();
            File scriptFile = new File(scriptPath);

            if (!scriptFile.exists()) {
                System.err.println("Script file not found: " + scriptFile.getAbsolutePath());
                return "";
            }

            // Build the command to run the script
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", scriptPath, fileDirectory, file.getName());
            Process pr = processBuilder.start();

            // Read the result from the process's error stream
            try (Scanner s = new Scanner(pr.getErrorStream()).useDelimiter("\\A")) {
                result = s.hasNext() ? s.next() : "";
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
