package VotingSystem;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;

public class AddCandidate extends JFrame {
    JRadioButton[] electionTypeRadioButton;
    JComboBox<String> regionComboBox;
    int electionId;
    String[] partyNames;
    Image backgroundImage;

    AddCandidate() {
        super("Adding Candidate");
        loadBackgroundImage();
        setSize(475, 400);

        CustomPanel addCandidatePanel = new CustomPanel();
        addCandidatePanel.setLayout(new BoxLayout(addCandidatePanel, BoxLayout.Y_AXIS));
        JPanel[] panels = new JPanel[5];
        for (int i = 0; i < panels.length; i++) {
            panels[i] = new JPanel();
            panels[i].setOpaque(false); // Make the panel transparent
            addCandidatePanel.add(panels[i]);
        }

        JLabel nameLabel = new JLabel("Name:"); 
        nameLabel.setForeground(Color.WHITE);
        panels[0].add(nameLabel);
        JTextField nameField = new JTextField(15);
        nameField.setBackground(Color.BLUE);
        nameField.setForeground(Color.WHITE);
        panels[0].add(nameField);

        JLabel partyLabel = new JLabel("  Party:");
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

        JButton addButton = new JButton("Submit");
        addButton.setBackground(Color.BLUE);
        addButton.setForeground(Color.WHITE);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // Make the panel transparent
        buttonPanel.add(addButton);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String party = (String) partyNamesComboBox.getSelectedItem();
                String region = (String) regionComboBox.getSelectedItem();

                addCandidateToDatabase(name, party, electionId, region);

                dispose(); // Close the window after adding the candidate
            }
        });

        add(buttonPanel, BorderLayout.SOUTH);
        add(addCandidatePanel, BorderLayout.CENTER);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void loadBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(new File("Add path to the image."));
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

    private void fetchAndPopulateRegions() {
        regionComboBox.removeAllItems();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/voting_system", "root", "root")) {
            String sql = "SELECT VotingRegion FROM Region WHERE ElectionId = ?";
            int selectedIndex = getSelectedIndex();
            electionId = selectedIndex + 1;
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, electionId);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    regionComboBox.addItem(resultSet.getString("VotingRegion"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    private int getSelectedIndex() {
        for (int i = 0; i < electionTypeRadioButton.length; i++) {
            if (electionTypeRadioButton[i].isSelected()) {
                return i;
            }
        }
        return -1; // Default value if none selected
    }

    private void addCandidateToDatabase(String name, String party, int electionId, String region) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Voting_System", "root", "root");
            int partyId = getPartyId(connection, party);
            int regionId = getRegionId(connection, region);

            String checkExistingCandidateSql = "SELECT * FROM Candidate WHERE PartyId = ? AND RegionId = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkExistingCandidateSql);
            checkStatement.setInt(1, partyId);
            checkStatement.setInt(2, regionId);
            ResultSet resultSet = checkStatement.executeQuery();
            if (resultSet.next()) {
                JOptionPane.showMessageDialog(null, "A candidate for the party " + party + " already exists in the region \"" + region + "\".");
                dispose();
                return;
            }

            String sql = "INSERT INTO Candidate (Name, PartyId, ElectionId, RegionId) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setInt(2, partyId);
            statement.setInt(3, electionId);
            statement.setInt(4, regionId);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(null, "Candidate added successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to add candidate!");
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

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
        int electionId = 0;
        try (PreparedStatement statement = connection.prepareStatement("SELECT RegionId FROM Region WHERE VotingRegion = ?")) {
            statement.setString(1, region);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                electionId = resultSet.getInt("RegionId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return electionId;
    }

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
