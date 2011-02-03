package com.github.wolfie.kuramud.server;

import com.github.wolfie.kuramud.server.areas.start.StartRoom;

public class PurgatoryRoom extends Room {

  protected PurgatoryRoom() {
    super(Paths.NONE);
  }

  @Override
  public void worldTick(final WorldTickEvent event) {
    // NOOP
  }

  @Override
  public String getName() {
    return "Purgatory";
  }

  @Override
  public String getShortDescription() {
    return "A burning world";
  }

  @Override
  public String getLongDescription() {
    return "Everything is burning eternally here. It's hopeless. You can't escape...";
  }

  @Override
  protected void reset() {
    for (final PlayerCharacter player : getPlayersInRoom()) {
      player.output("You got another chance!");
      Core.teleportCharacterTo(player, StartRoom.class);
    }
  }
}
