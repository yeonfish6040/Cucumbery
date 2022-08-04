package com.jho5245.cucumbery.listeners.block;

import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectType;
import com.jho5245.cucumbery.custom.customeffect.children.group.LocationCustomEffectImple;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;

public class BlockDamage implements Listener
{
  @EventHandler
  public void onBlockDamage(BlockDamageEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    Player player = event.getPlayer();
    if (CustomEffectManager.hasEffect(player, CustomEffectType.CURSE_OF_CREATIVITY) || CustomEffectManager.hasEffect(player, CustomEffectType.CURSE_OF_CREATIVITY_BREAK))
    {
      event.setCancelled(true);
      return;
    }
    Block block = event.getBlock();
    if (CustomEffectManager.hasEffect(player, CustomEffectType.CUSTOM_MINING_SPEED_MODE))
    {
      CustomEffectManager.addEffect(player, new LocationCustomEffectImple(CustomEffectType.CUSTOM_MINING_SPEED_MODE_PROGRESS, block.getLocation()));
    }
  }
}
