package com.jho5245.cucumbery.listeners.player.bucket;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectTypeCustomMining;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Constant.RestrictionType;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.no_groups.SoundPlay;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PlayerBucketEmpty implements Listener
{
  @EventHandler
  public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    Player player = event.getPlayer();
    // 커스텀 채광 모드에서는 불가능하게 막음
    if (player.getGameMode() != GameMode.CREATIVE && CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE) && !CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_2_NO_RESTORE))
    {
      event.setCancelled(true);
      return;
    }
    UUID uuid = player.getUniqueId();
    ItemStack original = player.getInventory().getItem(event.getHand());
    if (ItemStackUtil.itemExists(original))
    {
      if (NBTAPI.isRestricted(player, original, RestrictionType.NO_BUCKET))
      {
        event.setCancelled(true);
        if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.playerBucketUseAlertCooldown.contains(uuid))
        {
          Variable.playerBucketUseAlertCooldown.add(uuid);
          MessageUtil.sendTitle(player, "&c사용 불가!", "&r사용할 수 없는 양동이입니다", 5, 80, 15);
          SoundPlay.playSound(player, Constant.ERROR_SOUND);
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerBucketUseAlertCooldown.remove(uuid), 100L);
        }
        return;
      }
      if (player.getGameMode() != GameMode.CREATIVE && NBTAPI.arrayContainsValue(NBTAPI.getStringList(NBTAPI.getMainCompound(original), CucumberyTag.EXTRA_TAGS_KEY),
              Constant.ExtraTag.INFINITE.toString()))
      {
        Variable.playerItemConsumeCauseSwapCooldown.add(uuid);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerItemConsumeCauseSwapCooldown.remove(uuid), 0L);
        ItemStack finalItem = original.clone();
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> player.getInventory().setItem(event.getHand(), finalItem), 0L);
      }
      boolean usefulLore = Method.usingLoreFeature(player);
      if (usefulLore)
      {
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
          {
            ItemStack main = player.getInventory().getItemInMainHand(), off = player.getInventory().getItemInOffHand();
            ItemLore.setItemLore(main);
            ItemLore.setItemLore(off);
          }, 0L);
      }
    }
  }
}
