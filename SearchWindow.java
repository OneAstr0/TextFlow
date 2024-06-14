package TextFlow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


interface SearchCallback {
    void onSearch(String word);
    void onRemoveHighlights();
}


public class SearchWindow extends JFrame {

    private String word;
    private SearchCallback callback;

    private JTextField inputField = new JTextField();

    public SearchWindow(SearchCallback callback) {
        super("Find in text...");
        this.callback = callback;
        setSize(250,85);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        add(panel);

        panel.add(inputField, BorderLayout.CENTER);

        JButton findButton = new JButton("Find");
        findButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                search();
            }
        });
        panel.add(findButton, BorderLayout.SOUTH);


        inputField.addKeyListener(new KeyAdapter() {
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




    private void search() {
        word = inputField.getText();
        if (callback != null) {
            callback.onSearch(word);
        }
    }

}
