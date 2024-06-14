package TextFlow;

import javax.swing.*;
import java.awt.*;

public class HotButtonsWindow extends JFrame {

    public HotButtonsWindow() {
        super("HotButtons");
        setSize(250, 180);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setMargin(new Insets(10, 10, 10, 10));
        setVisible(true);

        textArea.append("Create file - Ctrl+N\n");
        textArea.append("Open file - Ctrl+O\n");
        textArea.append("Save file - Ctrl+S\n");
        textArea.append("Save file as - Ctrl+Shift+S\n\n");
        textArea.append("Find word - Ctrl+F\n");
        textArea.append("Replace word - Ctrl+H");

        JScrollPane scrollPane = new JScrollPane(textArea);
        getContentPane().add(scrollPane);
    }

}
