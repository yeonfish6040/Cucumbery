package com.jho5245.cucumbery.listeners.entity.no_groups;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.no_groups.SoundPlay;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Variable;
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class EntityLoadCrossbow implements Listener
{
  @EventHandler
  public void onEntityLoadCrossbow(EntityLoadCrossbowEvent event)
  {
    LivingEntity livingEntity = event.getEntity();
    if (livingEntity instanceof Player player)
    {
      UUID uuid = player.getUniqueId();
      ItemStack item = event.getCrossbow();
      if (NBTAPI.isRestricted(player, item, Constant.RestrictionType.NO_CROSSBOW_LOAD))
      {
        event.setCancelled(true);
        player.updateInventory();
        if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.playerLoadCrossbowAlertCooldown.contains(uuid))
        {
          Variable.playerLoadCrossbowAlertCooldown.add(uuid);
          MessageUtil.sendTitle(player, "&c장전 불가!", "&r장전할 수 없는 쇠뇌입니다", 5, 80, 15);
          SoundPlay.playSound(player, Constant.ERROR_SOUND);
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerLoadCrossbowAlertCooldown.remove(uuid), 100L);
        }
        return;
      }
      if (Method.usingLoreFeature(player))
      {
        if (ItemStackUtil.itemExists(item))
        {
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> ItemLore.setItemLore(item), 0L);
        }
      }
    }
  }
}
