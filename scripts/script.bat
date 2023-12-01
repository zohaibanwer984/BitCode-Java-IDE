@ECHO OFF

REM Get the full path of the currently executing script
SET SCRIPT_PATH=%~dp0

REM Set the path to the bundled JDK dynamically
SET BUNDLED_JDK=%SCRIPT_PATH%..\JDK\jdk-21.0.1

REM Change the directory to the specified path
CD "%1"

REM Set the path to the java executable within your bundled JDK
SET JAVA_EXECUTABLE=%BUNDLED_JDK%\bin\java.exe

REM Run the Java application using the bundled JDK
"%JAVA_EXECUTABLE%" -cp "%1" "%2"

PAUSE
EXIT
