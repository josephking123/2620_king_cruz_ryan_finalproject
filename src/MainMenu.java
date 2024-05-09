import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

/**
 * The MainMenu class represents the main menu of the Ping Pong Game.
 * It allows the user to select different game modes, view the leaderboard, and exit the game.
 */

public class MainMenu extends JFrame{

    private JButton mode1Button, mode2Button, mode3Button, leaderboardButton, exitButton, rulesButton;
    private JLabel titleLabel;
    private JPanel mainPanel;
    private Leaderboard leaderboard;


    /**
     * Constructs a new MainMenu object.
     * Sets up the GUI components of the main menu.
     */
    public MainMenu() {
        setTitle("Ping Pong Game");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        leaderboard = new Leaderboard();

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        titleLabel = new JLabel("Ping Pong Game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10));

        mode1Button = new JButton("Classic");
        mode2Button = new JButton("Rally");
        mode3Button = new JButton("Castle Defense");
        leaderboardButton = new JButton("Leaderboard");
        exitButton = new JButton("Exit");
        rulesButton = new JButton("Rules");

        /*
         * 
         * Action Listeners for mode buttons
         */
        mode1Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                @SuppressWarnings("unused")
                Classic classicPong = new Classic(leaderboard);
            }
        });

        mode2Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                @SuppressWarnings("unused")
                RallyMode rallyPong = new RallyMode(leaderboard);
            }
        });

        mode3Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                @SuppressWarnings("unused")
                CastleDefenseMode castlePong = new CastleDefenseMode();
            }
        });


        leaderboardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLeaderboard();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        rulesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle rules action
                JOptionPane.showMessageDialog(MainMenu.this, "Classic: Classic pong. Players volley the ball back and forth, with the ball moving faster after every return. A player wins when they score 3 points but have to win by 2 points.\n Rally: Rally pong.  Get the highest score by rallying between you and your opponent.\n Castle Defense: Defend your Castle!  Each wall can take three hits, but once the ball hits your edge it's game over.", "Game Rules", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        buttonPanel.add(mode1Button);
        buttonPanel.add(mode2Button);
        buttonPanel.add(mode3Button);
        buttonPanel.add(leaderboardButton);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        JPanel topButtonPanel = new JPanel(new BorderLayout());
        topButtonPanel.add(exitButton, BorderLayout.WEST);
        topButtonPanel.add(titleLabel);
        topButtonPanel.add(rulesButton, BorderLayout.EAST);
        mainPanel.add(topButtonPanel, BorderLayout.NORTH);


        add(mainPanel);
    }

    /**
     * Retrieves the leaderboard instance associated with the main menu.
     * @return The leaderboard instance.
     */
    public Leaderboard getLeaderboard() {
        return leaderboard;
    }

    /**
     * Displays the leaderboard in a dialog box.
     */
    private void showLeaderboard() {
        HashMap<String, Integer> highScores = leaderboard.getAllHighScores();
        StringBuilder leaderboardText = new StringBuilder("Leaderboard:\n");
        for (String mode : highScores.keySet()) {
            leaderboardText.append(mode).append(": ").append(highScores.get(mode)).append("\n");
        }
        JOptionPane.showMessageDialog(MainMenu.this, leaderboardText.toString(), "Leaderboard", JOptionPane.INFORMATION_MESSAGE);
    }
       
}