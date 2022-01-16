package com.jho5245.cucumbery.listeners.player.bucket;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.SoundPlay;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Variable;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PlayerBucketEntity implements Listener
{
  @EventHandler
  public void onPlayerBucketEntity(PlayerBucketEntityEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    Player player = event.getPlayer();
    UUID uuid = player.getUniqueId();
    ItemStack item = event.getOriginalBucket();
    if (NBTAPI.isRestricted(player, item, Constant.RestrictionType.NO_BUCKET))
    {
      event.setCancelled(true);
      player.updateInventory();
      if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.playerBucketUseAlertCooldown.contains(uuid))
      {
        Variable.playerBucketUseAlertCooldown.add(uuid);
        MessageUtil.sendTitle(player, "&c사용 불가!", "&r사용할 수 없는 양동이입니다", 5, 80, 15);
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerBucketUseAlertCooldown.remove(uuid), 100L);
      }
      return;
    }
    if (player.getGameMode() != GameMode.CREATIVE && NBTAPI.arrayContainsValue(NBTAPI.getStringList(NBTAPI.getMainCompound(item), CucumberyTag.EXTRA_TAGS_KEY),
            Constant.ExtraTag.INFINITE.toString()))
    {
        event.setCancelled(true);
        return;
    }
    ItemStack entityBucket = event.getEntityBucket();
    ItemLore.setItemLore(entityBucket);
  }
}
