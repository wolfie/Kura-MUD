package com.github.wolfie.kuramud.server;

public enum Direction {
  NORTH, EAST, SOUTH, WEST, UP, DOWN;

  @Override
  public String toString() {
    return name().toLowerCase();
  }

  public static Direction oppositeDirection(final Direction direction) {
    if (direction == NORTH) {
      return SOUTH;
    } else if (direction == EAST) {
      return WEST;
    } else if (direction == SOUTH) {
      return NORTH;
    } else if (direction == WEST) {
      return EAST;
    } else if (direction == UP) {
      return DOWN;
    } else if (direction == DOWN) {
      return UP;
    }

    return null;
  }
}
