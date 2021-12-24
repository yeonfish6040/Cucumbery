package com.jho5245.cucumbery.listeners.player;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.customeffect.CustomEffect;
import com.jho5245.cucumbery.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.customeffect.CustomEffectType;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant.RestrictionType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PlayerDeath implements Listener
{
  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    Player player = event.getEntity();
    if (UserData.SPECTATOR_MODE.getBoolean(player))
    {
      event.setCancelled(true);
      MessageUtil.info(player, ComponentUtil.translate("관전 모드여서 죽지 않았습니다."));
      return;
    }
    if (UserData.GOD_MODE.getBoolean(player))
    {
      event.setCancelled(true);
      return;
    }

    boolean keepInv = UserData.SAVE_INVENTORY_UPON_DEATH.getBoolean(player) || CustomEffectManager.hasEffect(player, CustomEffectType.KEEP_INVENTORY);
    boolean keepExp = UserData.SAVE_EXPERIENCE_UPON_DEATH.getBoolean(player);
    if (keepInv)
    {
      event.setKeepInventory(true);
      event.getDrops().removeAll(event.getDrops());
    }
    if (keepExp)
    {
      event.setKeepLevel(true);
      event.setDroppedExp(0);
    }

    if (!keepInv && CustomEffectManager.hasEffect(player, CustomEffectType.CURSE_OF_INVENTORY))
    {
      event.setKeepInventory(false);
    }

    if (!event.getKeepInventory())
    {
      List<ItemStack> drops = event.getDrops();
      List<ItemStack> removals = new ArrayList<>();
      for (ItemStack drop : drops)
      {
        if (NBTAPI.isRestricted(player, drop, RestrictionType.NO_TRADE))
        {
          removals.add(drop);
        }
      }
      drops.removeAll(removals);
    }

    List<CustomEffect> customEffects = CustomEffectManager.getEffects(player);
    customEffects.removeIf(customEffect -> !customEffect.isKeepOnDeath());

    if (UserData.IMMEDIATE_RESPAWN.getBoolean(player))
    {
      Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> player.spigot().respawn(), 0L);
    }
  }
}
