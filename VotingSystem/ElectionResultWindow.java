package VotingSystem;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

public class ElectionResultWindow extends JFrame {
    String[] partyNames;

    public ElectionResultWindow(int[] voteCounts, String[] partyNames, String region, int totalVotes) {
        super("Election Result");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create main panel with background image
        JPanel mainPanel = new ImagePanel(new ImageIcon("Add path to the background image for this frame.").getImage());
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Find the winning party
        int maxVotes = 0;
        int winningPartyIndex = -1;
        for (int i = 0; i < voteCounts.length; i++) {
            if (voteCounts[i] > maxVotes) {
                maxVotes = voteCounts[i];
                winningPartyIndex = i;
            }
        }

        // Title panel
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel(region + " Election Result");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.setOpaque(false); // Make the panel transparent
        titlePanel.add(titleLabel);

        // Label for the winning party
        JPanel winningPanel = new JPanel();
        JLabel winningPartyLabel = new JLabel("Winner: \"" + partyNames[winningPartyIndex] + "\" with " + maxVotes + " votes");
        winningPartyLabel.setFont(new Font("Arial", Font.ITALIC, 20));
        winningPartyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        winningPanel.setOpaque(false); // Make the panel transparent
        winningPanel.add(winningPartyLabel);

        // Result panel
        JPanel resultPanel = new JPanel(new GridLayout(0, 1, 0, 10));
        resultPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        resultPanel.setOpaque(false); // Make the panel transparent

        // Table header
        JPanel headerPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        headerPanel.setBorder(new EmptyBorder(0, 20, 0, 20));
        headerPanel.setOpaque(false); // Make the panel transparent
        JLabel partyLabel = new JLabel("Party");
        partyLabel.setFont(new Font("Serif", Font.BOLD, 16));
        JLabel votesLabel = new JLabel("Votes");
        votesLabel.setFont(new Font("Serif", Font.BOLD, 16));
        partyLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));
        headerPanel.add(partyLabel);
        headerPanel.add(votesLabel);
        headerPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK));
        resultPanel.add(headerPanel);

        // Add results to result panel
        for (int i = 0; i < partyNames.length; i++) {
            JPanel rowPanel = new JPanel(new GridLayout(1, 3, 10, 0));
            JLabel partyNameLabel = new JLabel(partyNames[i]);
            JLabel votesCountLabel = new JLabel(String.valueOf(voteCounts[i]));
            partyNameLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));
            rowPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK));
            rowPanel.setOpaque(false); // Make the panel transparent
            rowPanel.add(partyNameLabel);
            rowPanel.add(votesCountLabel);
            resultPanel.add(rowPanel);
        }

        // Total votes panel
        JPanel totalVotesPanel = new JPanel();
        JLabel totalVotesLabel = new JLabel("Total Votes: " + totalVotes);
        totalVotesPanel.setOpaque(false); // Make the panel transparent
        totalVotesPanel.add(totalVotesLabel);

        // Add components to main panel
        mainPanel.add(titlePanel);
        mainPanel.add(winningPanel);
        mainPanel.add(resultPanel);
        mainPanel.add(totalVotesPanel);

        // Add main panel to frame
        add(mainPanel);
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    // Inner class for background image panel
    class ImagePanel extends JPanel {
        private Image backgroundImage;

        public ImagePanel(Image backgroundImage) {
            this.backgroundImage = backgroundImage;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Create a graphics2D object
            Graphics2D g2d = (Graphics2D) g.create();
            // Set the alpha composite for 30% opacity
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
            // Draw the background image
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            // Dispose of the graphics2D object
            g2d.dispose();
        }
    }

//    public static void main(String[] args) {
//        // Example usage
//        int[] voteCounts = {5000, 3000, 2000}; // Example vote counts
//        String[] partyNames = {"Party A", "Party B", "Party C"}; // Example party names
//        String region = "Region X"; // Example region name
//        int totalVotes = 10000; // Example total votes
//        new ElectionResultWindow(voteCounts, partyNames, region, totalVotes);
//    }
}
