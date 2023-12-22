package com.zam.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Paths;

import javax.swing.SwingWorker;

import com.zam.components.terminal.Terminal;
import com.zam.ui.App;

/**
 * Utility class for running compiled Java class files.
 *
 * Responsibilities:
 * - Running Java class files.
 * - Handling input and output streams.
 *
 * Usage:
 * - Create an instance by providing the main App instance and the compiled Java class file.
 * - Call the write() method to send input to the running process.
 * - Call the isRunning() method to check if the process is still running.
 * - Call the destroyProcess() method to forcefully terminate the process.
 *
 * Example:
 * ```java
 * File javaClassFile = new File("path/to/your/classfile.class");
 * RunFile runner = new RunFile(mainApp, javaClassFile);
 * runner.write("input command");
 * boolean isRunning = runner.isRunning();
 * runner.destroyProcess();
 * ```
 *
 * @author Muhammed Zohaib
 * @version 1.0
 * @since 2023-12-21
 */
public class RunFile {

    private Process process;
    private BufferedWriter writer;
    private App mainApp;

    /**
     * Constructor for the RunFile class.
     *
     * @param parent         The main App instance.
     * @param javaClassFile  The compiled Java class file to be run.
     * @throws IOException If an I/O error occurs.
     */
    public RunFile(App parent, File javaClassFile) throws IOException {
        this.mainApp = parent;

        // Build the path to the java executable
        String javaExecutablePath = Paths.get(mainApp.jdkPath, "java").toString();
        String javaFilePath = javaClassFile.getParent();
        String className = javaClassFile.getName().replace(".java", "");

        String[] javaCommand = {
            javaExecutablePath,
            "-cp",
            javaFilePath,
            className
        };

        System.out.println("CMD : " + javaExecutablePath + " " + javaFilePath + " " + className);

        ProcessBuilder pb = new ProcessBuilder(javaCommand);
        pb.redirectErrorStream(true);
        process = pb.start();
        writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
        CommandReader commandReader = new CommandReader(mainApp, mainApp.terminalArea, process);
        commandReader.execute();
    }

    /**
     * Writes a command to the running process.
     *
     * @param command The command to be sent to the process.
     * @throws IOException If an I/O error occurs.
     */
    public void write(String command) throws IOException {
        writer.write(command);
        writer.flush();
    }

    /**
     * Checks if the process is still running.
     *
     * @return true if the process is running, false otherwise.
     */
    public boolean isRunning() {
        return process.isAlive();
    }

    /**
     * Forcefully destroys the running process.
     */
    public void destroyProcess() {
        process.destroyForcibly();
    }
}

/**
 * SwingWorker class for asynchronously reading the output of a process.
 */
class CommandReader extends SwingWorker<Void, String> {
    Terminal console;
    Process process;
    App mainApp;

    public CommandReader(App parent, Terminal console, Process process) {
        this.mainApp = parent;
        this.console = console;
        this.process = process;
    }

    @Override
    protected Void doInBackground() throws Exception {
        try {
            // Read the output of the process
            processOutput();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void process(java.util.List<String> chunks) {
        StringBuilder sb = new StringBuilder();
        for (String line : chunks) {
            sb.append(line).append("\n");
        }
        console.consolArea.append(sb.toString());
        console.consolArea.setCaretPosition(console.consolArea.getDocument().getLength());
        console.lastCaretPos = console.consolArea.getCaretPosition();
    }

    @Override
    protected void done() {
        console.consolArea.setEditable(false);
        console.consolArea.transferFocusBackward();
    }

    /**
     * Reads the output of the process line by line.
     *
     * @throws IOException If an I/O error occurs.
     */
    private void processOutput() throws IOException {
        try (BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(process.getInputStream()))) {
            String line;
            while (!isCancelled() && (line = reader.readLine()) != null) {
                publish(line);
            }
            if (!process.isAlive()) {
                publish(">>");
            }
        }
    }
}
