package com.jho5245.cucumbery.events.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.jetbrains.annotations.NotNull;

public class CustomBlockBreakEvent extends BlockEvent
{
  private static final HandlerList handlers = new HandlerList();

  private final Player player;

  public CustomBlockBreakEvent(@NotNull Block theBlock, @NotNull Player player)
  {
    super(theBlock);
    this.player = player;
  }

  public Player getPlayer()
  {
    return player;
  }

  @Override
  public @NotNull HandlerList getHandlers()
  {
    return handlers;
  }

  @NotNull
  public static HandlerList getHandlerList() {
    return handlers;
  }
}
