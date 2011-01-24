package com.github.wolfie.kuramud.server.blackboard;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;
import com.github.wolfie.kuramud.server.PlayerCharacter;
import com.github.wolfie.kuramud.server.Room;

public interface OutputListener extends Listener {
	public class OutputEvent implements Event {
		private final String output;
		private final Room room;
		private final PlayerCharacter player;
		
		public String getOutput() {
			return output;
		}
		
		public Room getRoom() {
			return room;
		}
		
		public OutputEvent(final Room room, final String output) {
			this.room = room;
			this.output = output;
			player = null;
		}
		
		public OutputEvent(final PlayerCharacter player, final String output) {
			this.player = player;
			this.output = output;
			room = null;
		}
		
		public OutputEvent(final String output) {
			this.output = output;
			player = null;
			room = null;
		}
		
		public PlayerCharacter getPlayer() {
			return player;
		}
	}
	
	public void output(OutputEvent event);
}
