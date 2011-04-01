package com.github.wolfie.kuramud.server;

import java.util.Comparator;

public interface Displayable {
  Comparator<? super Displayable> SHORT_COMPARATOR = new Comparator<Displayable>() {
    @Override
    public int compare(final Displayable o1, final Displayable o2) {
      return o1.getShortDescription().compareTo(o2.getShortDescription());
    }
  };

  String getShortDescription();

  String getLongDescription();
}
