package eu.menzerath.util;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class ConsoleWindow {
    private JPanel mainPanel;
    private JButton buttonExit;
    private JTextArea logOutput;
    private JButton buttonClear;

    public static void show() {
        // For an nicer look according to the used OS
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        final JFrame frame = new JFrame("ConsoleWindow");
        ConsoleWindow gui = new ConsoleWindow();
        frame.setContentPane(gui.mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setTitle("HTTP-Server");
        frame.setResizable(true);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frame.setExtendedState(JFrame.ICONIFIED);
            }
        });
    }

    public ConsoleWindow() {
        PrintStream printStream = new PrintStream(new CustomOutputStream(logOutput));
        System.setOut(printStream);
        System.setErr(printStream);

        buttonExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        buttonClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logOutput.setText("");
            }
        });
    }

    private class CustomOutputStream extends OutputStream {
        private JTextArea textArea;

        public CustomOutputStream(JTextArea textArea) {
            this.textArea = textArea;
        }

        @Override
        public void write(int b) throws IOException {
            textArea.append(String.valueOf((char) b));
            textArea.setCaretPosition(textArea.getDocument().getLength());
        }
    }
}