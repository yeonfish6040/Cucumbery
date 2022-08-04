package com.jho5245.cucumbery.listeners.block;

import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectType;
import com.jho5245.cucumbery.util.storage.data.Variable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageAbortEvent;

import java.util.UUID;

public class BlockDamageAbort implements Listener
{
  @EventHandler
  public void onBlockDamageAbort(BlockDamageAbortEvent event)
  {
    Player player = event.getPlayer();
    UUID uuid = player.getUniqueId();
    if (CustomEffectManager.hasEffect(player, CustomEffectType.CUSTOM_MINING_SPEED_MODE))
    {
      Variable.customMiningProgress.put(uuid, 0f);
      player.sendBlockDamage(new Location(Bukkit.getWorlds().get(0), 0, 0, 0), 0f);
      CustomEffectManager.removeEffect(player, CustomEffectType.CUSTOM_MINING_SPEED_MODE_PROGRESS);
    }
  }
}
