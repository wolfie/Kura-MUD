package com.github.wolfie.kuramud.server;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.github.wolfie.blackboard.Blackboard;
import com.github.wolfie.kuramud.Util;
import com.github.wolfie.kuramud.server.areas.start.CombatRoom;
import com.github.wolfie.kuramud.server.areas.start.NullRoom.EasternRoom;
import com.github.wolfie.kuramud.server.areas.start.NullRoom.NorthernRoom;
import com.github.wolfie.kuramud.server.areas.start.NullRoom.SouthernRoom;
import com.github.wolfie.kuramud.server.areas.start.NullRoom.WesternRoom;
import com.github.wolfie.kuramud.server.areas.start.StartRoom;
import com.github.wolfie.kuramud.server.blackboard.CombatTickListener;
import com.github.wolfie.kuramud.server.blackboard.CombatTickListener.CombatTickEvent;
import com.github.wolfie.kuramud.server.blackboard.OutputListener;
import com.github.wolfie.kuramud.server.blackboard.OutputListener.OutputEvent;
import com.github.wolfie.kuramud.server.blackboard.PlayerLoginListener;
import com.github.wolfie.kuramud.server.blackboard.PlayerLoginListener.PlayerLoginEvent;
import com.github.wolfie.kuramud.server.blackboard.PlayerLogoutListener;
import com.github.wolfie.kuramud.server.blackboard.PlayerLogoutListener.PlayerLogoutEvent;
import com.github.wolfie.kuramud.server.blackboard.WorldResetListener;
import com.github.wolfie.kuramud.server.blackboard.WorldResetListener.WorldResetEvent;
import com.github.wolfie.kuramud.server.blackboard.WorldTickListener;
import com.github.wolfie.kuramud.server.blackboard.WorldTickListener.WorldTickEvent;
import com.google.common.collect.Sets;

public class Core {

  private static class Ticker extends Thread {
    private boolean running = false;

    private static final long COMBAT_TICK_MILLIS = 5 * 1000; // 5sec
    private static final long WORLD_TICK_MILLIS = 60 * 1000; // 1min
    private static final long WORLD_RESET_MILLIS = 60 * 60 * 1000; // 1h

    private long timeToWorldTick = WORLD_TICK_MILLIS;
    private long timeToWorldReset = WORLD_RESET_MILLIS;

    public Ticker() {
      setDaemon(true);
    }

    @Override
    public void run() {
      running = true;
      try {
        while (running && !isInterrupted()) {

          /*
           * OPTIMIZATION: all event object could be singleton objects, since
           * there's no need to recreate the dummy objects each and every time.
           */

          Thread.sleep(COMBAT_TICK_MILLIS);
          BLACKBOARD.fire(new CombatTickEvent());

          timeToWorldTick -= COMBAT_TICK_MILLIS;
          timeToWorldReset -= COMBAT_TICK_MILLIS;

          if (timeToWorldTick <= 0) {
            BLACKBOARD.fire(new WorldTickEvent());
            timeToWorldTick = WORLD_TICK_MILLIS;
          }

          if (timeToWorldReset <= 0) {
            BLACKBOARD.fire(new WorldResetEvent());
            timeToWorldReset = WORLD_RESET_MILLIS;
          }

        }
      } catch (final InterruptedException e) {
        cleanup();
        final boolean wasRunning = running;
        running = false;

        if (wasRunning) {
          e.printStackTrace();
          interrupt();
        }

        return;
      }
    }

    public void die() {
      running = false;
      interrupt();
    }
  }

  private static class RoomInstances {
    private final Map<Class<? extends Room>, Room> roomInstances = new HashMap<Class<? extends Room>, Room>();

    private void add(final Room room) {
      roomInstances.put(room.getClass(), room);
      BLACKBOARD.addListener(room);
    }

    private void clear() {
      for (final Room room : roomInstances.values()) {
        BLACKBOARD.removeListener(room);
      }
      roomInstances.clear();
    }

    public Room get(final Class<? extends Room> roomClass) {
      return roomInstances.get(roomClass);
    }
  }

  private static final RoomInstances ROOM_INSTANCES = new RoomInstances();

  private static final Blackboard BLACKBOARD = new Blackboard();

  private static final Class<? extends Room> START_ROOM = StartRoom.class;
  private static final Set<PlayerCharacter> PLAYERS_ONLINE = Collections
      .synchronizedSet(new HashSet<PlayerCharacter>());

  private static final Ticker TICKER = new Ticker();

  private static final Set<Combat> COMBATS = Sets.newHashSet();

  private Core() {
  }

  public static boolean isRunning() {
    return TICKER.isAlive();
  }

  private static void initRoomInstances() {
    ROOM_INSTANCES.clear();
    add(new StartRoom());
    add(new NorthernRoom());
    add(new SouthernRoom());
    add(new EasternRoom());
    add(new WesternRoom());
    add(new CombatRoom());
    add(new PurgatoryRoom());
  }

  private static void add(final Room room) {
    ROOM_INSTANCES.add(room);
    BLACKBOARD.addListener(room);
  }

  public static void resetAllRooms() {
    BLACKBOARD.fire(new WorldResetEvent());
  }

  public static Room getStartRoom() {
    return ROOM_INSTANCES.get(START_ROOM);
  }

  public static void output(final Room room, final String output) {
    if (room == null || output == null) {
      throw new IllegalArgumentException("Arguments may not be null");
    }
    BLACKBOARD.fire(new OutputListener.OutputEvent(room, output));
  }

  public static void output(final PlayerCharacter player, final String output) {
    BLACKBOARD.fire(new OutputListener.OutputEvent(player, output));
  }

  public static void outputGlobal(final String output) {
    if (output == null) {
      throw new IllegalArgumentException("output may not be null");
    }
    BLACKBOARD.fire(new OutputListener.OutputEvent(output));
  }

  public static void addMudOutput(final OutputListener outputListener) {
    BLACKBOARD.addListener(outputListener);
  }

  public static void removeMudOutput(final OutputListener outputListener) {
    BLACKBOARD.removeListener(outputListener);
  }

  public static void teleportCharacterTo(final Character character,
      final Class<? extends Room> roomClass) {
    final Room room = getRoomInstance(roomClass);
    character.getCurrentRoom().teleportAway(character);
    room.teleportInto(character);
  }

  public static void login(final PlayerCharacter playerCharacter) {
    PLAYERS_ONLINE.add(playerCharacter);
    final Room startRoomInstance = getRoomInstance(START_ROOM);
    playerCharacter.setCurrentRoom(startRoomInstance);
    startRoomInstance.teleportInto(playerCharacter);
    BLACKBOARD.fire(new PlayerLoginEvent(playerCharacter));
  }

  public static void logout(final PlayerCharacter playerCharacter) {
    if (PLAYERS_ONLINE.contains(playerCharacter)) {
      BLACKBOARD.fire(new PlayerLogoutEvent(playerCharacter));
      playerCharacter.getCurrentRoom().teleportAway(playerCharacter);
      PLAYERS_ONLINE.remove(playerCharacter);
    }
  }

  public static Room getRoomInstance(final Class<? extends Room> roomClass) {
    if (roomClass == null) {
      throw new IllegalArgumentException("non-null argument required");
    }

    final Room roomInstance = ROOM_INSTANCES.get(roomClass);
    if (roomInstance != null) {
      return roomInstance;
    } else {
      throw new IllegalStateException(roomClass + " has not been instantiated!");
    }
  }

  public static void cleanup() {
    for (final PlayerCharacter player : PLAYERS_ONLINE) {
      logout(player);
    }

    BLACKBOARD.clear();
    PLAYERS_ONLINE.clear();
    ROOM_INSTANCES.clear();
    TICKER.die();
  }

  public static void move(final PlayerCharacter player,
      final Direction direction) {
    final Room oldRoom = player.getCurrentRoom();

    final boolean directionIsAccessible = oldRoom.checkAccessibility(direction,
        player);
    final boolean mayLeave = oldRoom.checkMayLeave(direction, player);

    if (directionIsAccessible && mayLeave) {
      final Room newRoom = oldRoom.getRoomInDirection(direction);
      final boolean mayEnter = newRoom.checkMayEnter(direction, player);

      if (mayEnter) {
        oldRoom.remove(player);
        output(oldRoom, player + " leaves " + direction + ".");
        output(newRoom,
            player + " enters from " + Direction.oppositeDirection(direction)
                + ".");
        newRoom.add(player);
        output(player, "You walk " + direction + ".");
        player.look();
      }
    }
  }

  public static void bootstrap() {
    if (!TICKER.running) {
      // @formatter:off
      BLACKBOARD.register(OutputListener.class, OutputEvent.class);
      BLACKBOARD.register(PlayerLoginListener.class, PlayerLoginEvent.class);
      BLACKBOARD.register(PlayerLogoutListener.class, PlayerLogoutEvent.class);
      BLACKBOARD.register(WorldResetListener.class, WorldResetEvent.class);
      BLACKBOARD.register(WorldTickListener.class, WorldTickEvent.class);
      BLACKBOARD.register(CombatTickListener.class, CombatTickEvent.class);
      // @formatter:on

      initRoomInstances();
      resetAllRooms();

      TICKER.start();
    }
  }

  public static void look(final String argument, final Room currentRoom,
      final PlayerCharacter lookingPlayer) {
    if (argument == null || argument.isEmpty()) {
      lookingPlayer.look();
    } else if (Util.is(argument, "n", "north")) {
      currentRoom.look(Direction.NORTH, lookingPlayer);
    } else if (Util.is(argument, "s", "south")) {
      currentRoom.look(Direction.SOUTH, lookingPlayer);
    } else if (Util.is(argument, "e", "east")) {
      currentRoom.look(Direction.EAST, lookingPlayer);
    } else if (Util.is(argument, "w", "west")) {
      currentRoom.look(Direction.WEST, lookingPlayer);
    } else if (Util.is(argument, "u", "up")) {
      currentRoom.look(Direction.UP, lookingPlayer);
    } else if (Util.is(argument, "d", "down")) {
      currentRoom.look(Direction.DOWN, lookingPlayer);
    } else {
      currentRoom.look(argument, lookingPlayer);
    }
  }

  public static void add(final Combat combat) {
    COMBATS.add(combat);
    BLACKBOARD.addListener(combat);
  }

  public static void remove(final Combat combat) {
    COMBATS.remove(combat);
    BLACKBOARD.removeListener(combat);
  }
}
