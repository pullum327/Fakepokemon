import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class StartWindow extends JFrame {
  public StartWindow() {
    setTitle("Welcome to Pokemon Battle");
    setSize(800, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    // Create a panel for the start button
    JPanel startPanel = new JPanel();
    startPanel.setLayout(new GridBagLayout());
    startPanel.setOpaque(false);

    JButton startButton = new JButton("開始遊戲");
    startButton.setFont(new Font("微軟正黑體", Font.BOLD, 30));
    startButton.setPreferredSize(new Dimension(200, 60));

    startButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // Create and display the game GUI
        EventQueue.invokeLater(new Runnable() {
          @Override
          public void run() {
            createAndShowGameGUI();
          }
        });
        // Close the start window
        dispose();
      }
    });

    startPanel.add(startButton);

    add(startPanel, BorderLayout.CENTER);
  }

  private void createAndShowGameGUI() {
    Skill thunderbolt = new Skill("電擊", 40, 10, null, 0, "Paralyze", 0.10);
    Skill quickAttack = new Skill("伏特攻擊", 20, 20);
    Skill ironTail = new Skill("鐵尾", 30, 15);
    Skill electroBall = new Skill("電球", 25, 10, null, 0, "Paralyze", 0.05);

    Pokemon pikachu = new Pokemon("比卡超", 10, 100, 15, Arrays.asList(thunderbolt, quickAttack, ironTail, electroBall), "src/resources/pokemon1.png");

    Skill scratch = new Skill("抓擊", 15, 35);
    Skill ember = new Skill("火花", 30, 15);
    Skill opponentGrowl = new Skill("咆哮", 0, 40, "Attack Boost", 3);
    Skill dragonBreath = new Skill("龍息", 40, 10);

    Pokemon charmander = new Pokemon("小火龍", 8, 80, 12, Arrays.asList(scratch, ember, opponentGrowl, dragonBreath), "src/resources/pokemon2.png");

    GameGUI gameGUI = new GameGUI(pikachu, charmander);
    gameGUI.setVisible(true);
  }
}
