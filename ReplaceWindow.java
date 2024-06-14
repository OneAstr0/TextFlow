package TextFlow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

interface ReplaceCallback {
    void onReplace(String targetWord, String replaceWord);
    void onRemoveHighlights();
}


public class ReplaceWindow extends JFrame {

    private String targetWord;
    private String replaceWord;
    private ReplaceCallback callback;

    private JTextField targetWordField = new JTextField();
    private JTextField replaceWordField = new JTextField();


    public ReplaceWindow(ReplaceCallback callback) {
        super("Replace in text...");
        this.callback = callback;
        setSize(300, 85);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(1, 2)); // GridLayout для двух колонок
        add(panel);

        panel.add(targetWordField);
        panel.add(replaceWordField);

        JButton findButton = new JButton("Replace");
        findButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                replace();
            }
        });
        add(findButton, BorderLayout.SOUTH);


        targetWordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    replaceWordField.requestFocusInWindow();
                }
            }
        });

        replaceWordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    findButton.doClick();
                }
            }

        });



        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                callback.onRemoveHighlights();
            }
        });
    }

    private void replace() {
        targetWord = targetWordField.getText();
        replaceWord = replaceWordField.getText();
        if (callback != null) {
            callback.onReplace(targetWord, replaceWord);
        }
    }

}
