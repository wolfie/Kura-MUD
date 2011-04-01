package com.github.wolfie.kuramud.client.commands.inventory;

import org.vaadin.console.Console;
import org.vaadin.console.Console.Command;

import com.github.wolfie.kuramud.server.PlayerCharacter;

public class Inventory implements Command {
  private static final long serialVersionUID = -8879861702706097874L;
  private final PlayerCharacter player;

  public Inventory(final PlayerCharacter player) {
    this.player = player;
  }

  @Override
  public Object execute(final Console console, final String[] argv)
      throws Exception {
    player.output("Can't see your inventory just yet");
    return null;
  }

  @Override
  public String getUsage(final Console console, final String[] argv) {
    return "Look into your inventory";
  }
}
