package com.jho5245.cucumbery.listeners.player.no_groups;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.no_groups.SoundPlay;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Variable;
import io.papermc.paper.event.player.PlayerLecternPageChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PlayerLecternPageChange implements Listener
{
  @EventHandler
  public void onPlayerLecternPageChange(PlayerLecternPageChangeEvent event)
  {
    Player player = event.getPlayer();
    UUID uuid = player.getUniqueId();
    ItemStack item = event.getBook();
    if (NBTAPI.isRestricted(player, item, Constant.RestrictionType.NO_LECTERN_CHANGE_PAGE))
    {
      event.setCancelled(true);
      if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.playerLecternChangePageAlertCooldown.contains(uuid))
      {
        Variable.playerLecternChangePageAlertCooldown.add(uuid);
        MessageUtil.sendTitle(player, "&c열람 불가!", "&r쪽수를 바꿀 수 없는 책입니다", 5, 80, 15);
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerLecternChangePageAlertCooldown.remove(uuid), 100L);
      }
    }
  }
}
