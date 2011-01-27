package com.github.wolfie.kuramud.server;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;

public class Paths {
  public static class Builder {
    private Paths paths = new Paths();

    public Builder put(final Direction direction,
        final Class<? extends Room> room) {
      paths.paths.put(direction, room);
      return this;
    }

    public Paths build() {
      final Paths value = paths;
      paths = new Paths();
      return value;
    }
  }

  public static final Ordering<Direction> DIRECTION_ORDER = new Ordering<Direction>() {
    @Override
    public int compare(final Direction left, final Direction right) {
      return left.ordinal() - right.ordinal();
    }
  };

  private final Map<Direction, Class<? extends Room>> paths = Maps.newHashMap();

  @Override
  public String toString() {
    final List<Direction> keySet = Lists.newArrayList(paths.keySet());
    if (keySet.isEmpty()) {
      return "There are no exits!";
    } else {
      Collections.sort(keySet, DIRECTION_ORDER);
      return "Exits to " + Joiner.on(", ").join(keySet);
    }
  }

  public Class<? extends Room> getRoomInDirection(final Direction direction) {
    return paths.get(direction);
  }
}
