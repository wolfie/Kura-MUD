package com.github.wolfie.kuramud.client.commands.combat;

import org.vaadin.console.Console;
import org.vaadin.console.Console.Command;

import com.github.wolfie.kuramud.client.CurrentPlayer;

public class Attack implements Command {

    /**
     * 
     */
    private static final long serialVersionUID = 4292250342764055479L;

    @Override
    public Object execute(Console console, String[] argv) throws Exception {
        CurrentPlayer.getPlayer().attack("");
        return null;
    }

    @Override
    public String getUsage(Console console, String[] argv) {
        return "Attack players or mobs in the same room";
    }

}
