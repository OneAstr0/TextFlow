package TextFlow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;


public class TextFlow extends JFrame implements SearchCallback, ReplaceCallback, CropCallback {

    private final String NAME = "New document";
    private int numeric = 1;

    private JFileChooser f = new JFileChooser();

    private JTabbedPane myTabs = new JTabbedPane();

    private JMenuBar menuBar = new JMenuBar();

    private JMenu file = new JMenu("File");
    private JMenuItem newFile = new JMenuItem("Create new file");
    private JMenuItem openFile = new JMenuItem("Open file");
    private JMenuItem saveFile = new JMenuItem("Save file");
    private JMenuItem saveFileAs = new JMenuItem("Save file as…");

    private JMenu edit = new JMenu("Edit");
    private JMenuItem findObject = new JMenuItem("Find");
    private JMenuItem replaceObject = new JMenuItem("Replace");
    private JMenuItem changeFirstLetterCase = new JMenuItem("Change case");
    private boolean isLower = false;
    private JMenuItem cropText = new JMenuItem("Crop");

    private JMenu settings = new JMenu("Settings");
    private JMenuItem textSize = new JMenuItem("Text size");
    private JMenuItem textFont = new JMenuItem("Font");

    private JMenu help = new JMenu("Help");
    private JMenuItem hotButtons = new JMenuItem("Hot buttons");

    @Override
    public void onSearch(String word) {
        findTextInMainDocument(word);
    }

    @Override
    public void onReplace(String targetWord, String replaceWord) {
        replaceWordsInMainDocument(targetWord, replaceWord);
    }

    @Override
    public void onCrop(int firstIndex, int lastIndex) {
        cropTextInMainDocument(firstIndex, lastIndex);
    }


    // Main Window – TextFlow
    public TextFlow() {
        super("TextFlow");
        settersVisualPack();
        windowParameters();
        addMouseListeners();
        addActionListeners();
        setBackgroundTextElements();
    }


    private void settersVisualPack() {
        file.add(newFile);
        file.add(openFile);
        file.add(saveFile);
        file.add(saveFileAs);

        edit.add(findObject);
        edit.add(replaceObject);
        edit.add(changeFirstLetterCase);
        edit.add(cropText);

        settings.add(textSize);
        settings.add(textFont);

        help.add(hotButtons);

        menuBar.add(file);
        menuBar.add(edit);
        menuBar.add(settings);
        menuBar.add(help);

        setJMenuBar(menuBar);

        add(myTabs);
    }


    private void windowParameters() {
        setSize(600, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    private void setBackgroundTextElements() {
        JLayeredPane layeredPane = getLayeredPane();
        JLabel label = new JLabel("TextFlow");
        label.setFont(new Font("San Francisco", Font.BOLD, 40));
        label.setForeground(new Color(255, 255, 255, 128)); // Установка полупрозрачности
        Dimension size = label.getPreferredSize();
        int x = (getWidth() - size.width) / 2;
        int y = (getHeight() - size.height) / 2;
        label.setBounds(x, y, size.width, size.height);
        layeredPane.add(label, JLayeredPane.PALETTE_LAYER);

        JLabel label2 = new JLabel("by Eugene");
        label2.setFont(new Font("San Francisco", Font.BOLD, 20));
        label2.setForeground(new Color(255, 255, 255, 128)); // Установка полупрозрачности
        int x2 = (getWidth() - size.width) / 2 + 40;
        int y2 = (getHeight() - size.height) / 2 + 40;
        label2.setBounds(x2, y2, size.width, size.height);
        layeredPane.add(label2, JLayeredPane.PALETTE_LAYER);
    }


    // Закрытие документа правым кликом
    private void addMouseListeners() {
        myTabs.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int tabIndex = myTabs.indexAtLocation(e.getX(), e.getY());
                    if (tabIndex != -1) { // Если пользователь щелкнул по вкладке
                        saveFile.doClick();
                        myTabs.remove(tabIndex);
                    }
                }
            }
        });

    }


    private void addActionListeners() {
        newFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextPane jTextPane = new JTextPane();
                String newFileName = NAME + " " + String.valueOf(numeric);
                numeric++;
                Scroll scrollPane = new Scroll(jTextPane, newFileName, false, null);
                myTabs.add(newFileName, scrollPane);
                addKeyListenerToTabComponent(scrollPane);
            }
        });

        openFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    f.showOpenDialog(null);
                    File file = f.getSelectedFile();
                    String input = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));

                    JTextPane jTextPane = new JTextPane();
                    jTextPane.setText(input);

                    Scroll scroll = new Scroll(jTextPane, file.getName(), true, file.getAbsolutePath());
                    myTabs.add(file.getName(), scroll);
                    addKeyListenerToTabComponent(scroll);
                } catch (IOException e1) {e1.printStackTrace();}
            }
        });

        saveFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (myTabs.countComponents() != 0) {
                    Scroll text = (Scroll) myTabs.getSelectedComponent();
                    String output = text.getText();
                    if (text.isOpened()) {
                        try {
                            FileOutputStream writer = new FileOutputStream(text.getPath());
                            writer.write(output.getBytes());
                            writer.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        saveFileAs.doClick();
                    }
                }
            }
        });

        saveFileAs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (myTabs.countComponents() != 0) {
                    Scroll text = (Scroll) myTabs.getSelectedComponent();
                    String output = text.getText();

                    int result = f.showSaveDialog(null);

                    if (result == JFileChooser.APPROVE_OPTION) {
                        File file = f.getSelectedFile();
                        String filePath = file.getAbsolutePath();

                        // Удаление расширения из имени файла
                        String fileNameWithoutExtension = file.getName().replaceFirst("[.][^.]+$", "");

                        if (!filePath.toLowerCase().endsWith(".txt")) {
                            file = new File(filePath + ".txt");
                        }

                        try {
                            FileOutputStream writer = new FileOutputStream(file);
                            writer.write(output.getBytes());
                            writer.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                        text.setPath(file.getAbsolutePath());
                        // Обновление заголовка вкладки с новым именем файла
                        int tabIndex = myTabs.getSelectedIndex();
                        myTabs.setTitleAt(tabIndex, fileNameWithoutExtension);
                    }
                }
            }
        });


        findObject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (myTabs.countComponents() != 0) {
                    getSearchWindow();
                }
            }
        });

        replaceObject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (myTabs.countComponents() != 0) {
                    getReplaceWindow();
                }
            }
        });

        changeFirstLetterCase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (myTabs.countComponents() != 0) {
                    changeFirstLetterCase();
                }
            }
        });

        cropText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (myTabs.countComponents() != 0) {
                    getCropWindow();
                }
            }
        });


        // Settings buttons
        textFont.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });







        hotButtons.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getHotButtonsWindow();
            }
        });

    }

    private void addKeyListenerToTabComponent(Scroll scroll) {
        JTextPane textPane = scroll.getTextPane();
        textPane.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // Create new file func with Ctrl+N
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_N) {
                    newFile.doClick();
                }

                // Open file func with Ctrl+O
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_O) {
                    openFile.doClick();
                }

                // Saving func with Ctrl+S
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S) {
                    saveFile.doClick();
                }

                // Saving As func with Ctrl+Shift+S
                if (e.isControlDown() && e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_S) {
                    saveFileAs.doClick();
                }

                // Find word(s) with Ctrl+F
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_F) {
                    findObject.doClick();
                }


                // Replace word(s) with Ctrl+H
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_H) {
                    replaceObject.doClick();
                }

            }
        });
    }


    private void getSearchWindow() {
        SearchWindow searchWindow = new SearchWindow(this);

    }

    // Func for search words
    private void findTextInMainDocument(String word) {
        Scroll scroll = (Scroll) myTabs.getSelectedComponent();
        JTextPane textPane = scroll.getTextPane();

        // Получаю текст текущего компонента
        String text = textPane.getText();

        // Очищаю все предыдущие подсветки
        textPane.getHighlighter().removeAllHighlights();

        // Создаю объект Highlighter для окрашивания совпадений
        Highlighter highlighter = textPane.getHighlighter();

        // Переменные для хранения индексов вхождений слова
        int index = -1;
        int lastIndex = 0;

        // Поиск совпадений слова в тексте
        while ((index = text.indexOf(word, lastIndex)) != -1) {
            try {
                // Подсветить вхождение слова
                highlighter.addHighlight(index, index + word.length(), DefaultHighlighter.DefaultPainter);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }

            // Обновляю индекс для следующего поиска
            lastIndex = index + word.length();
        }

        // Если слово не найдено, выведим сообщение
        if (lastIndex == 0) {
            JOptionPane.showMessageDialog(this, "Words not found.", "Find in text...", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    private void getReplaceWindow() {
        ReplaceWindow replaceWindow = new ReplaceWindow(this);

    }

    // Func for replace words
    private void replaceWordsInMainDocument(String targetWord, String replaceWord) {
        Scroll scroll = (Scroll) myTabs.getSelectedComponent();
        JTextPane textPane = scroll.getTextPane();
        String text = textPane.getText();
        text = text.replace(targetWord, replaceWord);
        textPane.setText(text);

        textPane.getHighlighter().removeAllHighlights();
        Highlighter highlighter = textPane.getHighlighter();

        int index = -1;
        int lastIndex = 0;

        while ((index = text.indexOf(replaceWord, lastIndex)) != -1) {
            try {

                highlighter.addHighlight(index, index + replaceWord.length(), DefaultHighlighter.DefaultPainter);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }

            lastIndex = index + replaceWord.length();
        }

        if (lastIndex == 0) {
            JOptionPane.showMessageDialog(this, "Words not found.", "Replace in text...", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    // Func for changing first letter case
    private void changeFirstLetterCase() {
        Scroll scroll = (Scroll) myTabs.getSelectedComponent();
        JTextPane textPane = scroll.getTextPane();
        String text = textPane.getText();
        StringBuilder modifiedText = new StringBuilder();

        boolean isFirstLetter = true;
        for (int i = 0; i < text.length(); i++) {
            char currentChar = text.charAt(i);
            if (Character.isLetter(currentChar)) {
                if (isLower) {
                    if (isFirstLetter) {
                        modifiedText.append(Character.toUpperCase(currentChar));
                        isFirstLetter = false;
                    } else {
                        modifiedText.append(currentChar);
                    }
                } else {
                    if (isFirstLetter) {
                        modifiedText.append(Character.toLowerCase(currentChar));
                        isFirstLetter = false;
                    } else {
                        modifiedText.append(currentChar);
                    }
                }
            } else {
                modifiedText.append(currentChar);
                isFirstLetter = true;
            }
        }
        textPane.setText(modifiedText.toString());
        isLower = !isLower;
    }


    private void getCropWindow() {
        CropWindow cropWindow = new CropWindow(this);
    }

    // Func for crop text in main document
    private void cropTextInMainDocument(int firstIndex, int lastIndex) {
        Scroll scroll = (Scroll) myTabs.getSelectedComponent();
        JTextPane textPane = scroll.getTextPane();
        String text = textPane.getText();
        text = text.substring(firstIndex, lastIndex);
        textPane.setText(text);
    }

    private void getHotButtonsWindow() {
        HotButtonsWindow hbw = new HotButtonsWindow();
    }

    // Удалятор выделений текстовых полей после закрытия окон поиска и замены слов в документе
    @Override
    public void onRemoveHighlights() {
        Scroll scroll = (Scroll) myTabs.getSelectedComponent();
        JTextPane textPane = scroll.getTextPane();
        textPane.getHighlighter().removeAllHighlights();
    }


    public static void main(String[] args) {
        TextFlow TextFlowf = new TextFlow();
    }
}