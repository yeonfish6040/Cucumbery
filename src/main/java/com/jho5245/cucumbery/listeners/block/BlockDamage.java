package com.jho5245.cucumbery.listeners.block;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.children.group.LocationCustomEffectImple;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectTypeCustomMining;
import com.jho5245.cucumbery.util.blockplacedata.BlockPlaceDataConfig;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.no_groups.SoundPlay;
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
    final Location location = block.getLocation();
    if (CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE))
    {
      if (CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_PROGRESS))
      {
        return;
      }
      boolean instaBreak = block.getType().getHardness() == 0f;
      if (block.getType() == Material.FIRE || instaBreak)
      {
        if (!CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_2_NO_RESTORE))
        {
          event.setCancelled(true);
        }
        return;
      }
      if (!Cucumbery.using_mcMMO)
      {
        event.setCancelled(true);
      }
      if (!Variable.customMiningCooldown.containsKey(location) || Variable.customMiningExtraBlocks.containsKey(location))
      {
        if (CustomMaterial.itemStackOf(BlockPlaceDataConfig.getItem(location)) == CustomMaterial.SUS)
        {
          SoundPlay.playSound(player, "custom_sus_breaking");
        }
        CustomEffectManager.addEffect(player, new LocationCustomEffectImple(CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_PROGRESS, location));
      }
    }
  }
}
