package com.github.wolfie.kuramud.server;

public abstract class NonPlayerCharacter extends Character {
  private final String[] keywords;
  protected PlayerCharacter currentlyFighting = null;

  protected NonPlayerCharacter(final String name, final String... keywords) {
    super(name);

    if (keywords == null || keywords.length == 0) {
      throw new IllegalArgumentException(
          "You must give at least one keyword for the mob");
    }

    this.keywords = keywords;
  }

  /**
   * Guaranteed to contain at least one element.
   * 
   * @return
   */
  public String[] getKeywords() {
    return keywords;
  }

  @Override
  public final void output(final String string) {
    // NOOP, by design. Mobs don't care what is being said to them.
  }
}
