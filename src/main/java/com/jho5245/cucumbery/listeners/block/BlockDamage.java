package com.jho5245.cucumbery.listeners.block;

import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.children.group.LocationCustomEffectImple;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectTypeCustomMining;
import com.jho5245.cucumbery.util.no_groups.Method2;
import com.jho5245.cucumbery.util.storage.data.Variable;
import org.bukkit.Location;
import org.bukkit.Material;
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
    Location location = block.getLocation();
    Material blockType = block.getType();
    if (CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE))
    {
      if (CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_2_NO_RESTORE_IGNORE_INSTA_BLOCKS) && blockType.getHardness() == 0f && Method2.getPlacedBlockDataAsItemStack(location) == null)
      {
        return;
      }
      if (!(block.getType() == Material.FIRE && CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_2_NO_RESTORE)))
      {
        event.setCancelled(true);
      }
      if (!Variable.customMiningCooldown.containsKey(location) || Variable.customMiningExtraBlocks.containsKey(location))
      {
        CustomEffectManager.addEffect(player, new LocationCustomEffectImple(CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_PROGRESS, block.getLocation()));
      }
    }
  }
}
