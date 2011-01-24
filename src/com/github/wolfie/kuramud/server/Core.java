package com.github.wolfie.kuramud.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.github.wolfie.blackboard.Blackboard;
import com.github.wolfie.kuramud.server.areas.start.NullRoom.EasternRoom;
import com.github.wolfie.kuramud.server.areas.start.NullRoom.NorthernRoom;
import com.github.wolfie.kuramud.server.areas.start.NullRoom.SouthernRoom;
import com.github.wolfie.kuramud.server.areas.start.NullRoom.WesternRoom;
import com.github.wolfie.kuramud.server.areas.start.StartRoom;
import com.github.wolfie.kuramud.server.blackboard.OutputListener;
import com.github.wolfie.kuramud.server.blackboard.OutputListener.OutputEvent;
import com.github.wolfie.kuramud.server.blackboard.PlayerLoginListener;
import com.github.wolfie.kuramud.server.blackboard.PlayerLoginListener.PlayerLoginEvent;
import com.github.wolfie.kuramud.server.blackboard.PlayerLogoutListener;
import com.github.wolfie.kuramud.server.blackboard.PlayerLogoutListener.PlayerLogoutEvent;
import com.github.wolfie.kuramud.server.blackboard.ResetListener;
import com.github.wolfie.kuramud.server.blackboard.ResetListener.ResetEvent;
import com.github.wolfie.kuramud.server.blackboard.TickListener;
import com.github.wolfie.kuramud.server.blackboard.TickListener.TickEvent;

public class Core extends Thread {
	
	private static class RoomInstances {
		private final Map<Class<? extends Room>, Room> roomInstances = new HashMap<Class<? extends Room>, Room>();
		
		private void add(final Room room) {
			roomInstances.put(room.getClass(), room);
			BLACKBOARD.addListener(room);
		}
		
		private void clear() {
			for (final Room room : roomInstances.values()) {
				BLACKBOARD.removeListener(room);
			}
			roomInstances.clear();
		}
		
		public Room get(final Class<? extends Room> roomClass) {
			return roomInstances.get(roomClass);
		}
	}
	
	private static final RoomInstances ROOM_INSTANCES = new RoomInstances();
	
	public static final Blackboard BLACKBOARD = new Blackboard();
	private static final Core SINGLETON = new Core();
	
	private static final StartRoom START_ROOM = new StartRoom();
	private static final Set<PlayerCharacter> PLAYERS_ONLINE = new HashSet<PlayerCharacter>();
	
	private static final long TICK_LENGTH_MILLIS = 5000;
	private static final int TICKS_BEFORE_RESET = 20;
	
	public static void bootstrap() {
		SINGLETON.start();
	}
	
	private boolean running = false;
	
	private int ticks = 0;
	
	private Core() {
	}
	
	@Override
	public void run() {
		BLACKBOARD.register(OutputListener.class, OutputEvent.class);
		BLACKBOARD.register(PlayerLoginListener.class, PlayerLoginEvent.class);
		BLACKBOARD.register(PlayerLogoutListener.class, PlayerLogoutEvent.class);
		BLACKBOARD.register(ResetListener.class, ResetEvent.class);
		BLACKBOARD.register(TickListener.class, TickEvent.class);
		
		initRoomInstances();
		resetAllRooms();
		
		running = true;
		while (running) {
			try {
				Thread.sleep(TICK_LENGTH_MILLIS);
				if (ticks++ < TICKS_BEFORE_RESET) {
					Core.outputGlobal("You hear a tick.");
					BLACKBOARD.fire(new TickEvent());
				} else {
					Core.outputGlobal("You notice everything simply reset.");
					BLACKBOARD.fire(new ResetEvent());
					ticks = 0;
				}
			} catch (final InterruptedException e) {
				e.printStackTrace();
				running = false;
			}
		}
	}
	
	public static boolean isRunning() {
		return SINGLETON.running;
	}
	
	private static void initRoomInstances() {
		ROOM_INSTANCES.clear();
		ROOM_INSTANCES.add(START_ROOM);
		ROOM_INSTANCES.add(new NorthernRoom());
		ROOM_INSTANCES.add(new SouthernRoom());
		ROOM_INSTANCES.add(new EasternRoom());
		ROOM_INSTANCES.add(new WesternRoom());
	}
	
	private static void resetAllRooms() {
		BLACKBOARD.fire(new ResetEvent());
	}
	
	public static Room getStartRoom() {
		return START_ROOM;
	}
	
	public static void output(final Room room, final String output) {
		if (room == null || output == null) {
			throw new IllegalArgumentException("Arguments may not be null");
		}
		BLACKBOARD.fire(new OutputListener.OutputEvent(room, output));
	}
	
	public static void output(final PlayerCharacter player, final String output) {
		BLACKBOARD.fire(new OutputListener.OutputEvent(player, output));
	}
	
	public static void outputGlobal(final String output) {
		if (output == null) {
			throw new IllegalArgumentException("output may not be null");
		}
		BLACKBOARD.fire(new OutputListener.OutputEvent(output));
	}
	
	public static void addMudOutput(final OutputListener outputListener) {
		BLACKBOARD.addListener(outputListener);
	}
	
	public static void removeMudOutput(final OutputListener outputListener) {
		BLACKBOARD.removeListener(outputListener);
	}
	
	public static void teleportPlayerTo(final PlayerCharacter player,
			final Room room) {
		player.getCurrentRoom().teleportAway(player);
		room.teleportInto(player);
	}
	
	public static void login(final PlayerCharacter playerCharacter) {
		PLAYERS_ONLINE.add(playerCharacter);
		playerCharacter.setCurrentRoom(START_ROOM);
		START_ROOM.teleportInto(playerCharacter);
		BLACKBOARD.fire(new PlayerLoginEvent(playerCharacter));
	}
	
	public static void logout(final PlayerCharacter playerCharacter) {
		if (PLAYERS_ONLINE.contains(playerCharacter)) {
			BLACKBOARD.fire(new PlayerLogoutEvent(playerCharacter));
			playerCharacter.getCurrentRoom().teleportAway(playerCharacter);
			PLAYERS_ONLINE.remove(playerCharacter);
		}
	}
	
	public static Room getRoomInstance(final Class<? extends Room> roomClass) {
		return ROOM_INSTANCES.get(roomClass);
	}
	
	public static void shutdown() {
		for (final PlayerCharacter player : PLAYERS_ONLINE) {
			logout(player);
		}
		
		PLAYERS_ONLINE.clear();
		ROOM_INSTANCES.clear();
		SINGLETON.running = false;
	}
}
