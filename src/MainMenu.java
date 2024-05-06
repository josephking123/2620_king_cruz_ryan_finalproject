import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenu extends JFrame{

    private JButton mode1Button, mode2Button, mode3Button, leaderboardButton, exitButton, rulesButton;
    private JLabel titleLabel;
    private JPanel mainPanel;

    public MainMenu() {
        setTitle("Ping Pong Game");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        titleLabel = new JLabel("Ping Pong Game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10));

        mode1Button = new JButton("Classic");
        mode2Button = new JButton("Mode 2");
        mode3Button = new JButton("Mode 3");
        leaderboardButton = new JButton("Leaderboard");
        exitButton = new JButton("Exit");
        rulesButton = new JButton("Rules");

        mode1Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MainMode normalPong = new MainMode();
            }
        });

        mode2Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle mode 2 action
            }
        });

        mode3Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle mode 3 action
            }
        });


        leaderboardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle leaderboard action
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
                JOptionPane.showMessageDialog(MainMenu.this, "Classic: Classic pong. Players volley the ball back and forth, with the ball moving faster after every return. A player wins when they score 7 points.", "Game Rules", JOptionPane.INFORMATION_MESSAGE);
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
        public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainMenu().setVisible(true);
            }
        });
    }
}