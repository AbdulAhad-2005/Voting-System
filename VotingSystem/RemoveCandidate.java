package VotingSystem;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;

public class RemoveCandidate extends JFrame {
    JRadioButton[] electionTypeRadioButton;
    JComboBox<String> regionComboBox;
    int electionId;
    String[] partyNames;
    Image backgroundImage;

    RemoveCandidate() {
        super("Remove Candidate");
        loadBackgroundImage();
        setSize(480, 450);

        CustomPanel removeCandidatePanel = new CustomPanel();
        removeCandidatePanel.setLayout(new BoxLayout(removeCandidatePanel, BoxLayout.Y_AXIS));
        JPanel[] panels = new JPanel[5];
        for (int i = 0; i < panels.length; i++) {
            panels[i] = new JPanel();
            panels[i].setOpaque(false); // Make the panel transparent
            removeCandidatePanel.add(panels[i]);
        }

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(Color.WHITE);
        panels[0].add(nameLabel);
        JTextField nameField = new JTextField(15);
        nameField.setBackground(Color.BLUE);
        nameField.setForeground(Color.WHITE);
        panels[0].add(nameField);

        JLabel partyLabel = new JLabel("Party:");
        partyLabel.setForeground(Color.WHITE);
        panels[1].add(partyLabel);
        partyNames = fetchPartyNames();
        JComboBox<String> partyNamesComboBox = new JComboBox<>(partyNames);
        partyNamesComboBox.setBackground(Color.BLUE);
        partyNamesComboBox.setForeground(Color.WHITE);
        panels[1].add(partyNamesComboBox);

        JLabel electionTypeLabel = new JLabel("Election Type:");
        electionTypeLabel.setForeground(Color.WHITE);
        panels[2].add(electionTypeLabel);

        electionTypeRadioButton = new JRadioButton[3];
        electionTypeRadioButton[0] = new JRadioButton("National Assembly Election");
        electionTypeRadioButton[1] = new JRadioButton("Provincial Assembly Election");
        electionTypeRadioButton[2] = new JRadioButton("Senate Election");

        setComponentColors(electionTypeRadioButton);

        electionTypeRadioButton[0].addItemListener(e -> fetchAndPopulateRegions());
        electionTypeRadioButton[1].addItemListener(e -> fetchAndPopulateRegions());
        electionTypeRadioButton[2].addItemListener(e -> fetchAndPopulateRegions());

        ButtonGroup electionTypeGroup = new ButtonGroup();
        for (JRadioButton button : electionTypeRadioButton) {
            electionTypeGroup.add(button);
            panels[2].add(button);
        }

        JLabel regionLabel = new JLabel("Region:");
        regionLabel.setForeground(Color.WHITE);
        panels[3].add(regionLabel);
        regionComboBox = new JComboBox<>();
        regionComboBox.setBackground(Color.BLUE);
        regionComboBox.setForeground(Color.WHITE);
        panels[3].add(regionComboBox);

        JButton removeButton = new JButton("Remove");
        removeButton.setBackground(Color.BLUE);
        removeButton.setForeground(Color.WHITE);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // Make the panel transparent
        buttonPanel.add(removeButton);
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String party = (String) partyNamesComboBox.getSelectedItem();
                String region = (String) regionComboBox.getSelectedItem();

                // Call a method to remove the candidate from the database
                removeCandidateFromDatabase(name, party, electionId, region);

                dispose(); // Close the window after removing the candidate
            }
        });

        add(buttonPanel, BorderLayout.SOUTH);
        add(removeCandidatePanel, BorderLayout.CENTER);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void loadBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(new File("C:\\\\Users\\\\abdul\\\\OneDrive\\\\Pictures\\\\Voting System Project pics\\\\41688014.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setComponentColors(JComponent... components) {
        for (JComponent component : components) {
            component.setBackground(Color.BLUE);
            component.setForeground(Color.WHITE);
        }
    }

    private String[] fetchPartyNames() {
        ArrayList<String> partyNamesList = new ArrayList<>(); // ArrayList to store party names
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Voting_System", "root", "root");
            PreparedStatement statement = connection.prepareStatement("SELECT Name FROM Party");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String partyName = resultSet.getString("Name");
                partyNamesList.add(partyName); // Add party name to the ArrayList
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return partyNamesList.toArray(new String[0]); // Initialize with size 0 to allow dynamic resizing
    }

    // Method to fetch and populate regions based on the selected election type
    private void fetchAndPopulateRegions() {
        // Clear existing items
        regionComboBox.removeAllItems();

        // Query the database to fetch regions based on the selected election type
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/voting_system", "root", "root")) {
            String sql = "SELECT VotingRegion FROM Region WHERE ElectionId = ?";
            // Get the selected election type index (0 for National Assembly, 1 for Provincial Assembly, 2 for Senate)
            int selectedIndex = getSelectedIndex();
            electionId = selectedIndex + 1;
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, electionId); // Adjust index to start from 1
                ResultSet resultSet = statement.executeQuery();
                // Populate the JComboBox with fetched regions
                while (resultSet.next()) {
                    regionComboBox.addItem(resultSet.getString("VotingRegion"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle SQLException
        }
    }

    // Method to get the selected election type index
    private int getSelectedIndex() {
        for (int i = 0; i < electionTypeRadioButton.length; i++) {
            if (electionTypeRadioButton[i].isSelected()) {
                return i;
            }
        }
        return -1; // Default value if none selected
    }

    // Method to remove a candidate from the database
    private void removeCandidateFromDatabase(String name, String party, int electionId, String region) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Voting_System", "root", "root");
            String sql = "DELETE FROM Candidate WHERE Name = ? AND PartyId = ? AND ElectionId = ? AND RegionId = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            // Retrieve PartyId, ElectionId, and RegionId based on the provided party name, election type, and region
            int partyId = getPartyId(connection, party);
            int regionId = getRegionId(connection, region);

            statement.setString(1, name);
            statement.setInt(2, partyId);
            statement.setInt(3, electionId);
            statement.setInt(4, regionId);

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(null, "Candidate removed successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to remove candidate!");
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    // Methods to retrieve PartyId and RegionId based on their respective names
    private int getPartyId(Connection connection, String party) {
        int partyId = 0;
        try (PreparedStatement statement = connection.prepareStatement("SELECT PartyId FROM Party WHERE Name = ?")) {
            statement.setString(1, party);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                partyId = resultSet.getInt("PartyId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return partyId;
    }

    private int getRegionId(Connection connection, String region) {
        int regionId = 0;
        try (PreparedStatement statement = connection.prepareStatement("SELECT RegionId FROM Region WHERE VotingRegion = ?")) {
            statement.setString(1, region);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                regionId = resultSet.getInt("RegionId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return regionId;
    }
    // Custom JPanel to paint the background image
    class CustomPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}
