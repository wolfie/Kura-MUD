package com.github.wolfie.kuramud.client.commands.movement;

import org.vaadin.console.Console;
import org.vaadin.console.Console.Command;

import com.github.wolfie.kuramud.client.CurrentPlayer;
import com.github.wolfie.kuramud.server.Core;
import com.github.wolfie.kuramud.server.Direction;

public class MoveSouth implements Command {

    /**
     * 
     */
    private static final long serialVersionUID = 1111106597884513916L;

    @Override
    public Object execute(Console console, String[] argv) throws Exception {
        Core.move(CurrentPlayer.getPlayer(), Direction.SOUTH);
        return null;
    }

    @Override
    public String getUsage(Console console, String[] argv) {
        return "Moves south";
    }

}
