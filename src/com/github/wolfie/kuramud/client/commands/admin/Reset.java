package com.github.wolfie.kuramud.client.commands.admin;

import org.vaadin.console.Console;
import org.vaadin.console.Console.Command;

import com.github.wolfie.kuramud.server.Core;

public class Reset implements Command {

  /**
     * 
     */
  private static final long serialVersionUID = -7379441698011324040L;

  @Override
  public Object execute(final Console console, final String[] argv)
      throws Exception {
    Core.resetAllRooms();
    return null;
  }

  @Override
  public String getUsage(final Console console, final String[] argv) {
    return "Reset all rooms";
  }

}
