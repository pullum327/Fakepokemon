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
  private boolean paralyzed; // Add this field

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
    this.paralyzed = false; // Initialize the field
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

  public void takeDamage(int damage) {
    health -= damage;
    if (health < 0) {
      health = 0;
    }
  }

  public void applyEffect(Skill skill) {
    if (skill.getEffect() != null) {
      if (skill.getEffect().equals("Attack Boost")) {
        attackBoost += 5;
        boostDuration = skill.getEffectDuration();
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
  }

  public int calculateAttackDamage(int skillPower) {
    return skillPower + attack + attackBoost;
  }

  public ImageIcon getImageIcon() {
    return imageIcon;
  }

  private ImageIcon createResizedIcon(String path, int width, int height) {
    ImageIcon icon = new ImageIcon(path);
    Image img = icon.getImage();
    Image resizedImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    return new ImageIcon(resizedImage);
  }
}
