package VotingSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class GiveVote extends JFrame {
    private JLabel imageLabel;
    private String[] imagePaths = {
        // Add path to the images of famous election quotes, so that voter feels good to vote.
        // these are just sample images.
            // "C:\\Users\\abdul\\OneDrive\\Pictures\\Voting System Project pics\\quote 1.png",
            // "C:\\Users\\abdul\\OneDrive\\Pictures\\Voting System Project pics\\quote 2.png",
            // "C:\\Users\\abdul\\OneDrive\\Pictures\\Voting System Project pics\\quote 3.png",
            // "C:\\Users\\abdul\\OneDrive\\Pictures\\Voting System Project pics\\quote 4.png"
    };
    private String title;
    private String region;
    private int electionId;
    int[] voteCounts;
    String[] partyNames;
    int totalVotes;

    public GiveVote(String title, String region, int electionId) {
        this.title = title;
        this.region = region;
        this.electionId = electionId;
        createAndShowGUI();
        setVisible(true);
    }

    public GiveVote(String title, String region, int electionId, int[] voteCounts, String[] partyNames, int totalVotes) {
        this.title = title;
        this.region = region;
        this.electionId = electionId;
        this.voteCounts = voteCounts;
        this.partyNames = partyNames;
        this.totalVotes = totalVotes;
        createAndShowGUI();
        setVisible(true);
    }

    private void createAndShowGUI() {
        setTitle("Let's Vote");
        setSize(410, 295);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE); // Set content panel background to white

        // Image section
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new BorderLayout());
        imagePanel.setBackground(Color.WHITE); // Set image panel background to white
        String imagePath = getRandomImagePath();
        ImageIcon icon = new ImageIcon(imagePath);
        imageLabel = new JLabel();

        if (icon.getIconWidth() == -1) {
            System.out.println("Image not found at: " + imagePath);
            imageLabel.setText("Image not found");
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        } else {
            imageLabel.setIcon(icon);
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        }

        imagePanel.add(imageLabel);
        contentPanel.add(imagePanel);

        // Add empty space to ensure the content is long enough to require scrolling
        JPanel spacerPanel1 = new JPanel();
        spacerPanel1.setPreferredSize(new Dimension(400, 40)); // Adjust height as needed
        spacerPanel1.setBackground(Color.WHITE); // Set spacer panel background to white
        contentPanel.add(spacerPanel1);

        // Create vote panel with empty labels around the central area
        JPanel votePanel = new JPanel();
        votePanel.setLayout(new BorderLayout());
        votePanel.setBackground(Color.WHITE); // Set vote panel background to white
        JLabel[] emptyLabels = new JLabel[4];
        String[] directions = {BorderLayout.NORTH, BorderLayout.SOUTH, BorderLayout.EAST, BorderLayout.WEST};

        for (int i = 0; i < emptyLabels.length; i++) {
            emptyLabels[i] = new JLabel();
            emptyLabels[i].setBackground(Color.WHITE); // Set empty labels background to white
            votePanel.add(emptyLabels[i], directions[i]);
        }

        // Inner panel to center the vote button
        JPanel centerPanel = new JPanel(new FlowLayout());
        centerPanel.setBackground(Color.WHITE); // Set center panel background to white
        JButton voteButton = new JButton("Vote");
        centerPanel.add(voteButton);
        votePanel.add(centerPanel, BorderLayout.CENTER);

        voteButton.setFont(new Font("Arial", Font.PLAIN, 16));
        voteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Navigate to VoterCredentials
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        new VoterCredentials(title, region, electionId).setVisible(true);
                    }
                });
                dispose();
            }
        });

        // End election button
        JPanel endPanel = new JPanel();
        endPanel.setBackground(Color.WHITE); // Set end panel background to white
        JButton endButton = new JButton("End Election");
        endButton.setFont(new Font("Arial", Font.PLAIN, 12));
        endButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (voteCounts == null) {
                            int option = JOptionPane.showConfirmDialog(
                                    null,
                                    "        No one has voted in this election. \nAre you sure you want to end this election?",
                                    "Confirm",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.WARNING_MESSAGE
                            );
                            if (option == JOptionPane.YES_OPTION) {
                                int numberOfCandidates = partyNames.length; // Assuming partyNames array has the length equal to the number of candidates/parties
                                voteCounts = new int[numberOfCandidates];
                                for (int i = 0; i < voteCounts.length; i++)
                                    voteCounts[i] = 0;
                                new EndElection(voteCounts, partyNames, region, totalVotes).setVisible(true);
                            }
                            if (option == JOptionPane.NO_OPTION) {
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        new GiveVote(title, region, electionId).setVisible(true);
                                    }
                                });
                                dispose();
                            }
                        } else
                            new EndElection(voteCounts, partyNames, region, totalVotes).setVisible(true);
                    }
                });
                dispose();
            }
        });
        endPanel.add(endButton);

        // Add vote panel to the content panel
        contentPanel.add(votePanel);

        // Add empty space to ensure the content is long enough to require scrolling
        JPanel spacerPanel2 = new JPanel();
        spacerPanel2.setPreferredSize(new Dimension(400, 30)); // Adjust height as needed
        spacerPanel2.setBackground(Color.WHITE); // Set spacer panel background to white
        contentPanel.add(spacerPanel2);

        // Add end button to the bottom of the content panel
        contentPanel.add(endPanel);

        // Add contentPanel to a JScrollPane with vertical scrolling only
        JScrollPane scrollPane = new JScrollPane(contentPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Add JScrollPane to the frame
        add(scrollPane, BorderLayout.CENTER);
    }

    private String getRandomImagePath() {
        Random random = new Random();
        int index = random.nextInt(imagePaths.length);
        return imagePaths[index];
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                new GiveVote();
//            }
//        });
//    }
}

