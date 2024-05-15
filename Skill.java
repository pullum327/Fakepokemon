public class Skill {
  private String name;
  private int power;
  private int remainingUses;
  private int maxUses;
  private String effect;
  private int effectDuration;
  private String specialEffect;
  private double specialEffectChance;

  public Skill(String name, int power, int maxUses) {
    this(name, power, maxUses, null, 0);
  }

  public Skill(String name, int power, int maxUses, String effect, int effectDuration) {
    this(name, power, maxUses, effect, effectDuration, null, 0.0);
  }

  public Skill(String name, int power, int maxUses, String effect, int effectDuration, String specialEffect, double specialEffectChance) {
    this.name = name;
    this.power = power;
    this.maxUses = maxUses;
    this.remainingUses = maxUses;
    this.effect = effect;
    this.effectDuration = effectDuration;
    this.specialEffect = specialEffect;
    this.specialEffectChance = specialEffectChance;
  }

  public String getName() {
    return name;
  }

  public int getPower() {
    return power;
  }

  public int getRemainingUses() {
    return remainingUses;
  }

  public int getMaxUses() {
    return maxUses;
  }

  public String getEffect() {
    return effect;
  }

  public int getEffectDuration() {
    return effectDuration;
  }

  public String getSpecialEffect() {
    return specialEffect;
  }

  public double getSpecialEffectChance() {
    return specialEffectChance;
  }

  public void use() {
    if (remainingUses > 0) {
      remainingUses--;
    }
  }

  public void reduceEffectDuration() {
    if (effectDuration > 0) {
      effectDuration--;
    }
  }
}
