package com.github.wolfie.kuramud.client.commands.chat;

import org.vaadin.console.Console;
import org.vaadin.console.Console.Command;

import com.github.wolfie.kuramud.client.CurrentPlayer;

public class Say implements Command {

    /**
     * 
     */
    private static final long serialVersionUID = -59431370586086904L;

    @Override
    public Object execute(Console console, String[] argv) throws Exception {
        String message = "";
        if (argv != null) {
            for (String word : argv) {
                message += word + " ";
            }
        }
        CurrentPlayer.getPlayer().getCurrentRoom().say(
                CurrentPlayer.getPlayer(), message);
        return null;
    }

    @Override
    public String getUsage(Console console, String[] argv) {
        return "Use 'say' to speak in the chat";
    }

}
