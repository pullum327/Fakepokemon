public class Battle {
  private Pokemon player;
  private Pokemon opponent;
  private GameGUI gameGUI;

  public Battle(Pokemon player, Pokemon opponent, GameGUI gameGUI) {
    this.player = player;
    this.opponent = opponent;
    this.gameGUI = gameGUI;
  }

  public void playerTurn(int skillIndex) {
    Skill skill = player.getSkills().get(skillIndex);
    if (skill.getRemainingUses() > 0) {
      skill.use();
      if (skill.getEffect() != null) {
        player.applyEffect(skill);
        gameGUI.appendOutput(player.getName() + " 使用了 " + skill.getName() + "!");
      } else {
        int damage = calculateDamage(skill, player);
        opponent.takeDamage(damage);
        gameGUI.appendOutput(player.getName() + " 使用了 " + skill.getName() + " 造成了 " + damage + " 傷害!");
        checkAndApplySpecialEffect(skill, opponent);
      }
      gameGUI.updateHp();
    } else {
      gameGUI.appendOutput("No remaining uses for " + skill.getName() + "!");
    }
    player.updateEffects(); // Update player's effects
  }

  public void opponentTurn() {
    if (opponent.isParalyzed()) {
      gameGUI.appendOutput(opponent.getName() + " 因 " + "麻痺" + " 無法行動!");
      opponent.setParalyzed(false); // Clear paralysis after skipping turn
      return;
    }

    int randomSkillIndex = (int) (Math.random() * opponent.getSkills().size());
    Skill skill = opponent.getSkills().get(randomSkillIndex);
    if (skill.getRemainingUses() > 0) {
      skill.use();
      if (skill.getEffect() != null) {
        opponent.applyEffect(skill);
        gameGUI.appendOutput(opponent.getName() + " 使用了 " + skill.getName() + "!");
      } else {
        int damage = calculateDamage(skill, opponent);
        player.takeDamage(damage);
        gameGUI.appendOutput(opponent.getName() + " 使用了 " + skill.getName() + " 造成了 " + damage + " 傷害!");
        checkAndApplySpecialEffect(skill, player);
      }
      gameGUI.updateHp();
    } else {
      opponentTurn(); // Retry if no remaining uses
    }
    opponent.updateEffects(); // Update opponent's effects
  }

  private int calculateDamage(Skill skill, Pokemon attacker) {
    int damage = skill.getPower();
    if (attacker.getBoostDuration() > 0) {
      damage += 5;
    }
    return damage;
  }

  private void checkAndApplySpecialEffect(Skill skill, Pokemon target) {
    if (skill.getSpecialEffect() != null && Math.random() < skill.getSpecialEffectChance()) {
      if (skill.getSpecialEffect().equals("Paralyze")) {
        target.setParalyzed(true);
        gameGUI.appendOutput(target.getName() + " 因 " + skill.getName() + " 受到麻痺 無法行動!");
      }
    }
  }
}
