package com.github.wolfie.kuramud.server.blackboard;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;
import com.github.wolfie.kuramud.server.PlayerCharacter;

public interface PlayerLogoutListener extends Listener {
  public class PlayerLogoutEvent implements Event {
    private final PlayerCharacter playerCharacter;

    public PlayerLogoutEvent(final PlayerCharacter playerCharacter) {
      this.playerCharacter = playerCharacter;
    }

    public PlayerCharacter getPlayerCharacter() {
      return playerCharacter;
    }
  }

  public void playerLogout(PlayerLogoutEvent event);
}
