package com.jho5245.cucumbery.listeners.block;

import com.jho5245.cucumbery.custom.customeffect.custom_mining.MiningManager;
import com.jho5245.cucumbery.util.blockplacedata.BlockPlaceDataConfig;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageAbortEvent;
import org.bukkit.inventory.ItemStack;

public class BlockDamageAbort implements Listener
{
  @EventHandler
  public void onBlockDamageAbort(BlockDamageAbortEvent event)
  {
    Player player = event.getPlayer();
    ItemStack itemStack = BlockPlaceDataConfig.getItem(event.getBlock().getLocation());
    if (CustomMaterial.itemStackOf(itemStack) == CustomMaterial.SUS)
    {
      player.stopSound("custom_sus_breaking");
    }
    MiningManager.quitCustomMining(player);
  }
}
