package com.jho5245.cucumbery.events.block;

import com.jho5245.cucumbery.custom.customeffect.custom_mining.MiningManager;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Called when {@link MiningManager#getMiningInfo(Player, Location)} is invoked, to indicate that {@link Player} is about to start mining.
 */
public class PreCustomBlockBreakEvent extends BlockEvent implements Cancellable
{
  boolean cancel;
  private static final HandlerList handlers = new HandlerList();

  private final Player player;

  public PreCustomBlockBreakEvent(@NotNull Block theBlock, @NotNull Player player)
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

  @Override
  public boolean isCancelled()
  {
    return cancel;
  }

  @Override
  public void setCancelled(boolean cancel)
  {
    this.cancel = cancel;
  }
}
