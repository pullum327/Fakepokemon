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
    ImageIcon icon = new ImageIcon("src/resources/pokemon4_fainted.png"); // 替换为你的图标路径
    setIconImage(icon.getImage());
    setLayout(null); // Use null layout for absolute positioning

    BackgroundPanel backgroundPanel = new BackgroundPanel("src/resources/BG.png");
    backgroundPanel.setLayout(null); // Use null layout for absolute positioning
    setContentPane(backgroundPanel);

    // Create a JLabel for the start button
    ImageIcon startIcon = new ImageIcon("src/resources/START.png");
    JLabel startLabel = new JLabel(startIcon);

    // Set the position and size of the start button based on the actual size of the image
    int buttonX = 210; // X-coordinate
    int buttonY = 370; // Y-coordinate
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


  private void createAndShowGameGUI() {
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
            new Skill("守住", 0, 20, "Damage Reduction", 3, null, 0.0, 0, 5, 3), // 添加守住技能
            new Skill("水流喷射", 25, 15)
    ), "src/resources/pokemon3.png");

    Pokemon newBulbasaur = new Pokemon("妙蛙种子", 9, 90, 14, Arrays.asList(
            new Skill("藤鞭", 35, 15),
            new Skill("种子炸弹", 45, 10),
            new Skill("麻痹粉", 0, 20, null, 0, "Paralyze", 0.2),
            new Skill("光合作用", 0, 5, "Heal", 0)
    ), "src/resources/pokemon4.png");

    GameGUI newGameGUI = new GameGUI(newPikachu, newCharmander, newSquirtle,newBulbasaur);
    newGameGUI.setVisible(true);
  }

  public static void main(String[] args) {
    EventQueue.invokeLater(() -> {
      StartWindow startWindow = new StartWindow();
      startWindow.setVisible(true);
    });
  }
}
