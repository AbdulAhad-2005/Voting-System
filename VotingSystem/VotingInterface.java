package VotingSystem;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.ArrayList;

public class VotingInterface extends JFrame {
    static int VoteCounter = 0;
    static int[] voteCounts = new int[6];    // Array to store vote counts for each party
    String[] partyNames;
    private BufferedImage[] partyImages;
    private String title; // Add a title field
    private String selectedRegion;
    int electionId;
    private String name;
    private String cnic;
    int[] partyIDs;

    public VotingInterface(String title, String selectedRegion, String name, String cnic) {
        super(title + " System");
        this.title = title; // Assign the title
        this.selectedRegion = selectedRegion;
        this.name = name;
        this.cnic = cnic;
        VoteCounter++;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/voting_system", "root", "root")) {
            // Fetch party names from the database
            partyNames = fetchPartyNames(connection);
            partyImages = fetchPartyImages(connection, partyNames); // Pass party names

            displayPartyImages();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    
    private String[] fetchPartyNames(Connection connection) throws SQLException {
        ArrayList<String> partyNamesList = new ArrayList<>(); // ArrayList to store party names

        try (PreparedStatement statement = connection.prepareStatement("SELECT Name FROM Party")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String partyName = resultSet.getString("Name");
                partyNamesList.add(partyName); // Add party name to the ArrayList
            }
        }
        // Convert ArrayList to String array
        String[] partyNames = partyNamesList.toArray(new String[0]); // Initialize with size 0 to allow dynamic resizing
        return partyNames;
    }


    private BufferedImage[] fetchPartyImages(Connection connection, String[] partyNames) throws SQLException, IOException {
        BufferedImage[] partyImages = new BufferedImage[partyNames.length];
        ArrayList<Integer> partyIDsList = new ArrayList<>(); // ArrayList to store PartyIDs

        for (int i = 0; i < partyNames.length; i++) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT PartyId, Symbol FROM Party WHERE Name = ?")) {
                statement.setString(1, partyNames[i]);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    int partyID = resultSet.getInt("PartyId");
                    partyIDsList.add(partyID); // Add PartyID to the ArrayList
                    byte[] imageData = resultSet.getBytes("Symbol");
                    // Convert byte array to BufferedImage
                    partyImages[i] = ImageIO.read(new ByteArrayInputStream(imageData));
                }
            }
        }
        // Convert ArrayList to int array if necessary
        partyIDs = partyIDsList.stream().mapToInt(Integer::intValue).toArray();
        return partyImages;
    }

    private void displayPartyImages() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 3, 10, 10)); // Arrange images in a grid

        // Add party symbols to the JPanel as buttons
        for (int i = 0; i < partyImages.length; i++) {
            JButton button = new JButton(new ImageIcon(partyImages[i]));
            button.addActionListener(new ImageClickListener(i)); // Add ActionListener
            panel.add(button);
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        getContentPane().add(scrollPane);
        pack();
        setLocationRelativeTo(null); // Center the JFrame
        setVisible(true);
    }

    private class ImageClickListener implements ActionListener {
        private int index;

        public ImageClickListener(int index) {
            this.index = index;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            voteCounts[index]++;
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/voting_system", "root", "root")) {
                // Retrieve VoterId, CandidateId, and ElectionId
                int voterId = getVoterIdFromDatabase(connection);
                int partyId = partyIDs[index];
                int candidateId = getCandidateIdFromDatabase(connection, partyId);
                electionId = getElectionIdFromDatabase(connection);

                // Insert the vote into the Vote table
                insertVoteIntoDatabase(connection, voterId, candidateId, partyId, electionId);

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(VotingInterface.this, "Error occurred while processing the vote: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

            showSuccessDialog();
        }

        private int getVoterIdFromDatabase(Connection connection) throws SQLException {
            int voterId = 0;
            long voterCNIC = Long.parseLong(cnic);
            try (PreparedStatement statement = connection.prepareStatement("SELECT VoterId FROM Voter WHERE Name = ? AND cnic = ?")) {
                statement.setString(1, name);
                statement.setLong(2, voterCNIC);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    voterId = resultSet.getInt("VoterId");
                }
            }
            return voterId;
        }

        private int getCandidateIdFromDatabase(Connection connection, int partyId) {
            int candidateId = 0;
            try (PreparedStatement statement = connection.prepareStatement("SELECT CandidateId FROM Candidate WHERE PartyId = ? AND RegionId = (SELECT RegionId FROM Region WHERE VotingRegion = ?);")) {
                statement.setInt(1, partyId);
                statement.setString(2, selectedRegion);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next())
                    candidateId = resultSet.getInt("CandidateId");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return candidateId;
        }

        private int getElectionIdFromDatabase(Connection connection) {
            int electionId = 0;
            try (PreparedStatement statement = connection.prepareStatement("SELECT ElectionId FROM Election_Type WHERE Title = ?")) {
                statement.setString(1, title);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    electionId = resultSet.getInt("ElectionId");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return electionId;
        }

        private void insertVoteIntoDatabase(Connection connection, int voterId, int candidateId, int partyId, int electionId) throws SQLException {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO Vote (VoterId, CandidateId, PartyID, ElectionId) VALUES (?, ?,?, ?)")) {
                statement.setInt(1, voterId);
                statement.setInt(2, candidateId);
                statement.setInt(3, partyId);
                statement.setInt(4, electionId);
                statement.executeUpdate();
            }
        }
        
        public void showSuccessDialog() {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(Color.WHITE);

            JLabel checkIcon = new JLabel(new ImageIcon("C:\\Users\\abdul\\OneDrive\\Pictures\\Voting System Project pics\\success icon.png"));
            checkIcon.setHorizontalAlignment(SwingConstants.CENTER);
            checkIcon.setVerticalAlignment(SwingConstants.CENTER);
            
            JLabel successLabel = new JLabel("               Success!", SwingConstants.CENTER);
            successLabel.setFont(new Font("Arial", Font.BOLD, 20));
            successLabel.setForeground(new Color(76, 175, 80));

            JLabel messageLabel = new JLabel("<html><div style='text-align: center;'>Your Vote has been given successfully.<br>Thanks for trusting in the voting process.</div></html>", SwingConstants.CENTER);
            messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            messageLabel.setForeground(Color.DARK_GRAY);

            JButton continueButton = new JButton("OK");
            continueButton.setBackground(new Color(76, 175, 80));
            continueButton.setForeground(Color.WHITE);
            continueButton.setFocusPainted(false);
            continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);

            JPanel messagePanel = new JPanel();
            messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
            messagePanel.setBackground(Color.WHITE);
            messagePanel.add(successLabel);
            messagePanel.add(Box.createRigidArea(new Dimension(0, 10)));
            messagePanel.add(messageLabel);

            JPanel southPanel = new JPanel();
            southPanel.setBackground(Color.WHITE);
            southPanel.add(continueButton);

            panel.add(checkIcon, BorderLayout.NORTH);
            panel.add(messagePanel, BorderLayout.CENTER);
            panel.add(southPanel, BorderLayout.SOUTH);

            JDialog dialog = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null).createDialog("Vote Success");
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            continueButton.addActionListener(e -> {
                GiveVote nextVoter = new GiveVote(title, selectedRegion, electionId, voteCounts, partyNames, VoteCounter);
                SwingUtilities.invokeLater(() -> {
                    nextVoter.setVisible(true);
                });
                dialog.dispose();
                VotingInterface.this.dispose(); // Close the current frame
            });

            dialog.setVisible(true);
        }
    }
}
