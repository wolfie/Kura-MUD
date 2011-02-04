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
    private Application application;
    private PlayerCharacter player;

    public CurrentPlayer(Application application) {
        this.application = application;
        this.player = new PlayerCharacter();
        // Set a value for the ThreadLocal to avoid any NPEs
        instance.set(this);
    }

    public static PlayerCharacter getPlayer() {
        return instance.get().player;
    }

    @Override
    public void transactionEnd(Application application, Object transactionData) {
        // Clear thread local instance at the end of the transaction
        if (this.application == application) {
            instance.set(null);
        }
    }

    @Override
    public void transactionStart(Application application, Object transactionData) {
        // Set the thread local instance
        if (this.application == application) {
            instance.set(this);
        }
    }

    public static void initialize(Application application) {
        if (application == null) {
            throw new IllegalArgumentException("Application may not be null");
        }
        CurrentPlayer appSettings = new CurrentPlayer(application);
        application.getContext().addTransactionListener(appSettings);
    }
}
