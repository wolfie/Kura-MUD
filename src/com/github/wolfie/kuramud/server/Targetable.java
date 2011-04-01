package com.github.wolfie.kuramud.server;

public interface Targetable {
  /**
   * Get the keywords that can be used to target the object
   * 
   * @return A non-empty array of non-null keyword Strings.
   */
  String[] getKeywords();
}
