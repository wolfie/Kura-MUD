package com.github.wolfie.kuramud.client;

import com.github.wolfie.kuramud.server.PlayerCharacter;
import com.vaadin.Application;
import com.vaadin.service.ApplicationContext.TransactionListener;

public class CurrentPlayer implements TransactionListener {

  /**
     * 
     */
  private static final long serialVersionUID = 7586177412183324350L;
  private static ThreadLocal<CurrentPlayer> instance = new ThreadLocal<CurrentPlayer>();
  private final Application application;
  private final PlayerCharacter player;

  public CurrentPlayer(final Application application) {
    this.application = application;
    player = new PlayerCharacter();
    // Set a value for the ThreadLocal to avoid any NPEs
    instance.set(this);
  }

  public static PlayerCharacter getPlayer() {
    return instance.get().player;
  }

  @Override
  public void transactionEnd(final Application application,
      final Object transactionData) {
    // Clear thread local instance at the end of the transaction
    if (this.application == application) {
      instance.set(null);
    }
  }

  @Override
  public void transactionStart(final Application application,
      final Object transactionData) {
    // Set the thread local instance
    if (this.application == application) {
      instance.set(this);
    }
  }

  public static void initialize(final Application application) {
    if (application == null) {
      throw new IllegalArgumentException("Application may not be null");
    }
    final CurrentPlayer currentPlayer = new CurrentPlayer(application);
    application.getContext().addTransactionListener(currentPlayer);
  }
}
