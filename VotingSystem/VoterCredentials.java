package VotingSystem;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import javax.swing.*;

public class VoterCredentials extends JFrame {
    private String selectedRegion;
    private int electionId;
    private JTextField nameField;
    private JTextField cnicField;
    private JLabel messageLabel;

    private Timer timer;
    private int index = 0;
    private String message = "Enter your Name and CNIC";

    public VoterCredentials(String title, String selectedRegion, int electionID) {
        super("Voter Credentials");
        this.selectedRegion = selectedRegion;
        electionId = electionID;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 180);
        setLocationRelativeTo(null); // Center the frame

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel nameLabel = new JLabel("          Name:");
        nameField = new JTextField("Write name as on your CNIC.", 15);
        nameField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (nameField.getText().equals("Write name as on your CNIC."))
                    nameField.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (nameField.getText().isEmpty())
                    nameField.setText("Write name as on your CNIC.");
            }
        });

        JLabel cnicLabel = new JLabel("          CNIC:");
        cnicField = new JTextField("Enter your 13-digit CNIC.", 15);
        cnicField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (cnicField.getText().equals("Enter your 13-digit CNIC."))
                    cnicField.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (cnicField.getText().isEmpty())
                    cnicField.setText("Enter your 13-digit CNIC.");
            }
        });

        messageLabel = new JLabel();
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(messageLabel, BorderLayout.NORTH);

        JButton submitButton = new JButton("Vote");

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String cnic = cnicField.getText();
                if (isValidVoter(name, cnic)) {
                    SwingUtilities.invokeLater(() -> {
                        new VotingInterface(title, selectedRegion, name, cnic).setVisible(true); // Open the VotingInterface
                    });
                    timer.stop(); // Stop the timer
                    dispose(); // Close this authentication GUI
                }
            }
        });

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(cnicLabel);
        panel.add(cnicField);
        //panel.add(new JLabel()); // Empty label for alignment
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);
        add(panel, BorderLayout.CENTER);
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

    private boolean isValidVoter(String name, String cnic) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/voting_system", "root", "root")) {
            // Get the current election title
            String currentElectionTitle = ElectionUtil.getCurrentElectionTitle(connection);
            // Check if the voter with provided name and CNIC is valid for the current election i.e added in the database
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM Voter AS v " +
                            "INNER JOIN Election_Type AS e ON v.ElectionId = e.ElectionId " +
                            "WHERE v.Name = ? AND v.CNIC = ? AND e.Title = ?");
            statement.setString(1, name);
            statement.setString(2, cnic);
            statement.setString(3, currentElectionTitle);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // Check if the voter has already voted in the current election type
                PreparedStatement voteCheckStatement = connection.prepareStatement("SELECT VoterId FROM Vote WHERE VoterId IN (SELECT VoterId FROM Voter WHERE Name = ? AND CNIC = ? AND ElectionId = ?)");
                voteCheckStatement.setString(1, name);
                voteCheckStatement.setString(2, cnic);
                voteCheckStatement.setInt(3, electionId);
                ResultSet voteCheckResultSet = voteCheckStatement.executeQuery();
                if (voteCheckResultSet.next()) {
                    // Voter has already voted in the current election type
                    JOptionPane.showMessageDialog(VoterCredentials.this, "You have already voted in this election. You cannot vote again.\nPlease give a chance to the next voter.", "Error", JOptionPane.ERROR_MESSAGE);
                    nameField.setText("");
                    cnicField.setText("");
                    return false;
                } else {
                    // Check if the voter is the selected voting region
                    PreparedStatement regionCheckStatement = connection.prepareStatement("SELECT VotingRegion from Region WHERE RegionId = (SELECT RegionId FROM Voting_Address WHERE voterId = (SELECT VoterId FROM Voter WHERE Name = ? AND CNIC = ? AND ElectionId = ?))");
                    regionCheckStatement.setString(1, name);
                    regionCheckStatement.setString(2, cnic);
                    regionCheckStatement.setInt(3, electionId);
                    ResultSet regionResultSet = regionCheckStatement.executeQuery();
                    if (regionResultSet.next()) {
                        String region = regionResultSet.getString("VotingRegion");
                        if (!region.equals(selectedRegion)) {
                            // Voter's region does not match the selected voting region
                            JOptionPane.showMessageDialog(VoterCredentials.this, "Your vote is not in this region.", "Error", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                    } else {
                        // No voting region found for the voter
                        JOptionPane.showMessageDialog(VoterCredentials.this, "Voting region not found for the voter.", "Error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    // Voter is valid for the current election
                    return true;
                }

            } else {
                // Voter is not valid for the current election
                JOptionPane.showMessageDialog(VoterCredentials.this, "Invalid credentials. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                // Clear the text fields
                nameField.setText("");
                cnicField.setText("");
                return false;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}

class ElectionUtil {
    public static String getCurrentElectionTitle(Connection connection) throws SQLException {
        String currentElectionTitle = "";
        try (PreparedStatement statement = connection.prepareStatement("SELECT Title FROM Election_Type WHERE ElectionId = 1")) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                currentElectionTitle = resultSet.getString("Title");
            }
        }
        return currentElectionTitle;
    }
}

