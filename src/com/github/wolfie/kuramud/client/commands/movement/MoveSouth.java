package com.github.wolfie.kuramud.client.commands.movement;

import org.vaadin.console.Console;
import org.vaadin.console.Console.Command;

import com.github.wolfie.kuramud.client.CurrentPlayer;
import com.github.wolfie.kuramud.server.Core;
import com.github.wolfie.kuramud.server.Direction;

public class MoveSouth implements Command {

  private static final long serialVersionUID = 1111106597884513916L;

  @Override
  public Object execute(final Console console, final String[] argv)
      throws Exception {
    Core.move(CurrentPlayer.getPlayer(), Direction.SOUTH);
    return null;
  }

  @Override
  public String getUsage(final Console console, final String[] argv) {
    return "Moves south";
  }

}
