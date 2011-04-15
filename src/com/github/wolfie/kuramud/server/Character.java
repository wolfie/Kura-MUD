package com.github.wolfie.kuramud.server;

import com.github.wolfie.kuramud.server.item.CharacterInventory;
import com.github.wolfie.kuramud.server.item.Item;
import com.github.wolfie.kuramud.server.item.NoSuchItemException;
import com.github.wolfie.kuramud.server.item.OutOfCapacityException;

public abstract class Character implements Displayable, Targetable {
  private final String name;
  private final CharacterInventory inventory;

  private Room currentRoom;
  private int statAttack = 0;
  private int statDefense = 0;
  private int health = 10;

  public Character(final String name, final CharacterInventory inventory) {
    this.name = name;

    if (inventory != null) {
      this.inventory = inventory;
    } else {
      this.inventory = CharacterInventory.NULL;
    }
  }

  @Override
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

  public abstract void output(String string);

  public void decreaseHealth(final int amount) {
    health -= amount;
  }

  public boolean isAlive() {
    return health > 0;
  }

  public void receive(final Item item) throws OutOfCapacityException {
    inventory.put(item);
  }

  public void remove(final Item item) throws NoSuchItemException {
    inventory.remove(item);
  }

  public Item removeItem(final String target) throws NoSuchItemException {
    return inventory.removeItem(target);
  }

  public String getInventoryString() {
    return inventory.toString();
  }
}
