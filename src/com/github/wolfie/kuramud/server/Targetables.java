package com.github.wolfie.kuramud.server;

import com.github.wolfie.kuramud.server.item.Item;

public final class Targetables {
  private Targetables() {
    // uninstantiable
  }

  /**
   * Check whether the given <code>argument</code> is a keyword for the given
   * {@link Item}
   */
  public static boolean matches(final String argument, final Item item) {
    for (final String keyword : item.getKeywords()) {
      if (argument.equals(keyword)) {
        return true;
      }
    }
    return false;
  }
}
