package com.github.wolfie.kuramud.server.blackboard;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public interface TickListener extends Listener {
  public class TickEvent implements Event {
  }

  public void tick(TickEvent event);
}
