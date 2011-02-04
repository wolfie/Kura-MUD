package com.github.wolfie.kuramud.client;

import org.vaadin.artur.icepush.ICEPush;
import org.vaadin.console.Console;

import com.github.wolfie.kuramud.Util;
import com.github.wolfie.kuramud.client.commands.admin.Reset;
import com.github.wolfie.kuramud.client.commands.combat.Attack;
import com.github.wolfie.kuramud.client.commands.environment.Look;
import com.github.wolfie.kuramud.client.commands.movement.MoveEast;
import com.github.wolfie.kuramud.client.commands.movement.MoveNorth;
import com.github.wolfie.kuramud.client.commands.movement.MoveSouth;
import com.github.wolfie.kuramud.client.commands.movement.MoveWest;
import com.github.wolfie.kuramud.server.Core;
import com.github.wolfie.kuramud.server.PlayerCharacter;
import com.github.wolfie.kuramud.server.Room;
import com.github.wolfie.kuramud.server.blackboard.OutputListener;
import com.vaadin.Application;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import com.vaadin.ui.themes.Reindeer;

@SuppressWarnings("serial")
public class KuramudApplication extends Application implements OutputListener {

    public class MyCloseListener implements CloseListener {
        private static final long serialVersionUID = -4512074063527224588L;

        @Override
        public void windowClose(final CloseEvent e) {
            close();
        }
    }

    private static final int TERMINAL_WIDTH = 70;

    // ignore findbugs: this application isn't meant to be serializable.
    private final ICEPush push = new ICEPush();

    private Console gameConsole = new Console();

    @Override
    public void init() {
        CurrentPlayer.initialize(this);
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
        Core.login(CurrentPlayer.getPlayer());
    }

    private void initCommands() {
        initMovementCommands();
        initCombatCommands();
        initEnvironmentCommands();
        initAdminCommans();
    }

    private void initMovementCommands() {
        MoveSouth south = new MoveSouth();
        gameConsole.addCommand("south", south);
        gameConsole.addCommand("s", south);

        MoveNorth north = new MoveNorth();
        gameConsole.addCommand("north", north);
        gameConsole.addCommand("n", north);

        MoveWest west = new MoveWest();
        gameConsole.addCommand("west", west);
        gameConsole.addCommand("w", west);

        MoveEast east = new MoveEast();
        gameConsole.addCommand("east", east);
        gameConsole.addCommand("e", east);
    }

    private void initCombatCommands() {
        Attack attack = new Attack();
        gameConsole.addCommand("attack", attack);
        gameConsole.addCommand("a", attack);
    }

    private void initEnvironmentCommands() {
        Look look = new Look();
        gameConsole.addCommand("look", look);
        gameConsole.addCommand("l", look);
    }

    private void initAdminCommans() {
        Reset reset = new Reset();
        gameConsole.addCommand("!reset", reset);
    }

    @Override
    public void close() {
        Core.logout(CurrentPlayer.getPlayer());
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

    private boolean isPlayerMsg(final Room inRoom,
            final PlayerCharacter toPlayer) {
        return inRoom == null && toPlayer == CurrentPlayer.getPlayer();
    }

    private boolean isRoomMsg(final Room inRoom, final PlayerCharacter toPlayer) {
        return inRoom != null && toPlayer == null
                && inRoom == CurrentPlayer.getPlayer().getCurrentRoom();
    }

    private boolean isGlobalMsg(final Room inRoom,
            final PlayerCharacter toPlayer) {
        return inRoom == null && toPlayer == null;
    }

    private void print(String message) {
        message = Util.outputWordWrap(message, TERMINAL_WIDTH);
        gameConsole.println(message);
        push.push();
    }
}
