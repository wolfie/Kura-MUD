package com.github.wolfie.kuramud.client;

import org.vaadin.artur.icepush.ICEPush;

import com.github.wolfie.kuramud.server.Core;
import com.github.wolfie.kuramud.server.PlayerCharacter;
import com.github.wolfie.kuramud.server.Room;
import com.github.wolfie.kuramud.server.blackboard.OutputListener;
import com.github.wolfie.kuramud.server.blackboard.ResetListener;
import com.vaadin.Application;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import com.vaadin.ui.themes.Reindeer;

public class KuramudApplication extends Application implements OutputListener {
	
	public class CommandListener implements ValueChangeListener {
		private static final long serialVersionUID = 6345624124147443926L;
		
		@Override
		public void valueChange(final ValueChangeEvent event) {
			final String command = (String) event.getProperty().getValue();
			((TextField) event.getProperty()).focus();
			
			if (command == null || command.isEmpty()) {
				return;
			}
			
			Core.output(player, command);
			
			if (is(command, "l", "look")) {
				player.lookAt(player.getCurrentRoom());
			} else if (is(command, "!reset")) {
				Core.BLACKBOARD.fire(new ResetListener.ResetEvent());
			} else {
				Core.output(player, "Didn't understand you there...");
			}
			
			event.getProperty().setValue("");
		}
		
		public boolean is(final String command, final String... matches) {
			if (matches == null || matches.length == 0) {
				return false;
			} else {
				for (final String match : matches) {
					if (match.equalsIgnoreCase(command)) {
						return true;
					}
				}
				return false;
			}
		}
	}
	
	public class MyCloseListener implements CloseListener {
		private static final long serialVersionUID = -4512074063527224588L;
		
		@Override
		public void windowClose(final CloseEvent e) {
			close();
		}
	}
	
	private static final long serialVersionUID = 320988979001844194L;
	
	private final PlayerCharacter player = new PlayerCharacter();
	private final ICEPush push = new ICEPush();
	
	private final Panel canvas = new Panel(new CssLayout());
	
	@Override
	public void init() {
		final VerticalLayout layout = new VerticalLayout();
		layout.setStyleName(Reindeer.LAYOUT_BLACK);
		final Window mainWindow = new Window("Kura MUD", layout);
		mainWindow.setScrollable(true);
		mainWindow.setSizeFull();
		mainWindow.getContent().setSizeFull();
		mainWindow.setStyleName(Reindeer.WINDOW_BLACK);
		setTheme("mud");
		
		setMainWindow(mainWindow);
		mainWindow.addListener(new MyCloseListener());
		
		layout.addComponent(push);
		
		final Label heading = new Label("Kura MUD!");
		heading.setStyleName(Reindeer.LABEL_H1);
		layout.addComponent(heading);
		
		canvas.setStyleName(Reindeer.LAYOUT_BLACK);
		canvas.setSizeFull();
		canvas.getContent().setSizeUndefined();
		canvas.getContent().setStyleName(Reindeer.LAYOUT_BLACK);
		canvas.getContent().setWidth("100%");
		layout.addComponent(canvas);
		layout.setExpandRatio(canvas, 1);
		
		final TextField textField = new TextField();
		textField.setWidth("100%");
		textField.focus();
		textField.setImmediate(true);
		textField.addListener(new CommandListener());
		layout.addComponent(textField);
		
		while (!Core.isRunning()) {
			try {
				Thread.sleep(1000);
			} catch (final InterruptedException e) {
				e.printStackTrace();
				mainWindow.addComponent(new Label(e.toString()));
				return;
			}
		}
		
		Core.addMudOutput(this);
		Core.login(player);
	}
	
	@Override
	public void close() {
		Core.logout(player);
		Core.removeMudOutput(this);
		super.close();
	}
	
	@Override
	public void output(final OutputEvent event) {
		
		final Room room = event.getRoom();
		final PlayerCharacter player = event.getPlayer();
		final String output = event.getOutput();
		
		if (room == null && player == null) {
			print("[SCOPE:GLOBAL] " + output);
		} else if (player == null && room == this.player.getCurrentRoom()) {
			print("[SCOPE:ROOM] " + output);
		} else if (room == null && player == this.player) {
			print("[SCOPE:PLAYER] " + output);
		}
	}
	
	private void print(final String message) {
		final Label output = new Label(message);
		output.setStyleName("prompt");
		canvas.addComponent(output);
		getMainWindow().scrollIntoView(output);
		push.push();
	}
}
