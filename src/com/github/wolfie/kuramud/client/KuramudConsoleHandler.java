package com.github.wolfie.kuramud.client;

import org.vaadin.console.Console;
import org.vaadin.console.DefaultConsoleHandler;

public class KuramudConsoleHandler extends DefaultConsoleHandler {
	private static final long serialVersionUID = 3467189653188342319L;
	@Override
	public void commandNotFound(Console console, String[] argv) {
		console.println("Sorry, couldn't understand you. Try 'help' for a list of commands.");
	}
}
