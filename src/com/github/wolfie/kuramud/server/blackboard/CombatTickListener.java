package com.github.wolfie.kuramud.server.blackboard;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public interface CombatTickListener extends Listener {
  public class CombatTickEvent implements Event {
  }

  public void combatTick(CombatTickEvent event);
}
