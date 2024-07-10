package VotingSystem;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

public class GUI extends JFrame {
    JButton addButton;
    JButton removeButton;
    JButton conductElection;
    JButton addCandidateButton;
    JButton removeCandidateButton;
    JButton addPartyButton;
    JButton removePartyButton;
    Image backgroundImage;

    public GUI() {
        super("Voting System");
        loadBackgroundImage();
        initializeUI();
    }

    private void loadBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(new File("C:\\Users\\abdul\\OneDrive\\Pictures\\Voting System Project pics\\1.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeUI() {
        setUndecorated(true); // Remove the default title bar
        setSize(480, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create a custom title bar
        JPanel titleBar = createCustomTitleBar();
        add(titleBar, BorderLayout.NORTH);

        CustomPanel mainPanel = new CustomPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        conductElection = new JButton("Conduct Election");
        addButton = new JButton("Add Voter");
        removeButton = new JButton("Remove Voter");
        addCandidateButton = new JButton("Add Candidate");
        removeCandidateButton = new JButton("Remove Candidate");
        addPartyButton = new JButton("Add Party");
        removePartyButton = new JButton("Remove Party");

        // Set button colors
        setButtonColor(conductElection, Color.BLUE, Color.WHITE);
        setButtonColor(addButton, Color.BLUE, Color.WHITE);
        setButtonColor(removeButton, Color.BLUE, Color.WHITE);
        setButtonColor(addCandidateButton, Color.BLUE, Color.WHITE);
        setButtonColor(removeCandidateButton, Color.BLUE, Color.WHITE);
        setButtonColor(addPartyButton, Color.BLUE, Color.WHITE);
        setButtonColor(removePartyButton, Color.BLUE, Color.WHITE);

        JPanel p1 = new JPanel(new GridLayout(6, 1, 5, 5));
        p1.setOpaque(false); // Make the panel transparent
        p1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        p1.add(Box.createVerticalStrut(30)); // Add vertical space
        p1.add(conductElection);
        p1.add(Box.createVerticalStrut(30)); // Add vertical space

        JPanel updateVoterPanel = new JPanel();
        updateVoterPanel.setOpaque(false);
        updateVoterPanel.setLayout(new FlowLayout());
        updateVoterPanel.add(addButton);
        updateVoterPanel.add(removeButton);

        JPanel updateCandidatePanel = new JPanel();
        updateCandidatePanel.setOpaque(false);
        updateCandidatePanel.setLayout(new FlowLayout());
        updateCandidatePanel.add(addCandidateButton);
        updateCandidatePanel.add(removeCandidateButton);

        JPanel updatePartyPanel = new JPanel();
        updatePartyPanel.setOpaque(false);
        updatePartyPanel.setLayout(new FlowLayout());
        updatePartyPanel.add(addPartyButton);
        updatePartyPanel.add(removePartyButton);

        p1.add(updateVoterPanel);
        p1.add(updateCandidatePanel);
        p1.add(updatePartyPanel);

        mainPanel.add(Box.createVerticalGlue()); // Center the content vertically
        mainPanel.add(p1);
        mainPanel.add(Box.createVerticalGlue()); // Center the content vertically

        add(mainPanel, BorderLayout.CENTER);

        conductElection.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                ElectionTypeSelection election = new ElectionTypeSelection(GUI.this);
                SwingUtilities.invokeLater(() -> {
                    election.setVisible(true);
                });
                dispose();
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                AddVoter addVoter = new AddVoter();
                SwingUtilities.invokeLater(() -> addVoter.setVisible(true));
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                RemoveVoter removeVoter = new RemoveVoter();
                SwingUtilities.invokeLater(() -> removeVoter.setVisible(true));
            }
        });

        addCandidateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                AddCandidate addCandidate = new AddCandidate();
                SwingUtilities.invokeLater(() -> addCandidate.setVisible(true));
            }
        });

        removeCandidateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                RemoveCandidate removeCandidate = new RemoveCandidate();
                SwingUtilities.invokeLater(() -> removeCandidate.setVisible(true));
            }
        });

        setLocationRelativeTo(null);

        addPartyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                AddParty addParty = new AddParty();
                SwingUtilities.invokeLater(() -> addParty.setVisible(true));
            }
        });

        removePartyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                RemoveParty removeParty = new RemoveParty();
                SwingUtilities.invokeLater(() -> removeParty.setVisible(true));
            }
        });
    }

    private JPanel createCustomTitleBar() {
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(Color.BLUE);
        titleBar.setPreferredSize(new Dimension(getWidth(), 30));

        JLabel titleLabel = new JLabel("Voting System");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonsPanel.setOpaque(false);

        JButton minimizeButton = createTitleBarButton("-");
        minimizeButton.addActionListener(e -> setState(Frame.ICONIFIED));

        JButton maximizeButton = createTitleBarButton("⬜");
        maximizeButton.addActionListener(new ActionListener() {
            private boolean maximized = false;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (maximized) {
                    setExtendedState(JFrame.NORMAL);
                    maximized = false;
                } else {
                    setExtendedState(JFrame.MAXIMIZED_BOTH);
                    maximized = true;
                }
            }
        });

        JButton closeButton = createTitleBarButton("X");
        closeButton.addActionListener(e -> System.exit(0));

        buttonsPanel.add(minimizeButton);
        buttonsPanel.add(maximizeButton);
        buttonsPanel.add(closeButton);

        titleBar.add(titleLabel, BorderLayout.WEST);
        titleBar.add(buttonsPanel, BorderLayout.EAST);

        titleBar.addMouseListener(new MouseAdapter() {
            private int mouseX;
            private int mouseY;

            @Override
            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });

        titleBar.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getXOnScreen() - mouseX;
                int y = e.getYOnScreen() - mouseY;
                setLocation(x, y);
            }
        });

        return titleBar;
    }

    private JButton createTitleBarButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(Color.BLUE);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        button.setFocusPainted(false);
        return button;
    }

    private void setButtonColor(JButton button, Color bgColor, Color fgColor) {
        button.setBackground(bgColor);
        button.setForeground(fgColor);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUI().setVisible(true));
    }
}
