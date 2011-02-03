package com.github.wolfie.kuramud.server.areas.start;

import com.github.wolfie.kuramud.server.Direction;
import com.github.wolfie.kuramud.server.Paths;
import com.github.wolfie.kuramud.server.Room;
import com.github.wolfie.kuramud.server.areas.start.NullRoom.EasternRoom;
import com.github.wolfie.kuramud.server.areas.start.NullRoom.NorthernRoom;
import com.github.wolfie.kuramud.server.areas.start.NullRoom.SouthernRoom;
import com.github.wolfie.kuramud.server.areas.start.NullRoom.WesternRoom;

public class StartRoom extends Room {

  public StartRoom() {
    super(new Paths.Builder()
        .put(Direction.NORTH, NorthernRoom.class)
        .put(Direction.SOUTH, SouthernRoom.class)
        .put(Direction.EAST, EasternRoom.class)
        .put(Direction.WEST, WesternRoom.class)
        .build());
  }

  @Override
  public void worldReset(final WorldResetEvent event) {
    mobInRoom(1, TutorMob.class);
    reset();
  }

  @Override
  public void worldTick(final WorldTickEvent event) {
    // noop
  }

  @Override
  public String getShortDescription() {
    return "a room that looks as a very nice place to start.";
  }

  @Override
  public String getLongDescription() {
    return "You are in a room that looks very nice and calm. It's very safe-looking, and doesn't really seem threatening at all.";
  }

  @Override
  public String getName() {
    return "Start Room";
  }
}
