package com.github.wolfie.kuramud.server;

public class PlayerCharacter extends Character {
  public PlayerCharacter() {
    super("Player " + System.currentTimeMillis());
    setAttack(2);
  }

  public void lookAt(final Character character) {
    Core.output(this, character.getLongDescription());
  }

  @Override
  public String getLongDescription() {
    return "Looks very much like a hero";
  }

  public void look() {
    Core.output(this, getCurrentRoom().getRoomString(this));
  }

  public void look(final String arguments) {
    Core.look(arguments, getCurrentRoom(), this);
  }

  @Override
  public void output(final String string) {
    Core.output(this, string);
  }

  public void attack(final String arguments) {
    final NonPlayerCharacter mob = getCurrentRoom().getMob(arguments);
    if (mob != null) {
      Core.add(new Combat(this, mob));
    } else {
      output("Nothing to kill here by that name.");
    }
  }

  @Override
  public String toString() {
    return getShortDescription();
  }
}
