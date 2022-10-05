package com.jho5245.cucumbery.listeners.block;

import com.jho5245.cucumbery.custom.customeffect.custom_mining.MiningManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageAbortEvent;

public class BlockDamageAbort implements Listener
{
  @EventHandler
  public void onBlockDamageAbort(BlockDamageAbortEvent event)
  {
    Player player = event.getPlayer();
    MiningManager.quitCustomMining(player);
  }
}
