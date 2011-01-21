package com.github.wolfie.kuramud.server.exception;

public class FatalError extends Error {
  private static final long serialVersionUID = 748789719807013929L;

  public FatalError(final String reason) {
    super(reason);
  }

  public FatalError(final Exception e) {
    super(e);
  }
}
