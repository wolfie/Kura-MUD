package com.github.wolfie.kuramud.server.blackboard;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public interface ResetListener extends Listener {
  public class ResetEvent implements Event {
  }

  public void reset(ResetEvent event);
}
