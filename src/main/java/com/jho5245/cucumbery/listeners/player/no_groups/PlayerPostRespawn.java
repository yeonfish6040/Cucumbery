package com.jho5245.cucumbery.listeners.player.no_groups;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.custom_mining.MiningScheduler;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectTypeCustomMining;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Scheduler;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import de.tr7zw.changeme.nbtapi.NBTContainer;
import de.tr7zw.changeme.nbtapi.NBTEntity;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class PlayerPostRespawn implements Listener
{
  @EventHandler
  public void onPlayerPostRespawn(PlayerPostRespawnEvent event)
  {
    Player player = event.getPlayer();
    UUID uuid = player.getUniqueId();
    if (Variable.buffFreezerEffects.containsKey(uuid))
    {
      MessageUtil.sendMessage(player, Prefix.INFO_CUSTOM_EFFECT, "%s 효과로 버프를 보호했습니다",
              CustomEffectManager.hasEffect(player, CustomEffectType.BUFF_FREEZE_D) ? CustomEffectType.BUFF_FREEZE_D : CustomEffectType.BUFF_FREEZE);
      player.addPotionEffects(Variable.buffFreezerEffects.get(uuid));
      Variable.buffFreezerEffects.remove(uuid);
    }
    CustomEffectManager.refreshAttributeEffects(player);
    if (CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE))
    {
      for (int i = 0; i < 10; i++)
      {
        Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
        {
          Scheduler.fakeBlocksAsync(player, true);
          MiningScheduler.customMining(player, true);
        }, i * 20);
      }
    }
    if (CustomEffectManager.hasEffect(player, CustomEffectType.STRANGE_CREATIVE_MODE))
    {
      NBTEntity nbtEntity = new NBTEntity(player);
      player.setGameMode(GameMode.CREATIVE);
      nbtEntity.mergeCompound(new NBTContainer("{abilities:{instabuild:0b,invulnerable:0b}}"));
      player.setAllowFlight(false);
    }
    ItemStackUtil.updateInventory(player);
  }
}
