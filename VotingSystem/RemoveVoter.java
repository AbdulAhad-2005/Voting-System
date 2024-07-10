package VotingSystem;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RemoveVoter extends JFrame {
    JTextField nameField;
    JTextField cnicField;
    JComboBox<String> electionTypeComboBox;
    JButton removeButton;
    Image backgroundImage;

    public RemoveVoter() {
        super("Removing Voter");
        loadBackgroundImage();
        initializeUI();
    }

    private void loadBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(new File("C:\\\\\\\\Users\\\\\\\\abdul\\\\\\\\OneDrive\\\\\\\\Pictures\\\\\\\\Voting System Project pics\\\\\\\\41688014.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeUI() {
        setSize(450, 300);
        setLayout(new BorderLayout());

        CustomPanel formPanel = new CustomPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        JPanel[] panels = new JPanel[3];

        panels[0] = new JPanel();
        panels[0].setOpaque(false);
        formPanel.add(panels[0]);
        JLabel nameLabel = new JLabel("   Name:    ");
        nameLabel.setForeground(Color.WHITE);
        panels[0].add(nameLabel);
        nameField = new JTextField(15);
        nameField.setBackground(Color.BLUE);
        nameField.setForeground(Color.WHITE);
        panels[0].add(nameField);

        panels[1] = new JPanel();
        panels[1].setOpaque(false);
        formPanel.add(panels[1]);
        JLabel cnicLabel = new JLabel("    CNIC:     ");
        cnicLabel.setForeground(Color.WHITE);
        panels[1].add(cnicLabel);
        cnicField = new JTextField(15);
        cnicField.setBackground(Color.BLUE);
        cnicField.setForeground(Color.WHITE);
        panels[1].add(cnicField);

        panels[2] = new JPanel();
        panels[2].setOpaque(false);
        formPanel.add(panels[2]);
        JLabel electionTypeLabel = new JLabel("Election Type: ");
        electionTypeLabel.setForeground(Color.WHITE);
        panels[2].add(electionTypeLabel);
        electionTypeComboBox = new JComboBox<>(new String[]{"National Assembly Election", "Provincial Assembly Election", "Senate Election"});
        electionTypeComboBox.setBackground(Color.BLUE);
        electionTypeComboBox.setForeground(Color.WHITE);
        panels[2].add(electionTypeComboBox);

        add(formPanel, BorderLayout.CENTER);

        removeButton = new JButton("Submit");
        setButtonColor(removeButton, Color.BLUE, Color.WHITE);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(removeButton);
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String cnic = cnicField.getText();
                int electionType = electionTypeComboBox.getSelectedIndex() + 1;
                if (!name.isEmpty() && !cnic.isEmpty() && electionType != 0) {
                    removeFromDatabase(name, cnic, electionType);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(RemoveVoter.this, "Please enter all the required fields to remove the voter.");
                }
            }
        });
        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }

    private void setButtonColor(JButton button, Color bgColor, Color fgColor) {
        button.setBackground(bgColor);
        button.setForeground(fgColor);
    }

    private void removeFromDatabase(String name, String uncheckedCNIC, int electionType) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/voting_system", "root", "root");
            PreparedStatement statement = connection.prepareStatement("DELETE FROM Voter WHERE Name = ? AND CNIC = ? AND ElectionId = ?");
            statement.setString(1, name);
            long cnic = Long.parseLong(uncheckedCNIC);
            statement.setLong(2, cnic);
            statement.setInt(3, electionType);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Voter removed successfully");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to remove voter", "Error", JOptionPane.ERROR_MESSAGE);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to remove voter: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
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

