package VotingSystem;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class EndElection extends JFrame {
	private int[] voteCounts;
	private String[] partyNames;
	String region;
	int totalVotes;
	
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel messageLabel;

    private static final String ADMIN_USERNAME = "Admin";
    private static final String ADMIN_PASSWORD = "12345";

    private Timer timer;
    private int index = 0;
    private String message = "Give username and password to end Election";

    public EndElection(int[] voteCounts, String[] partyNames, String region, int totalVotes) {
    	this.voteCounts = voteCounts;
    	this.partyNames = partyNames;
    	this.region = region;
    	this.totalVotes = totalVotes;
    	
        setTitle("Admin Confirmation");
        setSize(300, 180);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel usernameLabel = new JLabel("Username:");
        inputPanel.add(usernameLabel);
        usernameField = new JTextField();
        inputPanel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        inputPanel.add(passwordLabel);
        passwordField = new JPasswordField();
        inputPanel.add(passwordField);

        add(inputPanel, BorderLayout.CENTER);

        messageLabel = new JLabel();
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(messageLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticate();
            }
        });
        buttonPanel.add(loginButton);
        add(buttonPanel, BorderLayout.SOUTH);

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
            ElectionResultWindow electionResultWindow = new ElectionResultWindow(voteCounts, partyNames, region, totalVotes);
        	SwingUtilities.invokeLater(() -> {
            	electionResultWindow.setVisible(true);
        	});        
        } else {
            // Credentials are incorrect, show error message
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Authentication Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
    	
    }
}
