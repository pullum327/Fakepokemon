import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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
  private JLabel outputLabel;
  private LinkedList<String> outputMessages;
  private JPanel skillPanel;
  private boolean isPlayerTurn = true;

  public GameGUI(Pokemon player, Pokemon... opponents) {
    this.player = player;
    this.opponent = selectRandomOpponent(Arrays.asList(opponents));
    this.battle = new Battle(player, opponent, this);

    setTitle("Pokemon Battle");
    setSize(800, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    ImageIcon icon = new ImageIcon("src/resources/pokemon4_fainted.png"); // 替换为你的图标路径
    setIconImage(icon.getImage());


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

    outputLabel = new JLabel("", SwingConstants.LEFT);
    outputLabel.setFont(new Font("微軟正黑體", Font.BOLD, 16));
    outputLabel.setForeground(Color.WHITE);
    outputLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
    outputLabel.setOpaque(true);
    outputLabel.setBackground(new Color(0, 0, 0, 200));

    outputPanel = new JPanel(new BorderLayout());
    outputPanel.setOpaque(false);
    outputPanel.add(outputLabel, BorderLayout.NORTH);

    outputMessages = new LinkedList<>();

    topPanel.add(outputPanel);
    topPanel.add(Box.createHorizontalGlue());
    topPanel.add(opponentPanel);

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
    gbc.insets = new Insets(5, 5, 5, 5);

    String[] skillIconPaths = {"src/resources/skill1.png", "src/resources/skill2.png", "src/resources/skill3.png", "src/resources/skill4.png"};
    int numSkills = Math.min(skillIconPaths.length, player.getSkills().size());
    skillButtons = new JButton[numSkills];

    for (int i = 0; i < numSkills; i++) {
      Skill skill = player.getSkills().get(i);
      ImageIcon originalIcon = new ImageIcon(skillIconPaths[i]);
      Image scaledImage = originalIcon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
      ImageIcon scaledIcon = new ImageIcon(scaledImage);

      skillButtons[i] = new JButton(scaledIcon);
      if (i == 0) {
        skillButtons[i].setToolTipText("<html>" + skill.getName() + " (Power: " + skill.getPower() + ", Uses: " + skill.getRemainingUses() + ")<br>有10%機率造成麻痺!</html>");
      } else if (i==3) {
        skillButtons[i].setToolTipText("<html>" + skill.getName() + " (Power: " + skill.getPower() + ", Uses: " + skill.getRemainingUses() + ")<br>有5%機率造成麻痺!</html>");
      } else {
        skillButtons[i].setToolTipText(skill.getName() + " (Power: " + skill.getPower() + ", Uses: " + skill.getRemainingUses() + ")");
      }
      skillButtons[i].setPreferredSize(new Dimension(64, 64));

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
            updateSkillToolTips();
            updateHpBars();
            if (checkGameOver()) {
              disableSkillButtons();
            } else {
              isPlayerTurn = false;
              opponentAttack();
            }
          }
        }
      });
    }

    bottomPanel.add(skillPanel, BorderLayout.EAST);

    add(topPanel, BorderLayout.NORTH);
    add(bottomPanel, BorderLayout.SOUTH);
  }

  private Pokemon selectRandomOpponent(List<Pokemon> opponents) {
    Random random = new Random();
    return opponents.get(random.nextInt(opponents.size()));
  }

  public JLabel getPlayerImageLabel() {
    return playerImageLabel;
  }

  public JLabel getOpponentImageLabel() {
    return opponentImageLabel;
  }

  public JLabel getImageLabel(Pokemon pokemon) {
    if (pokemon == player) {
      return playerImageLabel;
    } else if (pokemon == opponent) {
      return opponentImageLabel;
    } else {
      return null;
    }
  }

  public void flashImage(final JLabel imageLabel, String imagePath) {
    // Only apply the effect if the image path is "pokemon1.png" or "pokemon2.png" or "pokemon3.png" or "pokemon4.png"
    if (imagePath.contains("pokemon1.png") || imagePath.contains("pokemon2.png") || imagePath.contains("pokemon3.png") || imagePath.contains("pokemon4.png")) {
      final int delay = 100; // milliseconds
      final int totalFlashes = 5;
      Timer timer = new Timer(delay, new ActionListener() {
        private int count = 0;
        private boolean visible = true;

        @Override
        public void actionPerformed(ActionEvent e) {
          if (count < totalFlashes) {
            visible = !visible;
            imageLabel.setVisible(visible);
            count++;
          } else {
            ((Timer) e.getSource()).stop();
            imageLabel.setVisible(true);
          }
        }
      });
      timer.start();
    }
  }

  private void replaceImageWithFainted(Pokemon pokemon, JLabel imageLabel) {
    String faintedImagePath;
    if (pokemon.getImagePath().contains("pokemon1.png")) {
      faintedImagePath = "src/resources/pokemon1_fainted.png";
    } else if (pokemon.getImagePath().contains("pokemon2.png")) {
      faintedImagePath = "src/resources/pokemon2_fainted.png";
    } else if (pokemon.getImagePath().contains("pokemon3.png")) {
      faintedImagePath = "src/resources/pokemon3_fainted.png";
    } else if (pokemon.getImagePath().contains("pokemon4.png")) {
      faintedImagePath = "src/resources/pokemon4_fainted.png";
    } else {
      return;
    }
    ImageIcon faintedIcon = new ImageIcon(faintedImagePath);
    Image faintedImage = faintedIcon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
    imageLabel.setIcon(new ImageIcon(faintedImage, faintedImagePath));
  }

  private void opponentAttack() {
    if (opponent.isParalyzed()) {
      appendOutput(opponent.getName() + " 因 " + "麻痺" + " 無法行動!");
      opponent.setParalyzed(false);
      isPlayerTurn = true;
      return;
    }
    if (player.getBurnDuration() > 0) {
      appendOutput(player.getName() + " 因 " + "燃燒" + " 受到5點傷害! ");
      player.takeDamage(5);
      updateHp();
      player.setBurnDuration(player.getBurnDuration() - 1);
    }

    Timer timer = new Timer(1000, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        battle.opponentTurn();
        updateSkillToolTips();
        updateHpBars();
        if (checkGameOver()) {
          disableSkillButtons();
        } else {
          isPlayerTurn = true;
        }
        ((Timer) e.getSource()).stop();
      }
    });
    timer.setRepeats(false);
    timer.start();
  }

  private boolean checkGameOver() {
    if (player.getHealth() <= 0) {
      appendOutput(player.getName() + "失去戰鬥能力");
      replaceImageWithFainted(player, playerImageLabel);
      showGameOverDialog(opponent.getName() + "取得勝利!");
      return true;
    } else if (opponent.getHealth() <= 0) {
      appendOutput(opponent.getName() + "失去戰鬥能力");
      replaceImageWithFainted(opponent, opponentImageLabel);
      showGameOverDialog(player.getName() + "取得勝利!");
      return true;
    }
    return false;
  }

  private void showGameOverDialog(String message) {
    JDialog gameOverDialog = new JDialog(this, "遊戲結束", true);
    gameOverDialog.setLayout(new BorderLayout());
    gameOverDialog.setSize(300, 200);

    JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
    messageLabel.setFont(new Font("微軟正黑體", Font.BOLD, 18));
    messageLabel.setForeground(new Color(255, 255, 255));
    gameOverDialog.add(messageLabel, BorderLayout.CENTER);

    // Set the content pane background to transparent black
    gameOverDialog.getContentPane().setBackground(new Color(0, 0, 0, 200));

    // Make the dialog background transparent
    gameOverDialog.setUndecorated(true);  // Remove window decorations
    gameOverDialog.setBackground(new Color(0, 0, 0, 128));  // Transparent background
    gameOverDialog.setOpacity(0.85f); // Set opacity (optional for additional transparency)

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout(1, 2));

    JButton restartButton = new JButton("再來一場");
    restartButton.setFont(new Font("微軟正黑體", Font.BOLD, 16));
    restartButton.setForeground(Color.white);
    restartButton.setBackground(new Color(0, 0, 0, 128));
    restartButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        gameOverDialog.dispose();
        dispose();
        EventQueue.invokeLater(() -> {
          Pokemon newPikachu = new Pokemon("比卡超", 10, 100, 15, Arrays.asList(
                  new Skill("電擊", 40, 10, null, 0, "Paralyze", 1.00),
                  new Skill("伏特攻擊", 10, 20),
                  new Skill("鐵尾", 30, 15),
                  new Skill("電球", 25, 10, null, 0, "Paralyze", 0.05)
          ), "src/resources/pokemon1.png");

          Pokemon newCharmander = new Pokemon("小火龍", 8, 80, 12, Arrays.asList(
                  new Skill("抓擊", 15, 35),
                  new Skill("火花", 30, 15, null, 0, "Burn", 1.00, 2),
                  new Skill("咆哮", 0, 40, "Attack Boost", 3),
                  new Skill("龍息", 40, 10, null, 0, "Burn", 1.00, 2)
          ), "src/resources/pokemon2.png");

          Pokemon newSquirtle = new Pokemon("杰尼龟", 9, 90, 14, Arrays.asList(
                  new Skill("抓擊", 15, 35),
                  new Skill("水枪", 30, 15, null, 0, "Soak", 1.00, 2),
                  new Skill("守住", 0, 20, "Damage Reduction", 3, null, 0.0, 0, 5, 3),
                  new Skill("水流喷射", 25, 15)
          ), "src/resources/pokemon3.png");

          // Create Bulbasaur
          Pokemon newBulbasaur = new Pokemon("妙蛙种子", 9, 90, 14, Arrays.asList(
                  new Skill("藤鞭", 35, 15),
                  new Skill("种子炸弹", 45, 10),
                  new Skill("麻痹粉", 0, 20, null, 0, "Paralyze", 0.2),
                  new Skill("光合作用", 0, 5, "Heal", 0)
          ), "src/resources/pokemon4.png");

          GameGUI newGameGUI = new GameGUI(newPikachu, newCharmander, newSquirtle, newBulbasaur);
          newGameGUI.setVisible(true);
        });
      }
    });
    buttonPanel.add(restartButton);

    JButton backButton = new JButton("返回開始畫面");
    backButton.setFont(new Font("微軟正黑體", Font.BOLD, 16));
    backButton.setBackground(new Color(0, 0, 0, 128));
    backButton.setForeground(Color.white);
    backButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        gameOverDialog.dispose();
        EventQueue.invokeLater(() -> {
          StartWindow startWindow = new StartWindow();
          startWindow.setVisible(true);
        });
        dispose();
      }
    });
    buttonPanel.add(backButton);

    gameOverDialog.add(buttonPanel, BorderLayout.SOUTH);

    gameOverDialog.setLocationRelativeTo(this);
    gameOverDialog.setVisible(true);
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
    hpBar.setForeground(Color.RED);
    hpBar.setPreferredSize(new Dimension(200, 20));
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

    JLabel nameLabel = createStyledLabel(skill.getName(), 65);
    JLabel powerLabel = createStyledLabel("威力: " + skill.getPower(), 65);
    JLabel usesLabel = createStyledLabel("次數: " + skill.getRemainingUses() + "/" + skill.getMaxUses(), 65);

    panel.add(nameLabel);
    panel.add(powerLabel);
    panel.add(usesLabel);

    return panel;
  }

  private JLabel createStyledLabel(String text, int width) {
    JLabel label = new JLabel(text, SwingConstants.CENTER);
    label.setForeground(Color.WHITE);
    label.setFont(new Font("微軟正黑體", Font.BOLD, 12));
    label.setPreferredSize(new Dimension(width, 20));
    label.setMaximumSize(new Dimension(width, 20));
    label.setOpaque(true);
    label.setBackground(new Color(0, 0, 0, 200));
    return label;
  }

  private void updateSkillInfoPanels() {
    for (int i = 0; i < player.getSkills().size(); i++) {
      Skill skill = player.getSkills().get(i);
      JPanel skillInfoPanel = createSkillInfoPanel(skill);
      JPanel skillButtonPanel = (JPanel) skillButtons[i].getParent();
      skillButtonPanel.remove(1);
      skillButtonPanel.add(skillInfoPanel, BorderLayout.SOUTH);
    }
    skillPanel.revalidate();
    skillPanel.repaint();
  }

  private void updateSkillToolTips() {
    for (int i = 0; i < player.getSkills().size(); i++) {
      Skill skill = player.getSkills().get(i);
      if (i == 0) {
        skillButtons[i].setToolTipText("<html>" + skill.getName() + " (Power: " + skill.getPower() + ", Uses: " + skill.getRemainingUses() + ")<br>有10%機率造成麻痺!</html>");
      } else if (i==3) {
        skillButtons[i].setToolTipText("<html>" + skill.getName() + " (Power: " + skill.getPower() + ", Uses: " + skill.getRemainingUses() + ")<br>有5%機率造成麻痺!</html>");
      }else {
        skillButtons[i].setToolTipText(skill.getName() + " (Power: " + skill.getPower() + ", Uses: " + skill.getRemainingUses() + ")");
      }
    }
    updateSkillInfoPanels();
  }

  public void updateHp() {
    updateHpBars();
    updateSkillToolTips();
  }

  private void updateHpBars() {
    playerHpBar.setValue(player.getHealth());
    playerHpBar.setString(player.getHealth() + "/" + player.getMaxHealth());
    opponentHpBar.setValue(opponent.getHealth());
    opponentHpBar.setString(opponent.getHealth() + "/" + opponent.getMaxHealth());
  }

  public void appendOutput(String text) {
    if (outputMessages.size() >= 6) {
      outputMessages.poll();
    }
    outputMessages.add(text);
    StringBuilder outputHtml = new StringBuilder("<html>");
    for (String message : outputMessages) {
      outputHtml.append(message).append("<br>");
    }
    outputHtml.append("</html>");
    outputLabel.setText(outputHtml.toString());
  }
}

