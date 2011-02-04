package com.github.wolfie.kuramud.client.commands.movement;

import org.vaadin.console.Console;
import org.vaadin.console.Console.Command;

import com.github.wolfie.kuramud.client.CurrentPlayer;
import com.github.wolfie.kuramud.server.Core;
import com.github.wolfie.kuramud.server.Direction;

public class MoveWest implements Command {

    /**
     * 
     */
    private static final long serialVersionUID = 1579827704174191819L;

    @Override
    public Object execute(Console console, String[] argv) throws Exception {
        Core.move(CurrentPlayer.getPlayer(), Direction.WEST);
        return null;
    }

    @Override
    public String getUsage(Console console, String[] argv) {
        return "Move west";
    }

}
