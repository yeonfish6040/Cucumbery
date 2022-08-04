package com.jho5245.cucumbery.util.itemlore;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Represents view of an {@link org.bukkit.inventory.ItemStack}'s lore.
 */
public record ItemLoreView(@NotNull Player player)
{
  public ItemLoreView(@NotNull Player player)
  {
    this.player = player;
  }

  @NotNull
  public Player getPlayer()
  {
    return player;
  }

  public static ItemLoreView of(@NotNull Player player)
  {
    return new ItemLoreView(player);
  }
}
