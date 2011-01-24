package com.github.wolfie.kuramud.server.blackboard;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;
import com.github.wolfie.kuramud.server.PlayerCharacter;

public interface PlayerLoginListener extends Listener {
	public class PlayerLoginEvent implements Event {
		private final PlayerCharacter playerCharacter;
		
		public PlayerLoginEvent(final PlayerCharacter playerCharacter) {
			this.playerCharacter = playerCharacter;
		}
		
		public PlayerCharacter getPlayerCharacter() {
			return playerCharacter;
		}
	}
	
	public void playerLogin(PlayerLoginEvent event);
}
