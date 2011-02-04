package com.github.wolfie.kuramud.client.commands.movement;

import org.vaadin.console.Console;
import org.vaadin.console.Console.Command;

import com.github.wolfie.kuramud.client.CurrentPlayer;
import com.github.wolfie.kuramud.server.Core;
import com.github.wolfie.kuramud.server.Direction;

public class MoveNorth implements Command {

    /**
     * 
     */
    private static final long serialVersionUID = 7863446197204210772L;

    @Override
    public Object execute(Console console, String[] argv) throws Exception {
        Core.move(CurrentPlayer.getPlayer(), Direction.NORTH);
        return null;
    }

    @Override
    public String getUsage(Console console, String[] argv) {
        return "Moves north";
    }

}
