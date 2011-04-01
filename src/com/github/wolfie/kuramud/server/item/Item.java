package com.github.wolfie.kuramud.server.item;

import com.github.wolfie.kuramud.server.Displayable;
import com.github.wolfie.kuramud.server.Targetable;

public abstract class Item implements Displayable, Targetable {

  private final int requiredCapacity;

  protected Item(final int requiredCapacity) {
    this.requiredCapacity = requiredCapacity;
  }

  public int getRequiredCapacity() {
    return requiredCapacity;
  }
}
