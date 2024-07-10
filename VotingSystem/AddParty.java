package VotingSystem;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class AddParty extends JFrame {
    private Image backgroundImage;
    private byte[] symbolData; // Store image data here

    AddParty() {
        super("Adding Party");
        loadBackgroundImage();
        createAndShowGUI();
    }

    private void loadBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(new File("Add path to the image."));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createAndShowGUI() {
        setSize(500, 400);
        setLocationRelativeTo(null);

        CustomPanel partyPanel = new CustomPanel();
        partyPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel nameLabel = new JLabel("Party Name:");
        JTextField nameField = new JTextField(15);
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        namePanel.setOpaque(false);
        namePanel.add(nameLabel);
        namePanel.add(nameField);
        partyPanel.add(namePanel, gbc);

        gbc.gridy++;
        JLabel abbreviationLabel = new JLabel("Abbreviation:");
        JTextField abbreviationField = new JTextField(5);
        JPanel abbreviationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        abbreviationPanel.setOpaque(false);
        abbreviationPanel.add(abbreviationLabel);
        abbreviationPanel.add(abbreviationField);
        partyPanel.add(abbreviationPanel, gbc);

        gbc.gridy++;
        JLabel leaderLabel = new JLabel("     Leader:   ");
        JTextField leaderField = new JTextField(15);
        JPanel leaderPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        leaderPanel.setOpaque(false);
        leaderPanel.add(leaderLabel);
        leaderPanel.add(leaderField);
        partyPanel.add(leaderPanel, gbc);

        gbc.gridy++;
        JLabel symbolLabel = new JLabel("Select Symbol:");
        JButton selectImageButton = new JButton("Select Image");
        JPanel symbolPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        symbolPanel.setOpaque(false);
        symbolPanel.add(symbolLabel);
        symbolPanel.add(selectImageButton);
        partyPanel.add(symbolPanel, gbc);

        setComponentColors(nameLabel, abbreviationLabel, leaderLabel, symbolLabel, nameField, abbreviationField, leaderField);

        selectImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Choose Party Symbol");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

                int returnValue = fileChooser.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        symbolData = Files.readAllBytes(selectedFile.toPath()); // Read and store image data
                        symbolLabel.setText(selectedFile.getName());
                    } catch (IOException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error reading image file: " + e.getMessage());
                    }
                }
            }
        });

        JButton addButton = new JButton("Add");
        addButton.setBackground(Color.BLUE);
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPartyToDatabase(nameField.getText(), abbreviationField.getText(), leaderField.getText(), symbolData); // Pass image data
                dispose();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(addButton);
        add(buttonPanel, BorderLayout.SOUTH);

        add(partyPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void setComponentColors(JComponent... components) {
        for (JComponent component : components) {
            component.setBackground(Color.BLUE);
            component.setForeground(Color.WHITE);
        }
    }

    private void addPartyToDatabase(String name, String abbreviation, String leader, byte[] symbolData) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Voting_System", "root", "root");
            String sql = "INSERT INTO Party (Name, Abbreviation, Symbol, Leader) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, name);
            statement.setString(2, abbreviation);
            statement.setBytes(3, symbolData);
            statement.setString(4, leader);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(null, "Party added successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to add party!");
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
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
