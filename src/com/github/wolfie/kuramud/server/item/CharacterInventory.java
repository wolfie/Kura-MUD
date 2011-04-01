package com.github.wolfie.kuramud.server.item;


public class CharacterInventory extends ItemContainer {

  public static final CharacterInventory NULL = newWithCapacity(0);

  private CharacterInventory(final int totalCapacity) {
    super(totalCapacity);
  }

  public static CharacterInventory newWithCapacity(final int totalCapacity) {
    return new CharacterInventory(totalCapacity);
  }
}
