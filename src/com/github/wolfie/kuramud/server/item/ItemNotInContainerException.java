package com.github.wolfie.kuramud.server.item;

public class ItemNotInContainerException extends Exception {
  private static final long serialVersionUID = 4652348948509820901L;

  public ItemNotInContainerException(final ItemContainer itemContainer,
      final Item item) {
    super("Item " + item + " was not found in " + itemContainer);
  }
}
