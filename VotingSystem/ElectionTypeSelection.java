package VotingSystem;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import javax.swing.*;

public class ElectionTypeSelection extends JDialog {
    private Connection connection;

    public ElectionTypeSelection(JFrame parent) {
        super(parent, true);
        setSize(300, 200);
        setLocationRelativeTo(parent);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); // Use system look and feel

            // Set title bar color to blue
            getRootPane().setWindowDecorationStyle(JRootPane.NONE); // Remove default title bar

            JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            titlePanel.setBackground(Color.BLUE);
            JLabel titleLabel = new JLabel("Select Voting Type");
            titleLabel.setForeground(Color.WHITE);
            titlePanel.add(titleLabel);

            getContentPane().add(titlePanel, BorderLayout.NORTH);

            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/voting_system", "root", "root");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT electionId, title FROM Election_Type");

            JPanel panel = new JPanel(new GridLayout(0, 1)); // Panel to hold the buttons

            // Iterate through the result set and create buttons for each title
            while (resultSet.next()) {
                int electionId = resultSet.getInt("electionId");
                String title = resultSet.getString("title");
                JButton button = new JButton(title);
                button.setBackground(Color.blue); // Set button background color to light blue
                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                            JComboBox<String> regionComboBox = new JComboBox<>();
                            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/voting_system", "root", "root");
                            PreparedStatement ps = connection.prepareStatement("SELECT VotingRegion FROM Region WHERE ElectionId = ?");
                            ps.setInt(1, electionId);
                            ResultSet rs = ps.executeQuery();
                            // Process ResultSet and update UI
                            while (rs.next()) {
                                String region = rs.getString("VotingRegion");
                                regionComboBox.addItem(region);
                            }
                            int option = JOptionPane.showConfirmDialog(ElectionTypeSelection.this, regionComboBox, "Select Region", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                            if (option == JOptionPane.OK_OPTION) {
                                String selectedRegion = (String) regionComboBox.getSelectedItem();
                                SwingUtilities.invokeLater(() -> {
                                    new GiveVote(title, selectedRegion, electionId).setVisible(true); // Open the VoterCredentials
                                });
                                dispose(); // Dispose the current dialog after opening the new one
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(ElectionTypeSelection.this, "Failed to retrieve regions: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
                panel.add(button); // Add button to the panel
            }

            // Add the button panel to the frame
            getContentPane().add(new JScrollPane(panel), BorderLayout.CENTER);

        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to retrieve election types: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (connection != null) {
                    connection.close(); // Close the connection here
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
