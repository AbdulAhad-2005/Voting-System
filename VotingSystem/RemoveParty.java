package VotingSystem;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class RemoveParty extends JFrame {
    private Image backgroundImage;

    RemoveParty() {
    	super("Removing Party");
        // Load the background image
        backgroundImage = new ImageIcon("Add the background image.").getImage();
        createAndShowGUI();
    }

    public void createAndShowGUI() {
        setSize(400, 180);
        setLocationRelativeTo(null); // Center the frame on the screen

        JPanel mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel partyNamePanel = new JPanel();
        partyNamePanel.setOpaque(false); // Make panel transparent to show background
        JLabel nameLabel = new JLabel(" Party Name:");
        nameLabel.setForeground(Color.WHITE); // Set label color to blue
        JTextField nameField = new JTextField(15);
        partyNamePanel.add(nameLabel);
        partyNamePanel.add(nameField);

        JPanel abbreviationPanel = new JPanel();
        abbreviationPanel.setOpaque(false); // Make panel transparent to show background
        JLabel abbreviationLabel = new JLabel("Abbreviation:");
        abbreviationLabel.setForeground(Color.WHITE); // Set label color to blue
        JTextField abbreviationField = new JTextField(15);
        abbreviationPanel.add(abbreviationLabel);
        abbreviationPanel.add(abbreviationField);
        
        setComponentColors(nameField, abbreviationField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // Make panel transparent to show background
        JButton removeButton = new JButton("Remove");
        removeButton.setBackground(Color.BLUE);
        removeButton.setForeground(Color.WHITE);
        removeButton.setFocusPainted(false); // Remove the focus border
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removePartyFromDatabase(nameField.getText(), abbreviationField.getText());
                dispose();
            }
        });

        buttonPanel.add(removeButton);

        mainPanel.add(partyNamePanel);
        mainPanel.add(abbreviationPanel);
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }
    
    private void setComponentColors(JComponent... components) {
        for (JComponent component : components) {
            component.setBackground(Color.BLUE);
            component.setForeground(Color.WHITE);
        }
    }

    private void removePartyFromDatabase(String name, String abbreviation) {
        try {
            // Establish connection using try-with-resources to ensure proper closing
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Voting_System", "root", "root")) {
                String sql = "DELETE FROM Party WHERE Name = ? AND Abbreviation = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, name);
                    statement.setString(2, abbreviation);

                    int rowsDeleted = statement.executeUpdate();
                    if (rowsDeleted > 0) {
                        JOptionPane.showMessageDialog(null, "Party removed successfully!");
                    } else {
                        JOptionPane.showMessageDialog(null, "No party found with the given name and abbreviation.");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Custom JPanel to paint the background image
    class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
