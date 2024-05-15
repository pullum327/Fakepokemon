public class Skill {
  private String name;
  private int power;
  private int remainingUses;
  private int maxUses;
  private String effect;
  private int effectDuration;
  private String specialEffect;
  private double specialEffectChance;
  private int burnDuration; // Duration for burn effect
  private int damageReduction; // Amount of damage reduction
  private int damageReductionDuration; // Duration for damage reduction effect

  public Skill(String name, int power, int maxUses) {
    this(name, power, maxUses, null, 0, null, 0.0, 0);
  }

  public Skill(String name, int power, int maxUses, String effect, int effectDuration) {
    this(name, power, maxUses, effect, effectDuration, null, 0.0, 0);
  }

  public Skill(String name, int power, int maxUses, String effect, int effectDuration, String specialEffect, double specialEffectChance) {
    this(name, power, maxUses, effect, effectDuration, specialEffect, specialEffectChance, 0);
  }

  public Skill(String name, int power, int maxUses, String effect, int effectDuration, String specialEffect, double specialEffectChance, int burnDuration) {
    this(name, power, maxUses, effect, effectDuration, specialEffect, specialEffectChance, burnDuration, 0, 0);
  }

  public Skill(String name, int power, int maxUses, String effect, int effectDuration, String specialEffect, double specialEffectChance, int burnDuration, int damageReduction, int damageReductionDuration) {
    this.name = name;
    this.power = power;
    this.maxUses = maxUses;
    this.remainingUses = maxUses;
    this.effect = effect;
    this.effectDuration = effectDuration;
    this.specialEffect = specialEffect;
    this.specialEffectChance = specialEffectChance;
    this.burnDuration = burnDuration;
    this.damageReduction = damageReduction;
    this.damageReductionDuration = damageReductionDuration;
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

  public int getBurnDuration() {
    return burnDuration;
  }

  public int getDamageReduction() {
    return damageReduction;
  }

  public int getDamageReductionDuration() {
    return damageReductionDuration;
  }

  public void use() {
    if (remainingUses > 0) {
      remainingUses--;
    }
  }
}
