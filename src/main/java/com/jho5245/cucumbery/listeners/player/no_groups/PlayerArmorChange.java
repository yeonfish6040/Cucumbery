package com.jho5245.cucumbery.listeners.player.no_groups;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.AbstractSkeleton;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerArmorChange implements Listener
{
  @EventHandler
  public void onPLayerArmorChange(PlayerArmorChangeEvent event)
  {
    Player player = event.getPlayer();
    ItemStack oldItem = event.getOldItem(), newItem = event.getNewItem();
    if (ItemStackUtil.itemExists(oldItem) && new NBTItem(oldItem).hasKey("id") || ItemStackUtil.itemExists(newItem) && new NBTItem(newItem).hasKey("id"))
    {
      Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Method.updateInventory(player), 0L);
    }
    this.sansArmor(event);
  }

  private void sansArmor(PlayerArmorChangeEvent event)
  {
    Player player = event.getPlayer();
    Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
    {
      PlayerInventory playerInventory = player.getInventory();
      ItemStack helmet = playerInventory.getHelmet(), chestplate = playerInventory.getChestplate(), leggings = playerInventory.getLeggings(), boots = playerInventory.getBoots();
      if (ItemStackUtil.itemExists(helmet) && CustomMaterial.SANS_HELMET.toString().toLowerCase().equals(new NBTItem(helmet).getString("id")) &&
              ItemStackUtil.itemExists(chestplate) && CustomMaterial.SANS_CHESTPLATE.toString().toLowerCase().equals(new NBTItem(chestplate).getString("id")) &&
              ItemStackUtil.itemExists(leggings) && CustomMaterial.SANS_LEGGINGS.toString().toLowerCase().equals(new NBTItem(leggings).getString("id")) &&
              ItemStackUtil.itemExists(boots) && CustomMaterial.SANS_BOOTS.toString().toLowerCase().equals(new NBTItem(boots).getString("id")))
      {
        player.getNearbyEntities(50, 50, 50).forEach(e -> {
          if (e instanceof AbstractSkeleton abstractSkeleton && abstractSkeleton.getTarget() == player)
          {
            abstractSkeleton.setTarget(null);
          }
        });
      }
    }, 0L);
  }
}
