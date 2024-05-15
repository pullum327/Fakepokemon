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
  private int defenseBoost;
  private int defenseBoostDuration;
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
    this.defenseBoost = 0;
    this.defenseBoostDuration = 0;
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

  public int getDefenseBoost() {
    return defenseBoost;
  }

  public int getDefenseBoostDuration() {
    return defenseBoostDuration;
  }

  public void setDefenseBoost(int defenseBoost, int defenseBoostDuration) {
    this.defenseBoost = defenseBoost;
    this.defenseBoostDuration = defenseBoostDuration;
  }

  public void takeDamage(int damage) {
    int finalDamage = damage;
    if (defenseBoostDuration > 0) {
      finalDamage = Math.max(0, damage - defenseBoost);
    }
    health -= finalDamage;
    if (health < 0) {
      health = 0;
    }
  }

  public void applyEffect(Skill skill) {
    if (skill.getEffect() != null) {
      if (skill.getEffect().equals("Attack Boost")) {
        attackBoost += 5;
        boostDuration = skill.getEffectDuration();
      } else if (skill.getEffect().equals("Defense Boost")) {
        setDefenseBoost(5, skill.getEffectDuration());
      }
    }
  }

  public void updateEffects() {
    if (boostDuration > 0) {
      boostDuration--;
      if (boostDuration == 0) {
        attackBoost = 0;
      }
    }

    if (defenseBoostDuration > 0) {
      defenseBoostDuration--;
      if (defenseBoostDuration == 0) {
        defenseBoost = 0;
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
