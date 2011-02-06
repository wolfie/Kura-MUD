package com.github.wolfie.kuramud.client.commands.movement;

import org.vaadin.console.Console;
import org.vaadin.console.Console.Command;

import com.github.wolfie.kuramud.client.CurrentPlayer;
import com.github.wolfie.kuramud.server.Core;
import com.github.wolfie.kuramud.server.Direction;

public class MoveNorth implements Command {

  private static final long serialVersionUID = 7863446197204210772L;

  @Override
  public Object execute(final Console console, final String[] argv)
      throws Exception {
    Core.move(CurrentPlayer.getPlayer(), Direction.NORTH);
    return null;
  }

  @Override
  public String getUsage(final Console console, final String[] argv) {
    return "Moves north";
  }

}
