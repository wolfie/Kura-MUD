package com.github.wolfie.kuramud.server;

public class Path {
  private final Direction direction;
  private final Class<? extends Room> room;

  public Path(final Direction direction, final Class<? extends Room> room) {
    this.direction = direction;
    this.room = room;
  }

  public Direction getDirection() {
    return direction;
  }

  public Class<? extends Room> getRoom() {
    return room;
  }
}
