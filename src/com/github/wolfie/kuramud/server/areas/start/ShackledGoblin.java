package com.github.wolfie.kuramud.server.areas.start;

import com.github.wolfie.kuramud.server.NonPlayerCharacter;

public class ShackledGoblin extends NonPlayerCharacter {

  public ShackledGoblin() {
    super("Shackled goblin", "shackled", "goblin");
  }

  @Override
  public String getLongDescription() {
    return "A goblin is shackled to the wall with short pieces of chain. It seems really pissed off, and flails furiously with its arms. You hear him trying to mock you with bad 'your mother' jokes. Maybe teach him a lesson?";
  }

}
