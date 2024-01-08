# BitCode IDE

[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
## Overview

BitCode IDE is a lightweight Java Integrated Development Environment designed for simplicity and ease of use. It provides a feature-rich code editor, syntax highlighting, and basic code compilation and execution capabilities.

## Features

- **Bulit-in Console:** Output of files will be run inside the IDE
- **Syntax Highlighting:** Supports syntax highlighting for Java code using the Rsyntaxtextarea library.
- **Code Compilation:** Compiles Java code using the bundled OpenJDK.
- **Code Execution:** Runs compiled Java programs.
- **Undo/Redo:** Provides undo and redo functionality for text edits.
- **Line Numbering:** Displays line numbers in the code editor.
- **Theme Support:** Offers different themes for the IDE.

## Screenshots

### Tabbed Editor
![Tabbed Editor](/screenshots/tabbed_editor.png)

### Syntax And Themes
![Syntax And Themes](/screenshots/Themes.png)

### Code Compilation
![Code Compilation](/screenshots/consoleOutput.png)

# Getting Started

### Prerequisites

- Maven installed
- JDK
  
### Installation

1. Clone the repository:

    ```bash
    git clone https://github.com/zohaibanwer984/BitCode-Java-IDE.git
    ```

2. Open the project in your favorite Java IDE.

### Usage

1. Open the project in your Java IDE.
2. Run the `com.zam.App` class to launch the BitCode IDE.

### Bundled OpenJDK

The project includes a `JDK` folder where you can place the OpenJDK distribution for development. The IDE will use this bundled JDK for compilation and execution.

## Thanks

Special thanks to [FlatLaf](https://github.com/JFormDesigner/FlatLaf) for providing the FlatLaf theme library, enhancing the visual appeal of BitCode IDE. Additionally, thanks to [Rsyntaxtextarea](https://github.com/bobbylight/RSyntaxTextArea) for enabling syntax highlighting in the textarea.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.
