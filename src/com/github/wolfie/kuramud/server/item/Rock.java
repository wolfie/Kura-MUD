package com.github.wolfie.kuramud.server.item;


public class Rock extends Item {

  public Rock() {
    super(1);
  }

  @Override
  public String getShortDescription() {
    return "A small rock";
  }

  @Override
  public String getLongDescription() {
    return "A fist-sized rock that has gathered a bit of moss on it";
  }

  @Override
  public String[] getKeywords() {
    return new String[] { "rock" };
  }
}
