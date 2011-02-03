package com.github.wolfie.kuramud.server.blackboard;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public interface WorldResetListener extends Listener {
  public class WorldResetEvent implements Event {
  }

  public void worldReset(WorldResetEvent event);
}
