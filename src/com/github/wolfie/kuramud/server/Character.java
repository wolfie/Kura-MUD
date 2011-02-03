package com.github.wolfie.kuramud.server;

public abstract class Character {
  private Room currentRoom;
  private final String name;
  private int statAttack = 0;
  private int statDefense = 0;
  private int health = 10;

  public Character(final String name) {
    this.name = name;
  }

  public String getShortDescription() {
    return name;
  }

  public void setCurrentRoom(final Room currentRoom) {
    this.currentRoom = currentRoom;
  }

  public Room getCurrentRoom() {
    return currentRoom;
  }

  @Override
  public String toString() {
    return getShortDescription();
  }

  public int getAttack() {
    return statAttack;
  }

  public int getDefense() {
    return statDefense;
  }

  public int getHealth() {
    return health;
  }

  protected void setAttack(final int attack) {
    statAttack = attack;
  }

  protected void setDefense(final int defense) {
    statDefense = defense;
  }

  protected void setHealth(final int health) {
    this.health = health;
  }

  public abstract String getLongDescription();

  public abstract void output(String string);

  public void decreaseHealth(final int amount) {
    health -= amount;
  }

  public boolean isAlive() {
    return health > 0;
  }
}
