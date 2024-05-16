public class Battle {
  private Pokemon player;
  private Pokemon opponent;
  private GameGUI gameGUI;
  private MusicPlayer skillSoundPlayer; // 添加音效播放器成員變量

  public Battle(Pokemon player, Pokemon opponent, GameGUI gameGUI) {
    this.player = player;
    this.opponent = opponent;
    this.gameGUI = gameGUI;
    this.player.setGameGUI(gameGUI);
    this.opponent.setGameGUI(gameGUI);
    this.skillSoundPlayer = new MusicPlayer(); // 初始化音效播放器
  }

  public void playerTurn(int skillIndex) {
    if (player.isParalyzed()) {
      gameGUI.appendOutput(player.getName() + " 因 " + "麻痺" + " 無法行動!");
      player.setParalyzed(false);
      return;
    }
    Skill skill = player.getSkills().get(skillIndex);
    if(skill.getRemainingUses()<=1)
    {
      gameGUI.updateSkillButtons();
    }
    if (skill.getRemainingUses() > 0) {
      skill.use();
      // 播放音效
      if (skill.getName().equals("電擊")) {
        skillSoundPlayer.skillMusic("src/resources/skill1.wav", -5.0f); // 替换为你的音效文件路径
      }
      if (skill.getEffect() != null) {
        gameGUI.appendOutput(player.getName() + " 使用了 " + skill.getName() + "!");
        player.applyEffect(skill);

      } else {
        int damage = calculateDamage(skill, player);
        if (player.getBoostDuration() > 0) {
          gameGUI.appendOutput(player.getName() + " 因 " + "咆哮效果增加了 5 點傷害!");
        }
        gameGUI.appendOutput(player.getName() + " 使用了 " + skill.getName());
        opponent.takeDamage(damage);
        checkAndApplySpecialEffect(skill, opponent);
      }
      gameGUI.updateHp();
    } else{
      gameGUI.appendOutput(player.getName() + " 没有剩余的 " + skill.getName() + " 使用次数了!");
    }
    player.updateEffects();
    opponent.updateEffects();
  }

  public void opponentTurn() {
    if (opponent.isParalyzed()) {
      gameGUI.appendOutput(opponent.getName() + " 因 " + "麻痺" + " 無法行動!");
      opponent.setParalyzed(false);
      return;
    }

    int randomSkillIndex = (int) (Math.random() * opponent.getSkills().size());
    Skill skill = opponent.getSkills().get(randomSkillIndex);
    if (skill.getRemainingUses() > 0) {
      skill.use();
      // 播放音效
      if (skill.getName().equals("電擊")) {
        skillSoundPlayer.playMusic("src/resources/skill1.wav", -10.0f); // 替换为你的音效文件路径
      }
      if (skill.getEffect() != null) {
        gameGUI.appendOutput(opponent.getName() + " 使用了 " + skill.getName() + "!");
        opponent.applyEffect(skill);

      } else {
        int damage = calculateDamage(skill, opponent);
        if (opponent.getBoostDuration() > 0) {
          gameGUI.appendOutput(opponent.getName() + " 因 " + "咆哮效果增加了 5 點傷害!");
        }
        gameGUI.appendOutput(opponent.getName() + " 使用了 " + skill.getName());
        player.takeDamage(damage);
        checkAndApplySpecialEffect(skill, player);
      }
      gameGUI.updateHp();
    } else {
      opponentTurn();
    }
    player.updateEffects();
    opponent.updateEffects();
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
      } else if (skill.getSpecialEffect().equals("Burn")) {
        target.setBurnDuration(skill.getBurnDuration());
        gameGUI.appendOutput(target.getName() + " 因 " + skill.getName() + "受到燃燒效果 接下來" + skill.getBurnDuration() + "回合每回合-5血!");
      }
    }
  }

}
