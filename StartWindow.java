import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

public class StartWindow extends JFrame {
  public StartWindow() {
    setTitle("Welcome to Pokemon Battle");
    setSize(800, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(null); // Use null layout for absolute positioning

    BackgroundPanel backgroundPanel = new BackgroundPanel("src/resources/BG.png");
    backgroundPanel.setLayout(null); // Use null layout for absolute positioning
    setContentPane(backgroundPanel);

    // Create a JLabel for the start button
    ImageIcon startIcon = new ImageIcon("src/resources/START.png");
    JLabel startLabel = new JLabel(startIcon);

    // Set the position and size of the start button based on the actual size of the image
    int buttonX = 225; // X-coordinate
    int buttonY = 350; // Y-coordinate
    int buttonWidth = startIcon.getIconWidth(); // Button width
    int buttonHeight = startIcon.getIconHeight(); // Button height

    startLabel.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);

    startLabel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        // Create and display the game GUI
        EventQueue.invokeLater(() -> {
          createAndShowGameGUI();
        });
        // Close the start window
        dispose();
      }
    });

    backgroundPanel.add(startLabel);
  }

  // Custom background panel class
  class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(String filePath) {
      try {
        backgroundImage = new ImageIcon(filePath).getImage();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      if (backgroundImage != null) {
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
      }
    }
  }

  private void createAndShowGameGUI() {
    Skill thunderbolt = new Skill("電擊", 40, 10, null, 0, "Paralyze", 1.00);
    Skill quickAttack = new Skill("伏特攻擊", 10, 20);
    Skill ironTail = new Skill("鐵尾", 30, 15);
    Skill electroBall = new Skill("電球", 25, 10, null, 0, "Paralyze", 0.05);
    Skill ember = new Skill("火花", 30, 15, null, 0, "Burn", 1.00, 2); // 添加燃烧效果
    Skill dragonBreath = new Skill("龍息", 40, 10, null, 0, "Burn", 1.00, 2); // 添加燃烧效果
    Skill waterGun = new Skill("水枪", 30, 15, null, 0, "Soak", 1.00, 2); // 新增技能：水枪

    Pokemon pikachu = new Pokemon("比卡超", 10, 100, 15, Arrays.asList(thunderbolt, quickAttack, ironTail, electroBall), "src/resources/pokemon1.png");
    Pokemon charmander = new Pokemon("小火龍", 8, 80, 12, Arrays.asList(new Skill("抓擊", 15, 35), ember, new Skill("咆哮", 0, 40, "Attack Boost", 3), dragonBreath), "src/resources/pokemon2.png");
    Pokemon squirtle = new Pokemon("杰尼龟", 9, 90, 14, Arrays.asList(new Skill("抓擊", 15, 35), waterGun, new Skill("守住", 0, 20), new Skill("水流喷射", 25, 15)), "src/resources/pokemon3.png");

    GameGUI gameGUI = new GameGUI(pikachu, charmander, squirtle);
    gameGUI.setVisible(true);
  }

  public static void main(String[] args) {
    EventQueue.invokeLater(() -> {
      StartWindow startWindow = new StartWindow();
      startWindow.setVisible(true);
    });
  }
}
