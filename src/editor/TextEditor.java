package editor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


public class TextEditor extends JFrame {
    // Instance
    Search search;
    // Constants
    private final int FRAME_WIDTH = 600;
    private final int FRAME_HEIGHT = 600;
    private final String FRAME_TITLE = "Text Editor";
    private final int TEXT_AREA_FONT_SIZE = 20;
    private final int TEXT_AREA_WIDTH = FRAME_WIDTH - 50;
    private final int TEXT_AREA_HEIGHT = FRAME_HEIGHT - 50;
    private final int SCROLL_PANE_WIDTH = TEXT_AREA_WIDTH;
    private final int SCROLL_PANE_HEIGHT = TEXT_AREA_HEIGHT - 50;
    private final int TOP_PANEL_H_GAP = 5;
    private final int TEXT_FIELD_WIDTH = (TEXT_AREA_WIDTH / 3) - 2 * TOP_PANEL_H_GAP;
    private final int TEXT_FIELD_HEIGHT = 26;
    private final String ABSOLUTE_PATH = "C:\\Users\\Tim\\IdeaProjects\\Text Editor\\Text Editor\\task\\src\\editor\\";
    private final String TEXT_AREA_FONT = "Arial";
    // Swing components
    JPanel topPanel, centerPanel;
    FlowLayout flowLayoutTop;
    JTextArea textArea;
    JScrollPane scrollPane;
    JTextField searchField;
    JButton saveButton, openButton, searchButton, previousButton, nextButton;
    JCheckBox checkRegex;
    JFileChooser fileChooser;
    JMenuBar menuBar;
    JMenu fileMenu, searchMenu;
    JMenuItem open, save, exit, startSearch, previousSearch, nextMatch, useRegex;

    public TextEditor() {
        // Set JFrame Fields
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setVisible(true);
        setLocationRelativeTo(null);
        setTitle(FRAME_TITLE);

        initComponents();
    }

    private void initComponents() {
        // File chooser
        fileChooser = new JFileChooser(ABSOLUTE_PATH);
        fileChooser.setName("FileChooser");
        add(fileChooser);
        // Top Pane
        topPanel = new JPanel();
        initTopPane(topPanel);
        add(topPanel, BorderLayout.NORTH);
        // Center Pane
        centerPanel = new JPanel();
        initCenterPane(centerPanel);
        add(centerPanel, BorderLayout.CENTER);
        // Menu
        initMenu();
    }

    private void initCenterPane(JPanel centerPanel) {
        // Text Area
        textArea = new JTextArea();
        textArea.setName("TextArea");
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font(TEXT_AREA_FONT, Font.PLAIN, TEXT_AREA_FONT_SIZE));
        textArea.setPreferredSize(new Dimension(TEXT_AREA_WIDTH, TEXT_AREA_HEIGHT));
        // Scroll Pane
        scrollPane = new JScrollPane(textArea);
        scrollPane.setName("ScrollPane");
        scrollPane.setPreferredSize(new Dimension(SCROLL_PANE_WIDTH, SCROLL_PANE_HEIGHT));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        // Add
        centerPanel.add(scrollPane);
    }

    private void initTopPane(JPanel topPanel) {
        // FLow Layout
        flowLayoutTop = new FlowLayout();
        topPanel.setLayout(flowLayoutTop);
        // Panel Layout
       topPanel.setPreferredSize(new Dimension(TEXT_AREA_WIDTH, 32));
        flowLayoutTop.setHgap(TOP_PANEL_H_GAP);
        // Text Field
        searchField = new JTextField();
        searchField.setName("SearchField");
        searchField.setPreferredSize(new Dimension(TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT));
        // Buttons
        saveButton = new JButton(new ImageIcon(ABSOLUTE_PATH + "resources\\save.png"));
        saveButton.setName("SaveButton");
        saveButton.addActionListener(actionEvent -> {
            saveAction();
        });
        openButton = new JButton(new ImageIcon(ABSOLUTE_PATH + "resources\\open.png"));
        openButton.setName("OpenButton");
        openButton.addActionListener(actionEvent -> {
            openAction();
        });
        searchButton = new JButton(new ImageIcon(ABSOLUTE_PATH + "resources\\search.png"));
        searchButton.setName("StartSearchButton");
        searchButton.addActionListener(actionEvent -> {
          searchAction();
        });
        previousButton = new JButton(new ImageIcon(ABSOLUTE_PATH + "resources\\previous.png"));
        previousButton.setName("PreviousMatchButton");
        previousButton.addActionListener(actionEvent -> {
            previousAction(search);

        });
        nextButton = new JButton(new ImageIcon(ABSOLUTE_PATH + "resources\\next.png"));
        nextButton.setName("NextMatchButton");
        nextButton.addActionListener(actionEvent -> {
            nextAction(search);
        });
        // Checkbox
        checkRegex = new JCheckBox("Use regex");
        checkRegex.setName("UseRegExCheckbox");
        // Add
        topPanel.add(saveButton);
        topPanel.add(openButton);
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(previousButton);
        topPanel.add(nextButton);
        topPanel.add(checkRegex);
    }

    private void initMenu() {
        menuBar = new JMenuBar();
        // file
        fileMenu = new JMenu("File");
        fileMenu.setName("MenuFile");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        open = new JMenuItem("Open");
        open.setName("MenuOpen");
        save = new JMenuItem("Save");
        save.setName("MenuSave");
        exit = new JMenuItem("Exit");
        exit.setName("MenuExit");

        open.addActionListener(e -> {
            openAction();
        });
        save.addActionListener(e -> {
            saveAction();
        });
        exit.addActionListener(e -> {
            dispose();
        });

        fileMenu.add(open);
        fileMenu.add(save);
        fileMenu.addSeparator();
        fileMenu.add(exit);

        // search
        searchMenu = new JMenu("Search");
        searchMenu.setName("MenuSearch");

        startSearch = new JMenuItem("Start search");
        startSearch.setName("MenuStartSearch");
        previousSearch = new JMenuItem("Previous search");
        previousSearch.setName("MenuPreviousMatch");
        nextMatch = new JMenuItem("Next match");
        nextMatch.setName("MenuNextMatch");
        useRegex = new JMenuItem("Use regular expressions");
        useRegex.setName("MenuUseRegExp");

        startSearch.addActionListener(e -> {
            searchAction();
        });
        previousSearch.addActionListener(e -> {
            previousAction(search);
        });
        nextMatch.addActionListener(e -> {
            nextAction(search);
        });
        useRegex.addActionListener(e -> {
            checkRegex.setSelected(!checkRegex.isSelected());
        });

        searchMenu.add(startSearch);
        searchMenu.add(previousSearch);
        searchMenu.add(nextMatch);
        searchMenu.add(useRegex);

        // add
        menuBar.add(fileMenu);
        menuBar.add(searchMenu);
        setJMenuBar(menuBar);
    }

    private void openAction() {
        File selectedFile;
        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();

            try {
                textArea.setText(new String(Files.readAllBytes(Paths.get(selectedFile.getAbsolutePath()))));
            } catch (IOException e) {
                textArea.setText("");
                e.printStackTrace();
            }
        }
    }

    private void saveAction() {
        File selectedFile;
        FileWriter fileWriter;
        int returnValue = fileChooser.showSaveDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            try {
                fileWriter = new FileWriter(selectedFile);
                fileWriter.write(textArea.getText());
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    // Search Methods
    private void searchAction() {
        search = new Search(textArea.getText(), searchField.getText(), checkRegex.isSelected());
        search.execute();
        try {
            search.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        setCaret(search.getKeyFromIndex(), search.getMatchResult(search.getKeyFromIndex()));
    }

    private void nextAction(Search search) {
        if(search != null) {
            search.goNext();
            setCaret(search.getKeyFromIndex(), search.getMatchResult(search.getKeyFromIndex()));
        }
    }

    private void previousAction(Search search) {
        if(search != null) {
            search.goPrevious();
            setCaret(search.getKeyFromIndex(), search.getMatchResult(search.getKeyFromIndex()));
        }
    }

    private void setCaret(int keyIndex, String foundText) {
        textArea.setCaretPosition(keyIndex + foundText.length());
        textArea.select(keyIndex, keyIndex + foundText.length());
        textArea.grabFocus();
    }
}
