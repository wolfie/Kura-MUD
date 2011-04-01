package com.github.wolfie.kuramud.server.item;

public class OutOfCapacityException extends Exception {

  private static final long serialVersionUID = 8112748162813096461L;

  public OutOfCapacityException(final ItemContainer container, final Item item) {
    super("The container " + container + " ("
        + container.getAvailableCapacity() + " out of "
        + container.getTotalCapacity()
        + " available) did not have enough space for " + item + " (requires "
        + item.getRequiredCapacity() + ")");
  }
}
