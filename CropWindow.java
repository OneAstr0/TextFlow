package TextFlow;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;

interface CropCallback {
    void onCrop(int firstIndex, int lastIndex);
}

public class CropWindow extends JFrame {

    private int firstIndex;
    private int lastIndex;
    private CropCallback callback;

    private JFormattedTextField firstIndexField;
    private JFormattedTextField lastIndexField;

    public CropWindow(CropCallback callback) {
        super("Crop text");
        this.callback = callback;
        setSize(300, 85);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(1, 2));
        add(panel);

        NumberFormat format = NumberFormat.getIntegerInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(Integer.MAX_VALUE);

        firstIndexField = new JFormattedTextField(formatter);
        lastIndexField = new JFormattedTextField(formatter);

        panel.add(firstIndexField);
        panel.add(lastIndexField);

        JButton cropButton = new JButton("Crop");
        add(cropButton, BorderLayout.SOUTH);
        cropButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crop();
            }
        });


        firstIndexField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    lastIndexField.requestFocusInWindow();
                }
            }
        });

        lastIndexField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    cropButton.doClick();
                }
            }
        });

    }

    private void crop() {
        Number firstIndexValue = (Number) firstIndexField.getValue();
        Number lastIndexValue = (Number) lastIndexField.getValue();

        if (firstIndexValue != null && lastIndexValue != null) {
            firstIndex = firstIndexValue.intValue();
            lastIndex = lastIndexValue.intValue();

            if ((firstIndex < lastIndex) && (callback != null)) {
                callback.onCrop(firstIndex, lastIndex);
            }

        }

    }

}
