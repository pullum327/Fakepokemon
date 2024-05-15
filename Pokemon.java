import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Pokemon {
  private String name;
  private int level;
  private int health;
  private int maxHealth;
  private int attack;
  private List<Skill> skills;
  private String imagePath;
  private ImageIcon imageIcon;
  private int attackBoost;
  private int boostDuration;
  private boolean paralyzed;
  private int burnDuration;
  private int damageReduction;
  private int damageReductionDuration;
  private GameGUI gameGUI;

  public Pokemon(String name, int level, int health, int attack, List<Skill> skills, String imagePath) {
    this.name = name;
    this.level = level;
    this.health = health;
    this.maxHealth = health;
    this.attack = attack;
    this.skills = skills;
    this.imagePath = imagePath;
    this.imageIcon = createResizedIcon(imagePath, 300, 300);
    this.attackBoost = 0;
    this.boostDuration = 0;
    this.paralyzed = false;
    this.burnDuration = 0;
    this.damageReduction = 0;
    this.damageReductionDuration = 0;
  }

  public void setGameGUI(GameGUI gameGUI) {
    this.gameGUI = gameGUI;
  }

  public String getName() {
    return name;
  }

  public int getHealth() {
    return health;
  }

  public int getMaxHealth() {
    return maxHealth;
  }

  public int getAttack() {
    return attack;
  }

  public int getBoostDuration() {
    return boostDuration;
  }

  public List<Skill> getSkills() {
    return skills;
  }

  public boolean isParalyzed() {
    return paralyzed;
  }

  public void setParalyzed(boolean paralyzed) {
    this.paralyzed = paralyzed;
  }

  public int getBurnDuration() {
    return burnDuration;
  }

  public void setBurnDuration(int burnDuration) {
    this.burnDuration = burnDuration;
  }

  public boolean hasDamageReductionEffect() {
    return damageReductionDuration > 0;
  }

  public void takeDamage(int damage) {
    int originalDamage = damage;
    if (damageReductionDuration > 0) {
      damage -= damageReduction;
      if (damage < 0) {
        damage = 0;
      }
      damageReductionDuration--;
      gameGUI.appendOutput(name + " 的 " + "守住效果减少了 " + (originalDamage - damage) + " 點傷害!");
    }

    health -= damage;
    if (health < 0) {
      health = 0;
    }
    if (gameGUI != null) {
      gameGUI.flashImage(gameGUI.getImageLabel(this), imagePath);
    }
    if(burnDuration == 0) {
      gameGUI.appendOutput(name + " 受到了 " + damage + " 點傷害!");
    }
  }

  public void applyEffect(Skill skill) {
    if (skill.getEffect() != null) {
      if (skill.getEffect().equals("Attack Boost")) {
        attackBoost += 5;
        boostDuration = skill.getEffectDuration();
      }
    }

    if (skill.getDamageReductionDuration() > 0) {
      damageReduction = skill.getDamageReduction();
      damageReductionDuration = skill.getDamageReductionDuration();
    }
  }

  public void updateEffects() {
    if (boostDuration > 0) {
      boostDuration--;
      if (boostDuration == 0) {
        attackBoost = 0;
      }
    }
  }

  public int calculateAttackDamage(int skillPower) {
    return skillPower + attack + attackBoost;
  }

  public ImageIcon getImageIcon() {
    return imageIcon;
  }

  public String getImagePath() {
    return imagePath;
  }

  private ImageIcon createResizedIcon(String path, int width, int height) {
    ImageIcon icon = new ImageIcon(path);
    Image img = icon.getImage();
    Image resizedImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    return new ImageIcon(resizedImage);
  }
}
