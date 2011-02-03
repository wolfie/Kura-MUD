package com.github.wolfie.kuramud.server.blackboard;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public interface WorldTickListener extends Listener {
  public class WorldTickEvent implements Event {
  }

  public void worldTick(WorldTickEvent event);
}
