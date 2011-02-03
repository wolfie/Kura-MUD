package com.github.wolfie.kuramud.server;

import com.github.wolfie.kuramud.server.blackboard.CombatTickListener;

public class Combat implements CombatTickListener {

  private final Character attacker;
  private final Character defender;

  public Combat(final Character attacker, final Character defender) {
    if (attacker == null || defender == null) {
      throw new IllegalArgumentException("Neither attacker (" + attacker
          + ") nor defender (" + defender + ") may be null");
    }

    this.attacker = attacker;
    this.defender = defender;

    attacker.output("You lunge yourself onto " + defender);
    defender.output("You get attacked by " + attacker);
  }

  @Override
  public void combatTick(final CombatTickEvent event) {
    hit(attacker, defender);
    if (defender.isAlive()) {
      hit(defender, attacker);
    }

    checkForDead(attacker, defender);
    checkForDead(defender, attacker);
  }

  private static void hit(final Character attacker, final Character defender) {
    final int damageToDefender = attacker.getAttack() - defender.getDefense();
    if (damageToDefender > 0) {
      defender.decreaseHealth(damageToDefender);
      attacker.output("You hit " + defender + " with " + damageToDefender
          + " damage.");
      defender.output(attacker + " hits you with " + damageToDefender
          + " damage.");
    } else {
      attacker.output("You miss " + defender + "!");
      defender.output(attacker + " misses you!");
    }
  }

  private void checkForDead(final Character victim, final Character killer) {
    if (!victim.isAlive()) {
      victim.output("You died!");
      victim.getCurrentRoom().output(victim + " got killed by " + killer);
      killer.output("You killed " + victim);

      Core.teleportCharacterTo(victim, PurgatoryRoom.class);

      Core.remove(this);
    }
  }
}
