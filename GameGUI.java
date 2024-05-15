import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameGUI extends JFrame {
  private JProgressBar playerHpBar;
  private JProgressBar opponentHpBar;
  private JButton[] skillButtons;
  private JLabel playerImageLabel;
  private JLabel opponentImageLabel;
  private Pokemon player;
  private Pokemon opponent;
  private Battle battle;
  private JPanel outputPanel;
  private BorderedText borderedText;
  private JPanel skillPanel; // Declare skillPanel as a class member
  private boolean isPlayerTurn = true; // Add a boolean to control turn

  public GameGUI(Pokemon player, Pokemon opponent) {
    this.player = player;
    this.opponent = opponent;
    this.battle = new Battle(player, opponent, this);

    setTitle("Pokemon Battle");
    setSize(800, 600); // Adjust the size to make room for the output area
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Create background panel
    BackgroundPanel backgroundPanel = new BackgroundPanel("src/resources/background.png");
    backgroundPanel.setLayout(new BorderLayout());
    setContentPane(backgroundPanel);

    JPanel topPanel = new JPanel();
    topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
    topPanel.setOpaque(false);

    // Opponent HP Bar
    opponentHpBar = createHpBar(opponent.getMaxHealth(), opponent.getHealth());

    opponentImageLabel = new JLabel(opponent.getImageIcon());
    JPanel opponentPanel = createImagePanel(opponentImageLabel, opponentHpBar);

    borderedText = new BorderedText("", Color.white, Color.GRAY, new Color(0, 0, 0, 200));
    borderedText.setFont(new Font("微軟正黑體", Font.BOLD, 16));

    outputPanel = new JPanel(new BorderLayout());
    outputPanel.setOpaque(false);
    outputPanel.add(borderedText, BorderLayout.NORTH);

    topPanel.add(outputPanel); // Add the chat panel to the top panel
    topPanel.add(Box.createHorizontalGlue()); // Fill remaining space
    topPanel.add(opponentPanel); // Add opponent panel to the right

    JPanel bottomPanel = new JPanel(new BorderLayout());
    bottomPanel.setOpaque(false);

    // Player HP Bar
    playerHpBar = createHpBar(player.getMaxHealth(), player.getHealth());

    playerImageLabel = new JLabel(player.getImageIcon());
    JPanel playerPanel = createImagePanel(playerImageLabel, playerHpBar);
    bottomPanel.add(playerPanel, BorderLayout.WEST);

    skillPanel = new JPanel();
    skillPanel.setOpaque(false);
    skillPanel.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.BOTH;
    gbc.insets = new Insets(5, 5, 5, 5); // Padding between buttons

    skillButtons = new JButton[4];

    // Load and scale skill icons
    String[] skillIconPaths = {"src/resources/skill1.png", "src/resources/skill2.png", "src/resources/skill3.png", "src/resources/skill4.png"};
    for (int i = 0; i < player.getSkills().size(); i++) {
      Skill skill = player.getSkills().get(i);
      ImageIcon originalIcon = new ImageIcon(skillIconPaths[i]);
      Image scaledImage = originalIcon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
      ImageIcon scaledIcon = new ImageIcon(scaledImage);

      skillButtons[i] = new JButton(scaledIcon);
      skillButtons[i].setToolTipText(skill.getName() + " (Power: " + skill.getPower() + ", Uses: " + skill.getRemainingUses() + ")");
      skillButtons[i].setPreferredSize(new Dimension(64, 64)); // Set button size

      JPanel skillInfoPanel = createSkillInfoPanel(skill);
      JPanel skillButtonPanel = new JPanel(new BorderLayout());
      skillButtonPanel.setOpaque(false);
      skillButtonPanel.add(skillButtons[i], BorderLayout.CENTER);
      skillButtonPanel.add(skillInfoPanel, BorderLayout.SOUTH);

      gbc.gridx = i;
      gbc.gridy = 0;
      skillPanel.add(skillButtonPanel, gbc);

      int index = i;
      skillButtons[i].addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          if (isPlayerTurn) {
            battle.playerTurn(index);
            updateSkillToolTips(); // Update tooltips after each use
            updateHpBars(); // Update health bars after each attack
            if (checkGameOver()) {
              disableSkillButtons();
            } else {
              isPlayerTurn = false; // Switch turn to opponent
              opponentAttack(); // Trigger opponent's attack
            }
          }
        }
      });
    }

    bottomPanel.add(skillPanel, BorderLayout.EAST); // Add the skill panel to the bottom panel

    add(topPanel, BorderLayout.NORTH);
    add(bottomPanel, BorderLayout.SOUTH); // Add the bottom panel to the south
  }

  private void opponentAttack() {
    // Simulate a delay for opponent's turn
    Timer timer = new Timer(1000, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        battle.opponentTurn();
        updateSkillToolTips(); // Update tooltips after each use
        updateHpBars(); // Update health bars after each attack
        if (checkGameOver()) {
          disableSkillButtons();
        } else {
          isPlayerTurn = true; // Switch turn back to player
        }
        ((Timer) e.getSource()).stop(); // Stop the timer
      }
    });
    timer.setRepeats(false); // Ensure the timer only runs once
    timer.start();
  }

  private boolean checkGameOver() {
    if (player.getHealth() <= 0) {
      appendOutput(player.getName()+"失去戰鬥能力");
      return true;
    } else if (opponent.getHealth() <= 0) {
      appendOutput(opponent.getName()+"失去戰鬥能力");
      return true;
    }
    return false;
  }

  private void disableSkillButtons() {
    for (JButton button : skillButtons) {
      button.setEnabled(false);
    }
  }

  private JProgressBar createHpBar(int maxHealth, int currentHealth) {
    JProgressBar hpBar = new JProgressBar(0, maxHealth);
    hpBar.setValue(currentHealth);
    hpBar.setStringPainted(true);
    hpBar.setString(currentHealth + "/" + maxHealth);
    hpBar.setForeground(Color.RED); // Set the bar color to red
    hpBar.setPreferredSize(new Dimension(200, 20)); // Adjust the size as needed
    return hpBar;
  }

  private JPanel createImagePanel(JLabel imageLabel, JProgressBar hpBar) {
    JPanel panel = new JPanel();
    panel.setLayout(new OverlayLayout(panel));
    panel.setOpaque(false);

    JPanel hpBarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
    hpBarPanel.setOpaque(false);
    hpBarPanel.add(hpBar);

    JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
    imagePanel.setOpaque(false);
    imagePanel.add(imageLabel);

    panel.add(hpBarPanel);
    panel.add(imagePanel);

    return panel;
  }

  private JPanel createSkillInfoPanel(Skill skill) {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setOpaque(false);

    JLabel nameLabel = new JLabel(skill.getName(), SwingConstants.CENTER);
    JLabel powerLabel = new JLabel("威力: " + skill.getPower(), SwingConstants.CENTER);
    JLabel usesLabel = new JLabel("次數: " + skill.getRemainingUses() + "/" + skill.getMaxUses(), SwingConstants.CENTER);

    nameLabel.setForeground(Color.BLACK);
    nameLabel.setFont(new Font("微軟正黑體", Font.BOLD, 14));
    powerLabel.setForeground(Color.BLACK);
    powerLabel.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
    usesLabel.setForeground(Color.BLACK);
    usesLabel.setFont(new Font("微軟正黑體", Font.PLAIN, 12));

    nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    powerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    usesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    panel.add(nameLabel);
    panel.add(powerLabel);
    panel.add(usesLabel);

    return panel;
  }

  private void updateSkillInfoPanels() {
    for (int i = 0; i < player.getSkills().size(); i++) {
      Skill skill = player.getSkills().get(i);
      JPanel skillInfoPanel = createSkillInfoPanel(skill);
      JPanel skillButtonPanel = (JPanel) skillButtons[i].getParent();
      skillButtonPanel.remove(1); // Remove the old skill info panel
      skillButtonPanel.add(skillInfoPanel, BorderLayout.SOUTH); // Add the new skill info panel
    }
    skillPanel.revalidate();
    skillPanel.repaint();
  }

  private void updateSkillToolTips() {
    for (int i = 0; i < player.getSkills().size(); i++) {
      Skill skill = player.getSkills().get(i);
      skillButtons[i].setToolTipText(skill.getName() + " (Power: " + skill.getPower() + ", Uses: " + skill.getRemainingUses() + ")");
    }
    updateSkillInfoPanels(); // Update the skill info panels as well
  }

  public void updateHp() {
    updateHpBars();
    updateSkillToolTips(); // Update tooltips to reflect current uses
  }

  private void updateHpBars() {
    playerHpBar.setValue(player.getHealth());
    playerHpBar.setString(player.getHealth() + "/" + player.getMaxHealth());
    opponentHpBar.setValue(opponent.getHealth());
    opponentHpBar.setString(opponent.getHealth() + "/" + opponent.getMaxHealth());
  }

  public void appendOutput(String text) {
    borderedText.setText(text);
  }
}