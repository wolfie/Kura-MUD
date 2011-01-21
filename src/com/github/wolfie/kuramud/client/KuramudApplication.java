package com.github.wolfie.kuramud.client;

import com.github.wolfie.kuramud.server.Core;
import com.github.wolfie.kuramud.server.PlayerCharacter;
import com.vaadin.Application;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

public class KuramudApplication extends Application {

  private static final long serialVersionUID = 320988979001844194L;

  private final PlayerCharacter player = new PlayerCharacter();

  @Override
  public void init() {
    Core.add(player);
    player.login();
    final Window mainWindow = new Window("Kura MUD");
    final Label label = new Label("Hello Vaadin user");
    mainWindow.addComponent(label);
    setMainWindow(mainWindow);
  }

  @Override
  public void close() {
    player.logout();
    Core.remove(player);
    super.close();
  }
}
