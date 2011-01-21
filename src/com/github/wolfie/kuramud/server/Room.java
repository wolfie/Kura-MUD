package com.github.wolfie.kuramud.server;

import java.util.Set;

import com.github.wolfie.kuramud.server.blackboard.ResetListener;
import com.github.wolfie.kuramud.server.blackboard.TickListener;
import com.google.gwt.thirdparty.guava.common.collect.ImmutableSet;

public abstract class Room implements ResetListener, TickListener {

  private final ImmutableSet<Path> paths;

  protected Room(final Set<Path> directions) {
    Core.BLACKBOARD.addListener(this);
    paths = ImmutableSet.copyOf(directions);
  }

  protected Room(final Path path) {
    paths = ImmutableSet.of(path);
  }

  public Set<Path> getDirections() {
    return paths;
  }

  abstract public String getLookDescription();

  abstract public String getLongDescription();

  public void output(final String string) {
    // TODO Auto-generated method stub

  }
}
