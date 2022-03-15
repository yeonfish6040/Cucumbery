package com.jho5245.cucumbery.util.itemlore;

import org.bukkit.entity.Player;

public class ItemLoreView
{
  private final Player player;

  public ItemLoreView(Player player)
  {
    this.player = player;
  }

  public Player getPlayer()
  {
    return player;
  }
}
