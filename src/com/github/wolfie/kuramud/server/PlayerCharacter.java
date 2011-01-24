package com.github.wolfie.kuramud.server;

public class PlayerCharacter extends Character {
	public PlayerCharacter() {
		super("Player " + System.currentTimeMillis());
	}
	
	public void lookAt(final Room room) {
		Core.output(this, room.getRoomString(this));
	}
	
	public void lookAt(final Character character) {
		Core.output(this, character.getLongDescription());
	}
	
	@Override
	public String getLongDescription() {
		return "Looks very much like a hero";
	}
}
