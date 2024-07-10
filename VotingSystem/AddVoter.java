package VotingSystem;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;

public class AddVoter extends JFrame {
    JRadioButton[] electionTypeRadioButton;
    JComboBox<String> regionComboBox;
    Random random = new Random();
    String gender;
    int electionId;
    Image backgroundImage;

    AddVoter() {
        super("Adding Voter");
        loadBackgroundImage();
        setSize(600, 600);

        CustomPanel formPanel = new CustomPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        JPanel[] panels = new JPanel[8];
        for (int i = 0; i < panels.length; i++) {
            panels[i] = new JPanel();
            panels[i].setOpaque(false); // Make the panel transparent
            formPanel.add(panels[i]);
        }

        JLabel[] voterLabels = new JLabel[10];
        String[] voterLabelNames = {"        Name:         ", "         CNIC:          ", "Father's Name:", "         Age:           ", "Gender: ", "Election Type: ", "Election Region: ", "Voting Address: ", "City: ", "Province: "};
        for (int i = 0; i < voterLabelNames.length; i++) {
            voterLabels[i] = new JLabel(voterLabelNames[i]);
            voterLabels[i].setForeground(Color.WHITE); // Set label text color to white
        }

        JTextField nameField = new JTextField(15);
        JTextField cnicField = new JTextField(15);
        JTextField fatherNameField = new JTextField(15);
        JTextField ageField = new JTextField(15);
        
        //nameField.setBackground(Color.LIGHT_GRAY);
        //cnicField.setBackground(Color.LIGHT_GRAY);
        //fatherNameField.setBackground(Color.LIGHT_GRAY);
        //ageField.setBackground(Color.LIGHT_GRAY);

        setComponentColors(nameField, cnicField, fatherNameField, ageField);

        panels[0].add(voterLabels[0]);
        panels[0].add(nameField);

        panels[1].add(voterLabels[1]);
        panels[1].add(cnicField);

        panels[2].add(voterLabels[2]);
        panels[2].add(fatherNameField);

        panels[3].add(voterLabels[3]);
        panels[3].add(ageField);

        // Add a FocusListener to the ageField JTextField
        ageField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                // Get the text from the age field
                String ageText = ageField.getText();
                try {
                    // Parse the age text to an integer
                    int age = Integer.parseInt(ageText);
                    // Check if age is greater than 18
                    if (age < 18) {
                        // Display an error message
                        JOptionPane.showMessageDialog(null, "Age must be greater or equal to 18.", "Age Error", JOptionPane.ERROR_MESSAGE);
                        // Clear the age field
                        ageField.setText("");
                        // Set focus back to the age field
                        ageField.requestFocus();
                    }
                } catch (NumberFormatException ex) {
                    // Handle if age is not a valid integer
                    JOptionPane.showMessageDialog(null, "Please enter a valid age.", "Age Error", JOptionPane.ERROR_MESSAGE);
                    // Clear the age field
                    ageField.setText("");
                    // Set focus back to the age field
                    ageField.requestFocus();
                }
            }
        });

        panels[4].add(voterLabels[4]);
        JRadioButton[] genderRadioButton = new JRadioButton[3];
        genderRadioButton[0] = new JRadioButton("Male");
        genderRadioButton[1] = new JRadioButton("Female");
        genderRadioButton[2] = new JRadioButton("Other");

        setComponentColors(genderRadioButton);

        ButtonGroup genderGroup = new ButtonGroup();
        for (JRadioButton button : genderRadioButton) {
            genderGroup.add(button);
            panels[4].add(button);
            button.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        gender = button.getText();
                    }
                }
            });
        }

        panels[5].add(voterLabels[5]);
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
            panels[5].add(button);
        }

        panels[6].add(voterLabels[6]);
        regionComboBox = new JComboBox<>();
        panels[6].add(regionComboBox);
        
        setComponentColors(regionComboBox);

        panels[7].add(voterLabels[7]);
        JTextField cityField = new JTextField(12);
        panels[7].add(cityField);

        panels[7].add(voterLabels[8]);
        panels[7].add(cityField);

        panels[7].add(voterLabels[9]);
        String[] provinceOptions = {"Punjab", "Sindh", "Khyber Pakhtunkhwa", "Balochistan", "Islamabad Capital Territory", "Azad Kashmir", "Gilgit-Baltistan"};
        JComboBox<String> provinceComboBox = new JComboBox<>(provinceOptions);
        panels[7].add(provinceComboBox);

        setComponentColors(cityField, provinceComboBox);

        add(formPanel, BorderLayout.CENTER);

        JButton submitButton = new JButton("Submit");
        submitButton.setBackground(Color.BLUE);
        submitButton.setForeground(Color.WHITE);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // Make the panel transparent
        buttonPanel.add(submitButton);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String addName = nameField.getText();
                String addCNIC = cnicField.getText();
                String fatherName = fatherNameField.getText();
                String age = ageField.getText();
                String regionSelected = (String) regionComboBox.getSelectedItem();
                String city = cityField.getText();
                String province = (String) provinceComboBox.getSelectedItem();
                if (addName.isEmpty() || addCNIC.isEmpty() || fatherName.isEmpty()
                        || age.isEmpty() || gender == null || electionId == 0 || city.isEmpty()
                        || province.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter data in all fields.");
                } else {
                    insertIntoDatabase(addName, addCNIC, fatherName, age, gender, electionId, regionSelected, city, province);
                    dispose(); // Close the frame after submitting
                }
            }
        });

        add(buttonPanel, BorderLayout.SOUTH);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(new File("Add path to the image"));
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
        }
    }

    private int getSelectedIndex() {
        for (int i = 0; i < electionTypeRadioButton.length; i++) {
            if (electionTypeRadioButton[i].isSelected()) {
                return i;
            }
        }
        return -1;
    }

    public String getBoothNameText() {
        String[] BoothNames = {"XXX School", "YYY College", "ZZZ University"};
        return BoothNames[random.nextInt(3)];
    }

    private void insertIntoDatabase(String addName, String addCNIC, String fatherName, String uncheckedAge, String gender, int electionId, String regionSelected, String city, String province) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/voting_system", "root", "root");
            PreparedStatement statement1 = connection.prepareStatement("INSERT INTO Voter(Name, CNIC, FatherName, Age, Gender, ElectionId) VALUES (?, ?, ?, ?, ?, ?);");
            statement1.setString(1, addName);
            statement1.setString(2, addCNIC);
            statement1.setString(3, fatherName);
            int age;
            try {
                age = Integer.parseInt(uncheckedAge);
            } catch (NumberFormatException e) {
                age = -1;
                throw new SQLException("Invalid age");
            }
            if (age != -1)
                statement1.setInt(4, age);
            statement1.setString(5, gender);
            statement1.setInt(6, electionId);

            int rowsAffected1 = statement1.executeUpdate();
            if (rowsAffected1 > 0)
                ;
            else
                JOptionPane.showMessageDialog(this, "Failed to add voter", "Error", JOptionPane.ERROR_MESSAGE);

            int regionId = getRegionId(connection, regionSelected);
            PreparedStatement statement2 = connection.prepareStatement("INSERT INTO Voting_Address (PollingBoothNo, RegionId, BoothName, City, Province) VALUES(?, ?, ?, ?, ?);");
            statement2.setInt(1, random.nextInt(10000) + 1);
            statement2.setInt(2, regionId);
            statement2.setString(3, getBoothNameText());
            statement2.setString(4, city);
            statement2.setString(5, province);

            int rowsAffected2 = statement2.executeUpdate();
            if (rowsAffected2 > 0)
                JOptionPane.showMessageDialog(this, "Voter added successfully");
            else
                JOptionPane.showMessageDialog(this, "Failed to add voter", "Error", JOptionPane.ERROR_MESSAGE);

            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to add voter: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public int getRegionId(Connection connection, String regionSelected) {
        int regionId = 0;
        try {
            PreparedStatement statement = connection.prepareStatement("Select regionId from region where VotingRegion = ?;");
            statement.setString(1, regionSelected);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                regionId = resultSet.getInt("regionId");
            } else {
                JOptionPane.showMessageDialog(this, "No region found for the selected region.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return regionId;
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
