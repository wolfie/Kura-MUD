package com.github.wolfie.kuramud.server.item;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.github.wolfie.kuramud.server.Displayable;
import com.github.wolfie.kuramud.server.Targetables;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multiset.Entry;

public abstract class ItemContainer {

  public interface PeekFindings {
    String getItemDescription();

    int getAmount();
  }

  private final int totalCapacity;
  private int usedCapacity = 0;
  private final Multiset<Item> items = HashMultiset.create();

  protected ItemContainer(final int totalCapacity) {
    this.totalCapacity = totalCapacity;
  }

  public int getTotalCapacity() {
    return totalCapacity;
  }

  public int getAvailableCapacity() {
    final int availableCapacity = totalCapacity - usedCapacity;
    if (availableCapacity < 0) {
      throw new RuntimeException("Available capacity was less than zero");
    }

    return availableCapacity;
  }

  public void put(final Item item) throws OutOfCapacityException {
    if (hasCapacityFor(item)) {
      items.add(item);
      usedCapacity += item.getRequiredCapacity();
    } else {
      throw new OutOfCapacityException(this, item);
    }
  }

  public void remove(final Item item) throws NoSuchItemException {
    final boolean success = items.remove(item);

    if (!success) {
      throw new NoSuchItemException(this, item);
    }
  }

  private boolean hasCapacityFor(final Item item) {
    return getAvailableCapacity() >= item.getRequiredCapacity();
  }

  public boolean isEmpty() {
    return usedCapacity == 0;
  }

  /**
   * Get the contents of the {@link ItemContainer} in a textual representation.
   */
  @Override
  public String toString() {
    if (items.isEmpty()) {
      return "It's empty";
    }

    final List<Item> itemTypes = Lists.newLinkedList();
    for (final Entry<Item> entry : items.entrySet()) {
      itemTypes.add(entry.getElement());
    }

    Collections.sort(itemTypes, Displayable.SHORT_COMPARATOR);

    final StringBuilder sb = new StringBuilder();
    for (final Item item : itemTypes) {
      sb.append(items.count(item) + " * " + item.getShortDescription() + '\n');
    }
    return sb.toString();
  }

  /**
   * Try to find an item matching the given keyword argument.
   * 
   * @param argument
   *          The argument to match for
   * @return A {@link PeekFindings} instance of the found thing in the
   *         <code>ItemContainer</code>. <code>null</code> will be returned, if
   *         no matches were found.
   */
  public PeekFindings peek(final String target) {
    // TODO: optimization opportunity.

    final Collection<Item> items = getItems(target);

    if (!items.isEmpty()) {
      final int amount = items.size();
      final String desc = items.iterator().next().getLongDescription();
      return new PeekFindings() {
        @Override
        public int getAmount() {
          return amount;
        }

        @Override
        public String getItemDescription() {
          return desc;
        }
      };
    } else {
      return null;
    }
  }

  public Item removeItem(final String target) throws NoSuchItemException {
    final Collection<Item> items = getItems(target);
    Item item = null;
    if (!items.isEmpty()) {
      item = items.iterator().next();
      remove(item);
      return item;
    } else {
      throw new NoSuchItemException(this, target);
    }
  }

  private Collection<Item> getItems(final String target) {
    final Multiset<Item> results = HashMultiset.create();
    for (final Item item : items) {
      if (Targetables.matches(target, item)) {
        results.add(item);
      }
    }
    return results;
  }
}
