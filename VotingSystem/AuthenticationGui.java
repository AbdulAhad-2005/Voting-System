package VotingSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AuthenticationGui extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel messageLabel;

    private static final String ADMIN_USERNAME = "Admin";
    private static final String ADMIN_PASSWORD = "12345";

    private Timer timer;
    private int index = 0;
    private String message = "Give username and password";
    private Point initialClick;

    public AuthenticationGui() {
        setUndecorated(true); // Remove default title bar
        setTitle("Admin Authentication");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Main panel background color
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(44, 62, 80)); // Background color of the main panel
        mainPanel.setLayout(new BorderLayout(0, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        add(mainPanel);

        // Custom title bar
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(new Color(34, 49, 63)); // Background color of the title bar
        titleBar.setPreferredSize(new Dimension(400, 30));

        JLabel titleLabel = new JLabel("Admin Authentication", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE); // Title text color
        titleBar.add(titleLabel, BorderLayout.CENTER);

        // Button panel for minimize, maximize, close
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.setOpaque(false);

        JButton minimizeButton = new JButton("-");
        minimizeButton.setForeground(Color.WHITE);
        minimizeButton.setBackground(new Color(34, 49, 63)); // Minimize button background color
        minimizeButton.setFocusPainted(false);
        minimizeButton.setBorderPainted(false);
        minimizeButton.setPreferredSize(new Dimension(50, 30));
        minimizeButton.addActionListener(e -> setState(JFrame.ICONIFIED));
        buttonPanel.add(minimizeButton);

        JButton maximizeButton = new JButton("□");
        maximizeButton.setForeground(Color.WHITE);
        maximizeButton.setBackground(new Color(34, 49, 63)); // Maximize button background color
        maximizeButton.setFocusPainted(false);
        maximizeButton.setBorderPainted(false);
        maximizeButton.setPreferredSize(new Dimension(50, 30));
        maximizeButton.addActionListener(e -> {
            if (getExtendedState() == JFrame.MAXIMIZED_BOTH) {
                setExtendedState(JFrame.NORMAL);
            } else {
                setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        });
        buttonPanel.add(maximizeButton);

        JButton closeButton = new JButton("X");
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(new Color(192, 57, 43)); // Close button background color
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.setPreferredSize(new Dimension(50, 30));
        closeButton.addActionListener(e -> System.exit(0));
        buttonPanel.add(closeButton);

        titleBar.add(buttonPanel, BorderLayout.EAST);

        // Enable window dragging from the custom title bar
        titleBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
                getComponentAt(initialClick);
            }
        });

        titleBar.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int thisX = getLocation().x;
                int thisY = getLocation().y;

                int xMoved = e.getX() - initialClick.x;
                int yMoved = e.getY() - initialClick.y;

                int X = thisX + xMoved;
                int Y = thisY + yMoved;
                setLocation(X, Y);
            }
        });

        add(titleBar, BorderLayout.NORTH);

        // Header
        JLabel headerLabel = new JLabel("Login", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE); // Header text color
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setOpaque(false); // Make panel background transparent

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE); // Username label text color
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(usernameLabel, gbc);

        usernameField = new JTextField(12);
        gbc.gridx = 1;
        gbc.gridy = 0;
        inputPanel.add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE); // Password label text color
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(12);
        gbc.gridx = 1;
        gbc.gridy = 1;
        inputPanel.add(passwordField, gbc);

        mainPanel.add(inputPanel, BorderLayout.CENTER);

        // Message label
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setForeground(Color.WHITE); // Message label text color
        mainPanel.add(messageLabel, BorderLayout.SOUTH);

        // Button panel
        JPanel buttonPanel2 = new JPanel();
        buttonPanel2.setOpaque(false); // Make panel background transparent
        JButton loginButton = new JButton("Login");
        loginButton.setForeground(Color.WHITE); // Login button text color
        loginButton.setBackground(new Color(41, 128, 185)); // Login button background color
        loginButton.setFocusPainted(false);
        loginButton.setPreferredSize(new Dimension(100, 30)); // Set preferred size
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticate();
            }
        });
        buttonPanel2.add(loginButton);
        mainPanel.add(buttonPanel2, BorderLayout.PAGE_END);

        // Start the timer to update message text
        timer = new Timer(200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateMessage();
            }
        });
        timer.start();
    }

    private void updateMessage() {
        if (index == message.length()) {
            index = 0;
        }
        messageLabel.setText(message.substring(0, index) + "|");
        index++;
    }

    private void authenticate() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
            // Credentials are correct, open next GUI
            timer.stop(); // Stop the timer
            dispose(); // Close this authentication GUI

            // Create an instance of GUI
            GUI gui = new GUI();
            SwingUtilities.invokeLater(() -> {
                gui.setVisible(true);
            });
        } else {
            // Credentials are incorrect, show error message
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Authentication Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                AuthenticationGui authGUI = new AuthenticationGui();
                authGUI.setVisible(true);
            }
        });
    }
}
