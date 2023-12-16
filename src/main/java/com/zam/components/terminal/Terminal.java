package com.zam.components.terminal;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import com.zam.ui.App;

public class Terminal extends JPanel{
    public int lastCaretPos = 0;
    public final JTextArea consolArea;
    
    private final JScrollPane consolePane;
    private JProgressBar progressBar;
  
    // private App mainApp;
    public Terminal(App parent) {
        // this.mainApp = parent;
        consolArea = new JTextArea("DEV-BitCode Terminal Errors And Code will be excuted here..\n>>");
        consolArea.setFont(App.font);
        lastCaretPos = consolArea.getCaretPosition();
        consolArea.setForeground(Color.white);
        consolArea.setBackground(Color.BLACK);
        consolArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
        consolArea.setSize(800,300);
        consolArea.setBorder( BorderFactory.createEmptyBorder(5,10,0,5));
        consolArea.setEditable(false);
        consolArea.setAutoscrolls(true);
        AbstractDocument doc = (AbstractDocument)consolArea.getDocument();
        doc.setDocumentFilter(new CaretProtectionFilter(lastCaretPos));
        ActionMap am = consolArea.getActionMap();
        Action oldAction = am.get("insert-break");// without this newline will not be added
        am.put("insert-break", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(consolArea.isEditable()){ // prevents enter keypress when its not editable but in focus
                    try {
                        int caretPos = consolArea.getCaretPosition();
                        String command = consolArea.getText(lastCaretPos, caretPos - lastCaretPos).trim();
                        lastCaretPos = caretPos;
                        System.out.println("Command :" + command);
                        // mainApp.editor.codeExcutor.runner.write(command + "\n");
                      } catch (BadLocationException ex) {
                        ex.printStackTrace();
                      }
                }
                oldAction.actionPerformed(e);
            }
        });
        consolePane = new JScrollPane(consolArea);
        consolePane.setAutoscrolls(true);

        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);

        setLayout(new BorderLayout());
        add(consolePane, BorderLayout.CENTER);
        add(progressBar, BorderLayout.NORTH);
    }
    public void showProgressBar() {
      progressBar.setVisible(true);
    }
    public void hideProgressBar() {
      progressBar.setVisible(false);
    }
    class CaretProtectionFilter extends DocumentFilter {
        private final int lastCaretPos;
      
        public CaretProtectionFilter(int lastCaretPos) {
          this.lastCaretPos = lastCaretPos;
        }
      
        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
          if (offset >= lastCaretPos) {
            super.remove(fb, offset, length);
          }
        }
    }
}

