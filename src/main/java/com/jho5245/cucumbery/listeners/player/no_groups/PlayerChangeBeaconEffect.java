package com.jho5245.cucumbery.listeners.player.no_groups;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.blockplacedata.BlockPlaceDataConfig;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.no_groups.SoundPlay;
import de.tr7zw.changeme.nbtapi.NBTList;
import io.papermc.paper.event.player.PlayerChangeBeaconEffectEvent;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PlayerChangeBeaconEffect implements Listener
{
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerChangeBeaconEffect(PlayerChangeBeaconEffectEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }

    Player player = event.getPlayer();
    UUID uuid = player.getUniqueId();
    InventoryView inventoryView = player.getOpenInventory();

    if (inventoryView.getType() == InventoryType.BEACON)
    {
      ItemStack item = inventoryView.getTopInventory().getItem(0);
      // 신호기 사용 불가 아이템 제한
      if (NBTAPI.isRestricted(player, item, Constant.RestrictionType.NO_BEACON))
      {
        event.setCancelled(true);
        if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.playerChangeBeaconEffectAlertCooldown.contains(uuid))
        {
          Variable.playerChangeBeaconEffectAlertCooldown.add(uuid);
          MessageUtil.sendTitle(player, "&c사용 불가!", "&r신호기에 사용할 수 없는 아이템입니다", 5, 80, 15);
          SoundPlay.playSound(player, Constant.ERROR_SOUND);
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerChangeBeaconEffectAlertCooldown.remove(uuid), 100L);
        }
        if (player.getInventory().firstEmpty() != -1)
        {
          Variable.playerChangeBeaconEffectItemDropCooldown.add(uuid);
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerChangeBeaconEffectItemDropCooldown.remove(uuid), 0L);
        }
        return;
      }

      // 무한 신호기 사용 모드 아이템 (신호기에 아이템을 사용해도 사라지지 않음)
      NBTList<String> extraTags = NBTAPI.getStringList(NBTAPI.getMainCompound(item), CucumberyTag.EXTRA_TAGS_KEY);
      if (NBTAPI.arrayContainsValue(extraTags, Constant.ExtraTag.INFINITE_BEACON_ITEM))
      {
        event.setConsumeItem(false);
        if (player.getInventory().firstEmpty() != -1)
        {
          Variable.playerChangeBeaconEffectItemDropCooldown.add(uuid);
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerChangeBeaconEffectItemDropCooldown.remove(uuid), 0L);
        }
      }

      Block block = event.getBeacon();
      if (block != null)
      {
        ItemStack placedBlockDataItemStack = BlockPlaceDataConfig.getItem(block.getLocation());
        if (NBTAPI.isRestricted(player, placedBlockDataItemStack, Constant.RestrictionType.NO_BLOCK_BEACON))
        {
          event.setCancelled(true);
          if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.playerChangeBeaconEffectAlertCooldown.contains(uuid))
          {
            Variable.playerChangeBeaconEffectAlertCooldown.add(uuid);
            MessageUtil.sendTitle(player, "&c사용 불가!", "&r효과를 적용하거나 바꿀 수 없는 신호기입니다", 5, 80, 15);
            SoundPlay.playSound(player, Constant.ERROR_SOUND);
            Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerChangeBeaconEffectAlertCooldown.remove(uuid), 100L);
          }
          if (player.getInventory().firstEmpty() != -1)
          {
            Variable.playerChangeBeaconEffectItemDropCooldown.add(uuid);
            Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerChangeBeaconEffectItemDropCooldown.remove(uuid), 0L);
          }
//          return;
        }
      }
    }
  }
}
