package com.github.wolfie.kuramud.server.areas.start;

import java.util.Set;

import com.github.wolfie.kuramud.server.Direction;
import com.github.wolfie.kuramud.server.Path;
import com.github.wolfie.kuramud.server.Room;
import com.github.wolfie.kuramud.server.areas.start.NullRoom.EasternRoom;
import com.github.wolfie.kuramud.server.areas.start.NullRoom.NorthernRoom;
import com.github.wolfie.kuramud.server.areas.start.NullRoom.SouthernRoom;
import com.github.wolfie.kuramud.server.areas.start.NullRoom.WesternRoom;
import com.google.gwt.dev.util.collect.Sets;

public class StartRoom extends Room {

  private final static Set<Path> PATHS;

  static {
    final Path north = new Path(Direction.NORTH, NorthernRoom.class);
    final Path south = new Path(Direction.SOUTH, SouthernRoom.class);
    final Path east = new Path(Direction.EAST, EasternRoom.class);
    final Path west = new Path(Direction.WEST, WesternRoom.class);
    PATHS = Sets.create(north, south, east, west);
  }

  public StartRoom() {
    super(PATHS);
  }

  public void reset(final ResetEvent event) {
    // TODO: helper NPC
  }

  public void tick(final TickEvent event) {
    // TODO: broadcast "you hear a tick"
  }

  @Override
  public String getLookDescription() {
    return "a room that looks as a very nice starting place.";
  }

  @Override
  public String getLongDescription() {
    return "You are in a room that looks very nice and calm. It's very safe-looking, and doesn't really seem threatening at all.";
  }
}
