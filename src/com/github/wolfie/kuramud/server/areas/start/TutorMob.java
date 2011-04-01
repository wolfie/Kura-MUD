package com.github.wolfie.kuramud.server.areas.start;

import com.github.wolfie.kuramud.server.NonPlayerCharacter;

public class TutorMob extends NonPlayerCharacter {
  public TutorMob() {
    super("[blue]A tutor[/blue]", null);
    setAttack(500);
    setDefense(500);
    setHealth(100000);
  }

  @Override
  public String getLongDescription() {
    return "The tutor looks very knowledgeable and kind. He could probably kick your ass, if he wanted to.";
  }

  @Override
  public String[] getKeywords() {
    return new String[] { "tutor" };
  }
}
