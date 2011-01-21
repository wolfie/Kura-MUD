package com.github.wolfie.kuramud.server.areas.start;

import com.github.wolfie.kuramud.server.Direction;
import com.github.wolfie.kuramud.server.Path;
import com.github.wolfie.kuramud.server.Room;

public abstract class NullRoom extends Room {

  public static class SouthernRoom extends NullRoom {
    public SouthernRoom() {
      super(new Path(Direction.NORTH, StartRoom.class));
    }
  }

  public static class NorthernRoom extends NullRoom {
    public NorthernRoom() {
      super(new Path(Direction.SOUTH, StartRoom.class));
    }
  }

  public static class EasternRoom extends NullRoom {
    public EasternRoom() {
      super(new Path(Direction.WEST, StartRoom.class));
    }
  }

  public static class WesternRoom extends NullRoom {
    public WesternRoom() {
      super(new Path(Direction.NORTH, StartRoom.class));
    }
  }

  private NullRoom(final Path path) {
    super(path);
  }

  @Override
  public String getLookDescription() {
    return "a " + getClass().getSimpleName();
  }

  @Override
  public String getLongDescription() {
    return "You're in a " + getClass().getSimpleName()
        + " that looks very bland and boring.";
  }

  public void reset(final ResetEvent event) {
    // NOOP
  }

  @Override
  public void tick(final TickEvent event) {
    // NOOP
  }

}
