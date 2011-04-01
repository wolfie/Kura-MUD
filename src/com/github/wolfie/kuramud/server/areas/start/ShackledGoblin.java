package com.github.wolfie.kuramud.server.areas.start;

import com.github.wolfie.kuramud.server.NonPlayerCharacter;
import com.github.wolfie.kuramud.server.item.CharacterInventory;

public class ShackledGoblin extends NonPlayerCharacter {

  public ShackledGoblin() {
    super("Shackled goblin", CharacterInventory.NULL);
  }

  @Override
  public String getLongDescription() {
    return "A goblin is shackled to the wall with short pieces of chain. It seems really pissed off, and flails furiously with its arms. You hear him trying to mock you with bad 'your mother' jokes. Maybe teach him a lesson?";
  }

  @Override
  public String[] getKeywords() {
    return new String[] { "shackled", "goblin" };
  }
}
