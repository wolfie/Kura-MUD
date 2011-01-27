package com.github.wolfie.kuramud.client;

import org.vaadin.artur.icepush.ICEPush;

import com.github.wolfie.kuramud.Util;
import com.github.wolfie.kuramud.server.Core;
import com.github.wolfie.kuramud.server.Direction;
import com.github.wolfie.kuramud.server.PlayerCharacter;
import com.github.wolfie.kuramud.server.Room;
import com.github.wolfie.kuramud.server.blackboard.OutputListener;
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

@SuppressWarnings("serial")
public class KuramudApplication extends Application implements OutputListener {

  public class CommandListener implements ValueChangeListener {

    @Override
    public void valueChange(final ValueChangeEvent event) {
      final String command = (String) event.getProperty().getValue();
      ((TextField) event.getProperty()).focus();

      if (command == null || command.isEmpty()) {
        return;
      }

      final String trimmedCommand = command.trim();
      Core.output(player, "> " + trimmedCommand);

      final String[] split = trimmedCommand.split("\\s", 2);
      final String mainCommand = split[0];
      final String arguments;

      if (split.length > 1) {
        arguments = split[1];
      } else {
        arguments = "";
      }

      if (Util.is(mainCommand, "l", "look")) {
        player.look(arguments);
      } else if (Util.is(mainCommand, "n", "north")) {
        Core.move(player, Direction.NORTH);
      } else if (Util.is(mainCommand, "s", "south")) {
        Core.move(player, Direction.SOUTH);
      } else if (Util.is(mainCommand, "w", "west")) {
        Core.move(player, Direction.WEST);
      } else if (Util.is(mainCommand, "e", "east")) {
        Core.move(player, Direction.EAST);
      } else if (Util.is(mainCommand, "!reset")) {
        Core.resetAllRooms();
      } else if (Util.is(mainCommand, "say")) {
        player.getCurrentRoom().say(player, arguments);
      } else {
        Core.output(player, "Sorry, didn't understand you there...");
      }

      event.getProperty().setValue("");
    }
  }

  public class MyCloseListener implements CloseListener {
    private static final long serialVersionUID = -4512074063527224588L;

    @Override
    public void windowClose(final CloseEvent e) {
      close();
    }
  }

  private static final int TERMINAL_WIDTH = 70;

  // ignore findbugs: this application isn't meant to be serializable.
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
    canvas.addStyleName("canvas");
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

    if (isGlobalMsg(room, player) || isRoomMsg(room, player)
        || isPlayerMsg(room, player)) {
      print(output);
    }
  }

  private boolean isPlayerMsg(final Room inRoom, final PlayerCharacter toPlayer) {
    return inRoom == null && toPlayer == player;
  }

  private boolean isRoomMsg(final Room inRoom, final PlayerCharacter toPlayer) {
    return inRoom != null && toPlayer == null
        && inRoom == player.getCurrentRoom();
  }

  private boolean isGlobalMsg(final Room inRoom, final PlayerCharacter toPlayer) {
    return inRoom == null && toPlayer == null;
  }

  private void print(final String message) {
    final Label output = new Label(Util.outputWordWrap(message, TERMINAL_WIDTH));
    canvas.addComponent(output);
    getMainWindow().scrollIntoView(output);
    push.push();
  }
}
