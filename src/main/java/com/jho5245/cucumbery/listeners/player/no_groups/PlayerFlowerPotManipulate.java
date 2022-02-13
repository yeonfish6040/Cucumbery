package com.jho5245.cucumbery.listeners.player.no_groups;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.no_groups.SoundPlay;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Constant.RestrictionType;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Variable;
import io.papermc.paper.event.player.PlayerFlowerPotManipulateEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.UUID;

public class PlayerFlowerPotManipulate implements Listener
{
  @EventHandler
  public void onPlayerFlowerPotManipulate(PlayerFlowerPotManipulateEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    Player player = event.getPlayer();
    UUID uuid = player.getUniqueId();
    ItemStack itemStack = event.getItem();
    if (event.isPlacing() && NBTAPI.isRestricted(player, itemStack, RestrictionType.NO_PLACE))
    {
      event.setCancelled(true);
      if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.blockPlaceAlertCooldown2.contains(uuid))
      {
        Variable.blockPlaceAlertCooldown2.add(uuid);
        MessageUtil.sendTitle(player, "&c설치 불가!", "&r설치할 수 없는 블록입니다", 5, 80, 15);
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.blockPlaceAlertCooldown2.remove(uuid), 100L);
      }
      return;
    }
    if (!event.isPlacing() && Method.usingLoreFeature(player))
    {
      PlayerInventory inventory = player.getInventory();
      MessageUtil.sendMessage(player, event.isPlacing() + "", itemStack);
      Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
      {
        ItemLore.setItemLore(inventory.getItemInMainHand(), player);
        ItemLore.setItemLore(inventory.getItemInOffHand(), player);
      }, 0L);
    }
  }
}
