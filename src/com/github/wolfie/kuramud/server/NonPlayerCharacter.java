package com.github.wolfie.kuramud.server;

import com.github.wolfie.kuramud.server.item.CharacterInventory;

public abstract class NonPlayerCharacter extends Character {
  protected PlayerCharacter currentlyFighting = null;

  protected NonPlayerCharacter(final String name,
      final CharacterInventory inventory) {
    super(name, inventory);
  }

  @Override
  public final void output(final String string) {
    // Mobs don't care what is being said to them.
  }
}
