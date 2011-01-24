package com.github.wolfie.kuramud.server;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.github.wolfie.kuramud.Util;
import com.github.wolfie.kuramud.server.blackboard.ResetListener;
import com.github.wolfie.kuramud.server.blackboard.TickListener;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public abstract class Room implements ResetListener, TickListener {
	
	private final Paths paths;
	private final Set<PlayerCharacter> playersInRoom = Collections
			.synchronizedSet(new HashSet<PlayerCharacter>());
	
	private Set<NonPlayerCharacter> npcs = Collections
			.synchronizedSet(new HashSet<NonPlayerCharacter>());
	
	private final Map<Class<? extends NonPlayerCharacter>, Integer> mobsThatShouldBeInRoom = Maps
			.newHashMap();
	
	protected Room(final Paths paths) {
		this.paths = paths;
		Core.BLACKBOARD.addListener(this);
	}
	
	public Room(final Direction direction, final Class<? extends Room> roomClass) {
		this(new Paths.Builder().put(direction, roomClass).build());
	}
	
	abstract public String getName();
	
	abstract public String getShortDescription();
	
	abstract public String getLongDescription();
	
	/**
	 * Output a string to this room.
	 * <p/>
	 * Equivalent to calling {@link Core#output(Room, String)} with this object as
	 * the <code>Room</code> argument.
	 * 
	 * @param string
	 */
	public final void output(final String string) {
		Core.output(this, string);
	}
	
	public final void teleportAway(final PlayerCharacter player) {
		Core.output(this, player + " disappears into a puff of smoke.");
		player.setCurrentRoom(null);
		playersInRoom.remove(player);
	}
	
	public final void teleportInto(final PlayerCharacter player) {
		Core.output(this, player + " appears from a puff of smoke.");
		player.setCurrentRoom(this);
		player.lookAt(this);
		playersInRoom.add(player);
	}
	
	protected final void spawn(final NonPlayerCharacter mob) {
		Core.output(this, mob + " appears from a puff of smoke.");
		mob.setCurrentRoom(this);
		npcs.add(mob);
	}
	
	protected final void destroy(final NonPlayerCharacter mob) {
		if (npcs.contains(mob)) {
			Core.output(this, mob
					+ " disintegrates into a cloud of colorful butterflies");
			npcs.remove(mob);
		}
	}
	
	public final String getRoomString(final PlayerCharacter looker) {
		String desc = getName() + "\n" + getLongDescription() + "\n";
		desc += paths.toString() + "\n";
		
		final Set<Character> players = new HashSet<Character>(
				playersInRoom);
		players.remove(looker);
		players.addAll(npcs);
		
		if (!players.isEmpty()) {
			desc += "You also see:\n" + Util.join(players, "\n") + "\n";
		} else {
			desc += "There's nothing else here.\n";
		}
		
		return desc;
	}
	
	protected void mobInRoom(final int amount,
			final Class<? extends NonPlayerCharacter> mobClass) {
		mobsThatShouldBeInRoom.put(mobClass, amount);
	}
	
	protected void reset() {
		final Set<NonPlayerCharacter> newNpcs = Sets.newHashSet();
		
		for (final Entry<Class<? extends NonPlayerCharacter>, Integer> entry : Maps
				.newHashMap(mobsThatShouldBeInRoom)
				.entrySet()) {
			int amount = entry.getValue();
			final Class<? extends NonPlayerCharacter> mobClass = entry.getKey();
			
			for (final NonPlayerCharacter mobInRoom : npcs) {
				if (mobClass.equals(mobInRoom.getClass())) {
					amount--;
					mobsThatShouldBeInRoom.put(mobClass, amount);
					newNpcs.add(mobInRoom);
					npcs.remove(mobInRoom);
				}
				if (amount == 0) {
					continue;
				}
			}
		}
		
		if (!npcs.isEmpty()) {
			for (final NonPlayerCharacter mob : npcs) {
				destroy(mob);
			}
		}
		
		npcs = newNpcs;
		
		if (!mobsThatShouldBeInRoom.isEmpty()) {
			try {
				for (final Entry<Class<? extends NonPlayerCharacter>, Integer> entry : mobsThatShouldBeInRoom
						.entrySet()) {
					final Class<? extends NonPlayerCharacter> mobClass = entry.getKey();
					final int amount = entry.getValue();
					for (int i = 0; i < amount; i++) {
						spawn(mobClass.newInstance());
					}
				}
			} catch (final SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (final IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (final InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (final IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mobsThatShouldBeInRoom.clear();
		}
	}
}
