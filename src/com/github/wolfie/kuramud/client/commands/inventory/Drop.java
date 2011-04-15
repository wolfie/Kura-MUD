package com.github.wolfie.kuramud.client.commands.inventory;

import org.vaadin.console.Console;
import org.vaadin.console.Console.Command;

import com.github.wolfie.kuramud.server.Core;
import com.github.wolfie.kuramud.server.PlayerCharacter;
import com.github.wolfie.kuramud.server.item.NoSuchItemException;
import com.github.wolfie.kuramud.server.item.OutOfCapacityException;

public class Drop implements Command {
  private static final long serialVersionUID = -3764370433190552723L;

  private final PlayerCharacter currentPlayer;

  public Drop(final PlayerCharacter currentPlayer) {
    this.currentPlayer = currentPlayer;
  }

  @Override
  public Object execute(final Console console, final String[] argv) {

    if (argv.length > 1) {
      try {
        final String target = argv[1];
        Core.drop(currentPlayer, target);
      } catch (final OutOfCapacityException e) {
        currentPlayer.output(e.getMessage());
      } catch (final NoSuchItemException e) {
        currentPlayer.output(e.getMessage());
      }
    } else {
      currentPlayer.output("Drop what?");
    }
    return null;
  }

  @Override
  public String getUsage(final Console console, final String[] argv) {
    return "Drop something.";
  }
}
