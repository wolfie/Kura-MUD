package com.github.wolfie.kuramud.server.areas.start;

import com.github.wolfie.kuramud.server.Direction;
import com.github.wolfie.kuramud.server.Room;
import com.github.wolfie.kuramud.server.areas.start.NullRoom.WesternRoom;

public class CombatRoom extends Room {

  public CombatRoom() {
    super(Direction.EAST, WesternRoom.class);
  }

  @Override
  protected void reset() {
    mobInRoom(5, ShackledGoblin.class);
  }

  @Override
  public void worldTick(final WorldTickEvent event) {
  }

  @Override
  public String getName() {
    return "Combat room";
  }

  @Override
  public String getShortDescription() {
    return "A room for combat training";
  }

  @Override
  public String getLongDescription() {
    return "In a room that looks like some kind of combat training area, you see some goblins shackled to the wall. Why don't you 'look' at the goblin?";
  }

}
