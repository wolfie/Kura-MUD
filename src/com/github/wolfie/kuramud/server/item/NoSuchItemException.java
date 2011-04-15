package com.github.wolfie.kuramud.server.item;

import com.github.wolfie.kuramud.server.Displayable;

public class NoSuchItemException extends Exception {
  private static final long serialVersionUID = 4652348948509820901L;
  private final String message;

  public NoSuchItemException(final ItemContainer itemContainer, final Item item) {
    if (item != null) {
      if (itemContainer instanceof Displayable) {
        final Displayable ic2 = (Displayable) itemContainer;
        message = item.getShortDescription() + " was not found in "
            + ic2.getShortDescription();
      } else {
        message = item.getShortDescription() + " was not found";
      }
    } else {
      throw new IllegalArgumentException("item was null");
    }
  }

  public NoSuchItemException(final ItemContainer itemContainer,
      final String target) {
    if (itemContainer instanceof Displayable) {
      final Displayable ic2 = (Displayable) itemContainer;
      message = target + " was not found in " + ic2.getShortDescription();
    } else {
      message = target + " was not found";
    }
  }

  @Override
  public String getMessage() {
    return message;
  }
}
