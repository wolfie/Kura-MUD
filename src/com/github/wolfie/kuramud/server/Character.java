package com.github.wolfie.kuramud.server;

public abstract class Character {
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((currentRoom == null) ? 0 : currentRoom.hashCode());
    result = prime * result + health;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + statAttack;
    result = prime * result + statDefense;
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Character other = (Character) obj;
    if (currentRoom == null) {
      if (other.currentRoom != null) {
        return false;
      }
    } else if (!currentRoom.equals(other.currentRoom)) {
      return false;
    }
    if (health != other.health) {
      return false;
    }
    if (name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!name.equals(other.name)) {
      return false;
    }
    if (statAttack != other.statAttack) {
      return false;
    }
    if (statDefense != other.statDefense) {
      return false;
    }
    return true;
  }

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
