package com.jho5245.cucumbery.custom.recipe;

import com.jho5245.cucumbery.util.itemlore.ItemLore;
import de.tr7zw.changeme.nbtapi.NBTContainer;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.jetbrains.annotations.NotNull;

public class PredicateChoice implements RecipeChoice
{
  private final boolean matchType, matchAmount;

  private final int amount;

  private final String predicate;
  private NBTContainer nbtContainer;

  public PredicateChoice(@NotNull String predicate)
  {
    this(predicate, false, false, 1);
  }
  public PredicateChoice(@NotNull String predicate, int amount)
  {
    this(predicate, false, true, amount);
  }
  public PredicateChoice(@NotNull String predicate, boolean matchType, boolean matchAmount, int amount)
  {
    this.predicate = predicate;
    this.nbtContainer = new NBTContainer(predicate);
    this.matchType = matchType;
    this.matchAmount = matchAmount;
    this.amount = amount;
  }

  @NotNull
  public String getPredicate()
  {
    return predicate;
  }

  /**
   * Gets a single item stack representative of this stack choice.
   *
   * @return a single representative item
   * @deprecated for compatibility only
   */
  @Override
  public @NotNull ItemStack getItemStack()
  {
    ItemStack itemStack = new ItemStack(Material.STONE);
    new NBTItem(itemStack, true).mergeCompound(nbtContainer);
    ItemLore.setItemLore(itemStack);
    return itemStack;
  }

  @Override
  @NotNull
  public PredicateChoice clone() {
    try {
      PredicateChoice predicateChoice = (PredicateChoice) super.clone();
      predicateChoice.nbtContainer = nbtContainer;
      return predicateChoice;
    } catch (CloneNotSupportedException ex) {
      throw new AssertionError(ex);
    }
  }

  @Override
  public boolean test(@NotNull ItemStack itemStack)
  {
    if (matchAmount && itemStack.getAmount() != amount)
    {
      return false;
    }
    if (matchType && itemStack.getType() != getItemStack().getType())
    {
      return false;
    }
    NBTItem nbtItem = new NBTItem(itemStack);
    nbtItem.mergeCompound(nbtContainer);
    return nbtItem.getCompound().toString().equals(new NBTItem(itemStack).getCompound().toString());
  }
}
