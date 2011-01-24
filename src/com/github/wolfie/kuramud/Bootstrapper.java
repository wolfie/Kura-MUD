package com.github.wolfie.kuramud;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.github.wolfie.kuramud.server.Core;

public class Bootstrapper implements ServletContextListener {
	
	@Override
	public void contextInitialized(final ServletContextEvent arg0) {
		Core.bootstrap();
	}
	
	@Override
	public void contextDestroyed(final ServletContextEvent arg0) {
		Core.shutdown();
	}
	
}
