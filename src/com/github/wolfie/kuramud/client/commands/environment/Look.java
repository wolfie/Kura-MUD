package com.github.wolfie.kuramud.client.commands.environment;

import org.vaadin.console.Console;
import org.vaadin.console.Console.Command;

import com.github.wolfie.kuramud.client.CurrentPlayer;

public class Look implements Command {

  private static final long serialVersionUID = 8304163166271979004L;

  @Override
  public Object execute(final Console console, final String[] argv)
      throws Exception {
    CurrentPlayer.getPlayer().look();
    return null;
  }

  @Override
  public String getUsage(final Console console, final String[] argv) {
    return "Look around to see what fun there is to do";
  }

}
