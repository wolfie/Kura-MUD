package com.github.wolfie.kuramud.server;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import com.github.wolfie.kuramud.server.blackboard.WorldResetListener;
import com.github.wolfie.kuramud.server.blackboard.WorldTickListener;
import com.github.wolfie.kuramud.server.item.Item;
import com.github.wolfie.kuramud.server.item.ItemContainer.PeekFindings;
import com.github.wolfie.kuramud.server.item.NoSuchItemException;
import com.github.wolfie.kuramud.server.item.OutOfCapacityException;
import com.github.wolfie.kuramud.server.item.RoomItemContainer;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public abstract class Room implements WorldResetListener, WorldTickListener,
    Displayable {

  // @formatter:off
  private final Paths paths;
  private final Set<PlayerCharacter> playersInRoom = Collections.synchronizedSet(new HashSet<PlayerCharacter>());
  private final RoomItemContainer items = new RoomItemContainer();
  // @formatter:on

  /**
   * A map of mob keywords to the instances of the mobs.
   * <p/>
   * Restriction: only one type of mobs may be in a room with the same keyword.
   * <p/>
   * This makes it easy to find the mobs by their keywords. One mob can respond
   * to many keywords, and there can be many mobs of a kind, ordered in a List.
   */
  // @formatter:off
  private final ConcurrentMap<String, List<NonPlayerCharacter>> mobs = Maps.newConcurrentMap();

  private final Map<Class<? extends NonPlayerCharacter>, Integer> mobsThatShouldBeAddedToRoom = Maps.newHashMap();
  private final Map<Class<? extends Item>, Integer> itemsThatShouldBeAddedToRoom = Maps.newHashMap();

  private Set<NonPlayerCharacter> homeMobs = null;
  private Set<Item> homeItems = null;
  // @formatter:on

  protected Room(final Paths paths) {
    this.paths = paths;
  }

  public Room(final Direction direction, final Class<? extends Room> roomClass) {
    this(new Paths.Builder().put(direction, roomClass).build());
  }

  abstract public String getName();

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

  public final void teleportAway(final Character character) {
    Core.output(this, character + " disappears into a puff of smoke.");
    character.setCurrentRoom(null);

    if (character instanceof PlayerCharacter) {
      playersInRoom.remove(character);
    } else {
      remove((NonPlayerCharacter) character);
    }
  }

  public final void teleportInto(final Character character) {
    Core.output(this, character + " appears from a puff of smoke.");
    character.setCurrentRoom(this);

    if (character instanceof PlayerCharacter) {
      final PlayerCharacter player = (PlayerCharacter) character;
      player.look();
      playersInRoom.add(player);
    } else {
      add((NonPlayerCharacter) character);
    }
  }

  protected final void spawn(final NonPlayerCharacter mob) {
    Core.output(this, mob + " appears from a puff of smoke.");
    add(mob);
  }

  private void add(final NonPlayerCharacter mob) {
    List<NonPlayerCharacter> list = null;

    /*
     * Find the list where the same kind of mobs exist, and add the new one
     * there
     */
    if (list == null) {
      final List<NonPlayerCharacter> mobsOfKindInRoom = mobs.get(mob
          .getKeywords()[0]);

      if (mobsOfKindInRoom != null) {
        list = mobsOfKindInRoom;
      } else {
        list = Lists.newArrayList();
      }

      mob.setCurrentRoom(this);
      list.add(mob);
    }

    for (final String mobKeyword : mob.getKeywords()) {

      // make sure that that one list is used for each keyword for that same
      // type of mobs. This is unnecessary to do for other times than for the
      // first instance of a mob type, but it doesn't really hurt much either.
      // OPTIMIZATION OPPORTUNITY: really make sure that this is not done
      // unnecessarily.

      mobs.put(mobKeyword, list);
    }
  }

  protected final void destroy(final NonPlayerCharacter mob) {
    final boolean success = remove(mob);
    if (success) {
      Core.output(this, mob
          + " disintegrates into a cloud of colorful butterflies");
    }
  }

  /**
   * 
   * @param mob
   * @return <code>true</code> if, and only if, the given mob was removed from
   *         the room.
   */
  private boolean remove(final NonPlayerCharacter mob) {
    final String mobKeyword = mob.getKeywords()[0];
    final List<NonPlayerCharacter> mobList = mobs.get(mobKeyword);
    if (mobList != null) {
      final boolean success = mobList.remove(mob);
      if (success) {
        mob.setCurrentRoom(null);
      }
      return success;
    } else {
      return false;
    }
  }

  public final String getRoomString(final PlayerCharacter looker) {
    String desc = getName() + "\n" + getLongDescription() + "\n";
    desc += paths.toString() + "\n";

    final Set<Character> characters = new HashSet<Character>(playersInRoom);
    characters.remove(looker);

    for (final Collection<NonPlayerCharacter> mobs : this.mobs.values()) {
      characters.addAll(mobs);
    }

    if (!characters.isEmpty()) {
      desc += "You also see:\n" + Joiner.on("\n").join(characters) + "\n";
    } else {
      desc += "There's nothing else here.\n";
    }

    if (!items.isEmpty()) {
      desc += "\nStuff lying on the floor:\n" + items.toString();
    }

    return desc;
  }

  protected void mobInRoom(final int amount,
      final Class<? extends NonPlayerCharacter> mobClass) {
    mobsThatShouldBeAddedToRoom.put(mobClass, amount);
  }

  protected void itemInRoom(final int amount,
      final Class<? extends Item> itemClass) {
    itemsThatShouldBeAddedToRoom.put(itemClass, amount);
  }

  @Override
  public void worldReset(final WorldResetEvent event) {
    reset();
    if (homeMobs == null) {
      firstReset();
    } else {
      subsequentReset();
    }
  }

  protected abstract void reset();

  private void firstReset() {
    homeMobs = Sets.newHashSet();
    homeItems = Sets.newHashSet();

    try {
      for (final Entry<Class<? extends NonPlayerCharacter>, Integer> entry : mobsThatShouldBeAddedToRoom
          .entrySet()) {
        final Class<? extends NonPlayerCharacter> mobClass = entry.getKey();
        final int amount = entry.getValue();

        for (int i = 0; i < amount; i++) {
          final NonPlayerCharacter mob = mobClass.newInstance();
          add(mob);
        }
      }

      for (final Entry<Class<? extends Item>, Integer> entry : itemsThatShouldBeAddedToRoom
          .entrySet()) {
        final Class<? extends Item> itemClass = entry.getKey();
        final int amount = entry.getValue();

        for (int i = 0; i < amount; i++) {
          final Item item = itemClass.newInstance();
          items.put(item);
        }
      }
    } catch (final IllegalAccessException e) {
      e.printStackTrace();
    } catch (final InstantiationException e) {
      e.printStackTrace();
    } catch (final OutOfCapacityException e) {
      throw new RuntimeException("Error when adding initial items to room "
          + this, e);
    }
  }

  private void subsequentReset() {
    for (final NonPlayerCharacter mob : homeMobs) {
      if (mob.getCurrentRoom() != this) {
        mob.getCurrentRoom().destroy(mob);
        spawn(mob);
      }
    }
  }

  /**
   * Checks whether a player may leave in a certain direction.
   * <p/>
   * <b>Note:</b> This method will notify the player of the reason to why that
   * player can't go there.
   * 
   * @param direction
   * @param player
   * @return
   */
  public boolean checkAccessibility(final Direction direction,
      final PlayerCharacter player) {
    final boolean success = (getRoomInDirection(direction) != null);
    if (!success) {
      Core.output(player, "You can't find a way to go " + direction + ".");
    }
    return success;
  }

  public boolean checkMayLeave(final Direction direction,
      final PlayerCharacter player) {
    return true;
  }

  public boolean checkMayEnter(final Direction direction,
      final PlayerCharacter player) {
    return true;
  }

  public Room getRoomInDirection(final Direction direction) {
    final Class<? extends Room> roomInDirection = paths
        .getRoomInDirection(direction);
    if (roomInDirection != null) {
      return Core.getRoomInstance(roomInDirection);
    } else {
      return null;
    }
  }

  public boolean remove(final PlayerCharacter player) {
    final boolean success = playersInRoom.remove(player);
    if (success) {
      player.setCurrentRoom(null);
    }
    return success;
  }

  public boolean add(final PlayerCharacter player) {
    final boolean success = playersInRoom.add(player);
    if (success) {
      player.setCurrentRoom(this);
    }
    return success;
  }

  public void look(final String argument, final PlayerCharacter lookingPlayer) {

    final boolean mobsSuccess = lookMobs(argument, lookingPlayer);
    final boolean itemsSuccess = lookItems(argument, lookingPlayer);

    if (!mobsSuccess && !itemsSuccess) {
      lookingPlayer.output("You can't find it.");
    }
  }

  private boolean lookMobs(final String argument,
      final PlayerCharacter lookingPlayer) {
    final List<NonPlayerCharacter> mobs = this.mobs.get(argument);
    if (mobs != null && !mobs.isEmpty()) {
      lookingPlayer.output(mobs.get(0).getLongDescription());
      if (mobs.size() > 1) {
        lookingPlayer.output("There's " + mobs.size() + " of them here.");
      }
      return true;
    }

    return false;
  }

  private boolean lookItems(final String argument,
      final PlayerCharacter lookingPlayer) {
    final PeekFindings findings = items.peek(argument);

    if (findings != null) {
      lookingPlayer.output(findings.getItemDescription());
      if (findings.getAmount() > 1) {
        lookingPlayer.output("You see " + findings.getAmount() + " of them.");
      }
      return true;
    } else {
      return false;
    }
  }

  public void look(final Direction direction,
      final PlayerCharacter lookingPlayer) {
    final Room room = getRoomInDirection(direction);
    if (room != null) {
      lookingPlayer.output(room.getShortDescription());
    } else {
      lookingPlayer.output("There's nothing in that direction.");
    }
  }

  public void say(final PlayerCharacter player, final String arguments) {
    output(player + " says: " + arguments);
  }

  public NonPlayerCharacter getMob(final String arguments) {
    final List<NonPlayerCharacter> mobs = this.mobs.get(arguments);
    if (mobs != null && mobs.size() > 0) {
      return mobs.get(0);
    } else {
      return null;
    }
  }

  protected Set<PlayerCharacter> getPlayersInRoom() {
    return Collections.unmodifiableSet(playersInRoom);
  }

  public Item removeItem(final String target) throws NoSuchItemException {
    return items.removeItem(target);
  }

  public void add(final Item item) throws OutOfCapacityException {
    items.put(item);
  }
}
