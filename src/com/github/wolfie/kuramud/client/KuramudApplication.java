package com.github.wolfie.kuramud.client;

import org.vaadin.artur.icepush.ICEPush;
import org.vaadin.console.Console;
import org.vaadin.console.Console.Command;

import com.github.wolfie.kuramud.Util;
import com.github.wolfie.kuramud.client.commands.admin.Reset;
import com.github.wolfie.kuramud.client.commands.chat.Say;
import com.github.wolfie.kuramud.client.commands.combat.Attack;
import com.github.wolfie.kuramud.client.commands.environment.Look;
import com.github.wolfie.kuramud.client.commands.inventory.Inventory;
import com.github.wolfie.kuramud.client.commands.movement.MoveEast;
import com.github.wolfie.kuramud.client.commands.movement.MoveNorth;
import com.github.wolfie.kuramud.client.commands.movement.MoveSouth;
import com.github.wolfie.kuramud.client.commands.movement.MoveWest;
import com.github.wolfie.kuramud.server.Core;
import com.github.wolfie.kuramud.server.PlayerCharacter;
import com.github.wolfie.kuramud.server.Room;
import com.github.wolfie.kuramud.server.blackboard.OutputListener;
import com.google.common.base.Joiner;
import com.google.common.collect.HashMultimap;
import com.vaadin.Application;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import com.vaadin.ui.themes.Reindeer;

@SuppressWarnings("serial")
public class KuramudApplication extends Application implements OutputListener {

  private final class Help implements Command {

    @Override
    public Object execute(final Console console, final String[] argv)
        throws Exception {
      if (argv.length < 2) {
        return getUsage(console, argv);
      }

      final String commandString = argv[1];
      final Command commandObject = gameConsole.getCommand(commandString);

      final String[] trimmedArgv = getRealArguments(argv);
      return commandObject.getUsage(console, trimmedArgv);
    }

    private String[] getRealArguments(final String[] argv) {
      final int twoLess = argv.length - 2;
      final String[] trimmedArgv = new String[twoLess];
      System.arraycopy(argv, 2, trimmedArgv, 0, twoLess);
      return trimmedArgv;
    }

    @Override
    public String getUsage(final Console console, final String[] argv) {

      final HashMultimap<Command, String> groupedCommands = HashMultimap
          .create();
      for (final String commandString : gameConsole.getCommands()) {
        final Command commandObject = gameConsole.getCommand(commandString);
        groupedCommands.put(commandObject, commandString);
      }

      final StringBuilder sb = new StringBuilder();
      sb.append("Available commands:\n");

      for (final Command commandObject : groupedCommands.keySet()) {
        final String joinedCommands = Joiner.on(", ").join(
            groupedCommands.get(commandObject));
        sb.append(joinedCommands + "\n");
      }
      return sb.toString();
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

  private final ICEPush push = new ICEPush();

  private final Console gameConsole = new Console();

  private final PlayerCharacter currentPlayer = new PlayerCharacter();

  @Override
  public void init() {
    gameConsole.addStyle("red", "red-highlight");
    gameConsole.addStyle("green", "green-highlight");
    gameConsole.addStyle("blue", "blue-highlight");

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

    gameConsole.setSizeFull();
    gameConsole.setPs("}> ");
    gameConsole.setCols(80);
    gameConsole.setMaxBufferSize(20);
    gameConsole.setGreeting("Welcome to Kura MUD");
    gameConsole.reset();
    gameConsole.focus();
    gameConsole.setHandler(new KuramudConsoleHandler());

    layout.addComponent(gameConsole);
    layout.setExpandRatio(gameConsole, 1);
    initCommands();

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
    Core.login(currentPlayer);
  }

  private void initCommands() {
    final Help help = new Help();
    gameConsole.addCommand("help", help);
    gameConsole.addCommand("h", help);

    initMovementCommands();
    initCombatCommands();
    initEnvironmentCommands();
    initAdminCommans();
    initChatCommands();
    initInventoryCommands();
  }

  private void initMovementCommands() {
    final MoveSouth south = new MoveSouth(currentPlayer);
    gameConsole.addCommand("south", south);
    gameConsole.addCommand("s", south);

    final MoveNorth north = new MoveNorth(currentPlayer);
    gameConsole.addCommand("north", north);
    gameConsole.addCommand("n", north);

    final MoveWest west = new MoveWest(currentPlayer);
    gameConsole.addCommand("west", west);
    gameConsole.addCommand("w", west);

    final MoveEast east = new MoveEast(currentPlayer);
    gameConsole.addCommand("east", east);
    gameConsole.addCommand("e", east);
  }

  private void initCombatCommands() {
    final Attack attack = new Attack(currentPlayer);
    gameConsole.addCommand("attack", attack);
    gameConsole.addCommand("a", attack);
  }

  private void initEnvironmentCommands() {
    final Look look = new Look(currentPlayer);
    gameConsole.addCommand("look", look);
    gameConsole.addCommand("l", look);
  }

  private void initAdminCommans() {
    final Reset reset = new Reset();
    gameConsole.addCommand("!reset", reset);
  }

  private void initChatCommands() {
    final Say say = new Say(currentPlayer);
    gameConsole.addCommand("say", say);
  }

  private void initInventoryCommands() {
    final Inventory inventory = new Inventory(currentPlayer);
    gameConsole.addCommand("i", inventory);
    gameConsole.addCommand("inv", inventory);
    gameConsole.addCommand("inventory", inventory);
  }

  @Override
  public void close() {
    Core.logout(currentPlayer);
    Core.removeMudOutput(this);
    super.close();
  }

  @Override
  public void output(final OutputEvent event) {
    final String output = event.getOutput();

    if (event.getOutputType().equals(OutputType.GLOBAL)) {
      print(output);
      return;
    }

    final Room room = event.getRoom();
    if (event.getOutputType().equals(OutputType.ROOM)
        && room.equals(currentPlayer.getCurrentRoom())) {
      print(output);
      return;
    }

    final PlayerCharacter player = event.getPlayer();
    if (event.getOutputType().equals(OutputType.PLAYER)
        && player.equals(currentPlayer)) {
      print(output);
      return;
    }
  }

  private void print(String message) {
    message = Util.outputWordWrap(message, TERMINAL_WIDTH);
    gameConsole.println(message);
    push.push();
  }
}
