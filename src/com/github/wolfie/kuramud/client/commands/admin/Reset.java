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
    public Object execute(Console console, String[] argv) throws Exception {
        Core.resetAllRooms();
        return null;
    }

    @Override
    public String getUsage(Console console, String[] argv) {
        return "Reset all rooms";
    }

}
