package com.zam.utils;

import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.autocomplete.TemplateCompletion;
import org.fife.ui.autocomplete.VariableCompletion;

/**
 * A custom code completion provider for Java language.
 * Provides basic keywords, variable types, and useful templates for Java programming.
 *
 * Responsibilities:
 * - Adding basic Java keywords and variable types for code completion.
 * - Including templates for common Java programming constructs.
 *
 * @author Muhammed Zohaib
 * @version 1.0.2
 * @since 2024-01-07
 */
public class JavaCompletionProvider extends DefaultCompletionProvider {

    /**
     * Constructor for the JavaCompletionProvider.
     * Initializes the code completion provider with Java keywords, variable types, and templates.
     */
    public JavaCompletionProvider() {
        // Basic Java keywords
        addCompletion(new BasicCompletion(this, "return"));
        addCompletion(new BasicCompletion(this, "break"));
        addCompletion(new BasicCompletion(this, "case"));
        addCompletion(new BasicCompletion(this, "assert"));
        addCompletion(new BasicCompletion(this, "extends"));
        addCompletion(new BasicCompletion(this, "implements"));
        addCompletion(new BasicCompletion(this, "abstract"));
        addCompletion(new BasicCompletion(this, "interface"));

        // Basic variable types
        addCompletion(new VariableCompletion(this, "int ", "int"));
        addCompletion(new VariableCompletion(this, "char ", "char"));
        addCompletion(new VariableCompletion(this, "boolean ", "bool"));
        addCompletion(new VariableCompletion(this, "String ", "string"));
        addCompletion(new VariableCompletion(this, "float ", "float"));

        // Useful templates for Java programming
        addCompletion(new TemplateCompletion(this, "sout", "standard print", "System.out.println(${cursor});"));
        addCompletion(new TemplateCompletion(this, "main", "main method", "public static void main(String[] args){\n\t${cursor}\n}"));
        addCompletion(new TemplateCompletion(this, "public", "public method", "public ${type} ${name}() {\n\t${cursor}\n}"));
        addCompletion(new TemplateCompletion(this, "protected", "protected method", "protected ${type} ${name}() {\n\t${cursor}\n}"));
        addCompletion(new TemplateCompletion(this, "private", "private method", "private ${type} ${name}() {\n\t${cursor}\n}"));

        // Control flow and loop constructs
        addCompletion(new TemplateCompletion(this, "for", "for loop", "for (int ${i} = 0; ${i} < ${array}.length; ${i}++) {\n\t${cursor}\n}"));
        addCompletion(new TemplateCompletion(this, "for_each", "for each loop", "for (int ${i} = 0; ${i} < ${10}; ${i}++) {\n\t${cursor}\n}"));
        addCompletion(new TemplateCompletion(this, "if", "if statement", "if (${condition}) {\n\t${cursor}\n}"));
        addCompletion(new TemplateCompletion(this, "if_else", "if-else statement", "if (${condition}) {\n\t${cursor}\n}\nelse {\n\t\n}"));
        addCompletion(new TemplateCompletion(this, "do", "do-while loop", "do {\n\t${cursor}\n} while (${condition});"));
        addCompletion(new TemplateCompletion(this, "while", "while loop", "while (${condition}) {\n\t${cursor}\n}"));

        // Switch-case and exception handling constructs
        addCompletion(new TemplateCompletion(this, "switch", "switch-case", "switch (${key}) {\n\tcase ${value}:\n\t\t${cursor}\n\t\tbreak;\n\tdefault:\n\t\tbreak;\n}"));
        addCompletion(new TemplateCompletion(this, "try", "try-catch", "try {\n\t ${cursor} \n} catch (${err}) {\n\t\n}"));

        // Class template
        addCompletion(new TemplateCompletion(this, "class", "class", "class ${name} {\n\t${cursor}}\n"));
    }
}
