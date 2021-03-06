package com.github.wolfie.kuramud.server.areas.start;

import com.github.wolfie.kuramud.server.Direction;
import com.github.wolfie.kuramud.server.Paths;
import com.github.wolfie.kuramud.server.Room;

public abstract class NullRoom extends Room {

  public static class SouthernRoom extends NullRoom {
    public SouthernRoom() {
      super(Direction.NORTH, StartRoom.class);
    }
  }

  public static class NorthernRoom extends NullRoom {
    public NorthernRoom() {
      super(Direction.SOUTH, StartRoom.class);
    }
  }

  public static class EasternRoom extends NullRoom {
    public EasternRoom() {
      super(Direction.WEST, StartRoom.class);
    }
  }

  public static class WesternRoom extends NullRoom {
    public WesternRoom() {
      super(new Paths.Builder().put(Direction.EAST, StartRoom.class)
          .put(Direction.WEST, CombatRoom.class).build());
    }
  }

  public NullRoom(final Direction north, final Class<? extends Room> roomClass) {
    super(north, roomClass);
  }

  public NullRoom(final Paths paths) {
    super(paths);
  }

  @Override
  public String getName() {
    return getClass().getSimpleName();
  }

  @Override
  public String getShortDescription() {
    return "a " + getClass().getSimpleName();
  }

  @Override
  public String getLongDescription() {
    return "You're in a " + getClass().getSimpleName()
        + " that looks very bland and boring.";
  }

  @Override
  protected void reset() {
    // NOOP
  }

  @Override
  public void worldTick(final WorldTickEvent event) {
    // NOOP
  }

}
