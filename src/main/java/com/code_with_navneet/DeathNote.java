package com.code_with_navneet;

import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import javax.swing.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.filechooser.*;
import javax.swing.border.*;
import javax.sound.sampled.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;

public class DeathNote extends JFrame implements ActionListener {
    // Gothic Dark Theme Colors
    private static final Color DARK_BG = new Color(30, 30, 35);
    private static final Color LIGHT_TEXT = new Color(220, 220, 220);
    private static final Color ACCENT_COLOR = new Color(0, 39, 180);
    private static final Color MENU_HIGHLIGHT = new Color(9, 0, 70);
    private static final Color STATUS_BG = new Color(20, 20, 25);
    private static final Color STATUS_TEXT = new Color(180, 180, 180);
    private static final Color TEXTAREA_BG = new Color(40, 40, 45);

    // Components
    JMenuBar menubar = new JMenuBar();
    JMenu file = new JMenu("File");
    JMenu edit = new JMenu("Edit");
    JMenu help = new JMenu("Help");

    JMenuItem newFile = new JMenuItem("New");
    JMenuItem openFile = new JMenuItem("Open");
    JMenuItem saveFile = new JMenuItem("Save");
    JMenuItem print = new JMenuItem("Print");
    JMenuItem exit = new JMenuItem("Exit");

    JMenuItem cut = new JMenuItem("Cut");
    JMenuItem copy = new JMenuItem("Copy");
    JMenuItem paste = new JMenuItem("Paste");
    JMenuItem selectall = new JMenuItem("Select All");

    JMenuItem about = new JMenuItem("About");

    JTextArea textArea = new JTextArea();
    private JLabel statusBar = new JLabel(" Ready | Death Note");

    // Easter Egg components
    private Timer quoteTimer;
    private final int QUOTE_INTERVAL = 100000;
    private String[] ryukQuotes = {
            "Humans are so interesting!",
            "How about an apple?",
            "I was bored, so I dropped the Death Note in the human world.",
            "The human who uses this note can neither go to Heaven nor Hell.",
            "This is the first time I've seen a human as interesting as you.",
            "Do you know how to use a Death Note?",
            "When you die, I'll be the one writing your name in my Death Note.",
            "The Death Note is a curse to humans who use it."
    };
    private Clip themeMusic;

    public DeathNote() {
        setTitle("Death Note");
        setBounds(100, 100, 800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            // Load icon from resources
            ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("icon.jpg"));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.out.println("Icon loading failed, using default");
            e.printStackTrace();
        }

        // Play theme music on startup
        playThemeMusic();//1

        setupMenuBar();//2
        setupTextArea();//3
        setupStatusBar();//4
        setupShortcuts();//5
        setupRyukQuotes();//6
    }

    private void playThemeMusic() {
        try {
            // Try to load theme music
            // Note: You'll need to have a sound file named "deathnote_theme.wav" in your resources
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                    getClass().getClassLoader().getResource("deathnote.wav"));
            themeMusic = AudioSystem.getClip();
            themeMusic.open(audioInputStream);
            themeMusic.loop(Clip.LOOP_CONTINUOUSLY);
            themeMusic.start();
        } catch (Exception e) {
            System.out.println("Could not load theme music: " + e.getMessage());
        }
    }

    private void stopThemeMusic() {
        if (themeMusic != null && themeMusic.isRunning()) {
            themeMusic.stop();
            themeMusic.close();
        }
    }

    private void setupRyukQuotes() {
        quoteTimer = new Timer();
        quoteTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                showRyukQuote();
            }
        }, QUOTE_INTERVAL, QUOTE_INTERVAL);
    }

    private void showRyukQuote() {
        Random random = new Random();
        String quote = ryukQuotes[random.nextInt(ryukQuotes.length)];

        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(DeathNote.this,
                    quote,
                    "Ryuk says...",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private void setupMenuBar() {
        menubar.setBackground(DARK_BG);
        menubar.setForeground(LIGHT_TEXT);
        menubar.setBorder(new MatteBorder(0, 0, 2, 0, ACCENT_COLOR));

        // Add menus
        styleMenu(file);
        styleMenu(edit);
        styleMenu(help);

        // Add menu items
        addMenuItem(file, newFile);
        addMenuItem(file, openFile);
        addMenuItem(file, saveFile);
        file.addSeparator();
        addMenuItem(file, print);
        file.addSeparator();
        addMenuItem(file, exit);

        addMenuItem(edit, cut);
        addMenuItem(edit, copy);
        addMenuItem(edit, paste);
        edit.addSeparator();
        addMenuItem(edit, selectall);

        addMenuItem(help, about);

        menubar.add(file);
        menubar.add(edit);
        menubar.add(help);
        setJMenuBar(menubar);
    }

    private void styleMenu(JMenu menu) {
        menu.setForeground(LIGHT_TEXT);
        menu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        menu.setBorder(new EmptyBorder(5, 15, 5, 15));
    }

    private void addMenuItem(JMenu menu, JMenuItem item) {
        styleMenuItem(item);
        menu.add(item);
    }

    private void styleMenuItem(JMenuItem item) {
        item.setBackground(DARK_BG);
        item.setForeground(LIGHT_TEXT);
        item.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        item.setBorder(new EmptyBorder(5, 20, 5, 10));
        item.addActionListener(this);

        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                item.setBackground(MENU_HIGHLIGHT);
                item.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                item.setBackground(DARK_BG);
                item.setForeground(LIGHT_TEXT);
            }
        });
    }

    private void setupTextArea() {
        textArea.setBackground(TEXTAREA_BG);
        textArea.setForeground(LIGHT_TEXT);
        textArea.setCaretColor(LIGHT_TEXT);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 16));
        textArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollpane = new JScrollPane(textArea);
        scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollpane.setBorder(BorderFactory.createEmptyBorder());

        // Custom scrollbar
        JScrollBar verticalScrollBar = scrollpane.getVerticalScrollBar();
        verticalScrollBar.setBackground(DARK_BG);
        verticalScrollBar.setUI(new DeathNoteScrollBarUI());

        add(scrollpane, BorderLayout.CENTER);
    }

    private void setupStatusBar() {
        statusBar.setBackground(STATUS_BG);
        statusBar.setForeground(STATUS_TEXT);
        statusBar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusBar.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 0, 0, 0, ACCENT_COLOR),
                new EmptyBorder(5, 15, 5, 15)
        ));
        statusBar.setOpaque(true);
        add(statusBar, BorderLayout.SOUTH);
    }

    private void setupShortcuts() {
        newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
        openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        print.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK));
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK));
        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK));
        selectall.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
        about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_J, KeyEvent.CTRL_DOWN_MASK));
    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            stopThemeMusic();
            quoteTimer.cancel();
        }
        super.processWindowEvent(e);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DeathNote app = new DeathNote();
            app.setVisible(true);
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "New":
                textArea.setText("");
                updateStatus("New file created");
                break;

            case "Save":
                saveFile();
                break;

            case "Open":
                openFile();
                break;

            case "Print":
                printFile();
                break;

            case "Exit":
                stopThemeMusic();
                quoteTimer.cancel();
                System.exit(0);
                break;

            case "Cut":
                textArea.cut();
                updateStatus("Text cut to clipboard");
                break;

            case "Copy":
                textArea.copy();
                updateStatus("Text copied to clipboard");
                break;

            case "Paste":
                textArea.paste();
                updateStatus("Text pasted from clipboard");
                break;

            case "Select All":
                textArea.selectAll();
                updateStatus("All text selected");
                break;

            case "About":
                showAboutDialog();
                break;
        }
    }

    private void saveFile() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files (*.txt)", "txt");
        fileChooser.setFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(false);

        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String path = file.getAbsolutePath();

            if (!path.endsWith(".txt")) {
                path += ".txt";
                file = new File(path);
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                textArea.write(writer);
                updateStatus("Soul saved: " + file.getName());
            } catch (IOException ex) {
                updateStatus("Error saving file");
                ex.printStackTrace();
            }
        }
    }

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files (*.txt)", "txt");
        fileChooser.setFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(false);

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                textArea.read(reader, null);
                updateStatus("soul opened: " + file.getName());
            } catch (IOException ex) {
                updateStatus("Error opening file");
                ex.printStackTrace();
            }
        }
    }

    private void printFile() {
        try {
            textArea.print();
            updateStatus("Print your nightmares");
        } catch (PrinterException ex) {
            updateStatus("Print error");
            Logger.getLogger(DeathNote.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showAboutDialog() {
        JDialog aboutDialog = new JDialog(this, "About Death Note", true);
        aboutDialog.setSize(400, 250);
        aboutDialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(DARK_BG);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title
        JLabel title = new JLabel("DEATH NOTE", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(ACCENT_COLOR);

        // Version
        JLabel version = new JLabel("Version 1.0", JLabel.CENTER);
        version.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        version.setForeground(LIGHT_TEXT);

        // Info
        JTextArea info = new JTextArea();
        info.setText("A sinister text editor for your darkest thoughts\n\n" +
                "Created with Java Swing\n" +
                "© 2023 CodeWithNavneet");
        info.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        info.setForeground(LIGHT_TEXT);
        info.setBackground(DARK_BG);
        info.setEditable(false);
        info.setBorder(new EmptyBorder(10, 20, 10, 20));

        panel.add(title, BorderLayout.NORTH);
        panel.add(version, BorderLayout.CENTER);
        panel.add(info, BorderLayout.SOUTH);

        aboutDialog.add(panel);
        aboutDialog.setVisible(true);
    }

    private void updateStatus(String message) {
        statusBar.setText(" " + message + " | Death Note");
    }
}

