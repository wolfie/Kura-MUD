package com.github.wolfie.kuramud.server;

import java.util.HashSet;
import java.util.Set;

import com.github.wolfie.blackboard.Blackboard;
import com.github.wolfie.kuramud.server.areas.start.NullRoom.EasternRoom;
import com.github.wolfie.kuramud.server.areas.start.NullRoom.NorthernRoom;
import com.github.wolfie.kuramud.server.areas.start.NullRoom.SouthernRoom;
import com.github.wolfie.kuramud.server.areas.start.NullRoom.WesternRoom;
import com.github.wolfie.kuramud.server.areas.start.StartRoom;
import com.github.wolfie.kuramud.server.blackboard.ResetListener.ResetEvent;

public class Core extends Thread {
  private static final Set<Room> ROOM_INSTANCES = new HashSet<Room>();

  public static final Blackboard BLACKBOARD = new Blackboard();

  private static final Core SINGLETON = new Core();

  private static final StartRoom START_ROOM = new StartRoom();

  private static final Set<PlayerCharacter> PLAYERS_ONLINE = new HashSet<PlayerCharacter>();

  public static void bootstrap() {
    SINGLETON.start();
  }

  private Core() {
  }

  @Override
  public void run() {
    initRoomInstances();
    resetAllRooms();
  }

  private static void initRoomInstances() {
    clearRoomInstances();
    ROOM_INSTANCES.add(START_ROOM);
    ROOM_INSTANCES.add(new NorthernRoom());
    ROOM_INSTANCES.add(new SouthernRoom());
    ROOM_INSTANCES.add(new EasternRoom());
    ROOM_INSTANCES.add(new WesternRoom());
  }

  private static void clearRoomInstances() {
    for (final Room room : ROOM_INSTANCES) {
      BLACKBOARD.removeListener(room);
    }
  }

  private static void resetAllRooms() {
    BLACKBOARD.fire(new ResetEvent());
  }

  public static Room getStartRoom() {
    return START_ROOM;
  }

  public static void output(final Room currentRoom, final String string) {
    currentRoom.output(string);
  }

  public static void add(final PlayerCharacter player) {
    PLAYERS_ONLINE.add(player);
  }

  public static void remove(final PlayerCharacter player) {
    PLAYERS_ONLINE.remove(player);
  }
}
