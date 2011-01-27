package com.github.wolfie.kuramud.server;

public class PlayerCharacter extends Character {
  public PlayerCharacter() {
    super("Player " + System.currentTimeMillis());
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

  public void output(final String string) {
    Core.output(this, string);
  }
}
