package TextFlow;

import javax.swing.*;

public class Scroll extends JScrollPane {

    private String path;
    private final String name;
    private Boolean isOpened;
    private final JTextPane text;

    public Scroll(JTextPane text, String name, Boolean isOpened, String path) {
        super(text);
        this.text = text;
        this.name = name;
        this.isOpened = isOpened;
        this.path = path;
    }

    public String getText() {
        return text.getText();
    }

    public boolean isOpened() {
        return isOpened;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setIsOpened() {
        isOpened = true;
    }

    public JTextPane getTextPane() {
        // Возвращаю JTextPane
        return text;
    }

}
