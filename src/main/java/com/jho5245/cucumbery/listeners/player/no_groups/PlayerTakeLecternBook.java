package com.jho5245.cucumbery.listeners.player.no_groups;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.no_groups.SoundPlay;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Variable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTakeLecternBookEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PlayerTakeLecternBook implements Listener
{
  @EventHandler
  public void onPlayerTakeLecternBook(PlayerTakeLecternBookEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    Player player = event.getPlayer();
    UUID uuid = player.getUniqueId();
    ItemStack item = event.getBook();
    if (NBTAPI.isRestricted(player, item, Constant.RestrictionType.NO_LECTERN_BOOK_TAKE))
    {
      event.setCancelled(true);
      if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.playerTakeLecternBookAlertCooldown.contains(uuid))
      {
        Variable.playerTakeLecternBookAlertCooldown.add(uuid);
        MessageUtil.sendTitle(player, "&c회수 불가!", "&r가져갈 수 없는 책입니다", 5, 80, 15);
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerTakeLecternBookAlertCooldown.remove(uuid), 100L);
      }
    }
    if (ItemStackUtil.itemExists(item) && ItemStackUtil.countSpace(player.getInventory(), item) < 1)
    {
      event.setCancelled(true);
      if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.playerTakeLecternBookAlertCooldown.contains(uuid))
      {
        Variable.playerTakeLecternBookAlertCooldown.add(uuid);
        MessageUtil.sendTitle(player, "&c회수 불가!", "&r인벤토리가 가득 찬 상태에서는 책을 가져갈 수 없습니다", 5, 80, 15);
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerTakeLecternBookAlertCooldown.remove(uuid), 100L);
      }
    }
    Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
    {
      if (item != null)
      {
        ItemLore.setItemLore(item);
      }
    }, 0L);
  }
}
