package com.github.wolfie.kuramud.server;


public class PlayerCharacter {
  private String name = "Player " + System.currentTimeMillis();
  private Room currentRoom = Core.getStartRoom();

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public void setCurrentRoom(final Room currentRoom) {
    this.currentRoom = currentRoom;
  }

  public Room getCurrentRoom() {
    return currentRoom;
  }

  @Override
  public String toString() {
    return getName();
  }

  public void login() {
    getCurrentRoom().output(
        "The area temporarily fills with smoke. "
            + "Once it fades away, you notice that " + this
            + " has appeared nearby.");
  }

  public void logout() {
    getCurrentRoom().output(this + " vanishes in a puff of smoke!");
  }
}
