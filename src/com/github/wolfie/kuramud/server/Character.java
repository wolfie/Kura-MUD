package com.github.wolfie.kuramud.server;

public abstract class Character {
  private Room currentRoom;
  private final String name;

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

  public abstract String getLongDescription();
}
