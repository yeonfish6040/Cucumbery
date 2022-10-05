package com.jho5245.cucumbery.listeners.player.no_groups;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.no_groups.Method2;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
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
    this.sansArmor(event);
  }

  private void sansArmor(PlayerArmorChangeEvent event)
  {
    Player player = event.getPlayer();
    Bukkit.getScheduler().runTaskLaterAsynchronously(Cucumbery.getPlugin(), () ->
    {
      PlayerInventory playerInventory = player.getInventory();
      ItemStack helmet = playerInventory.getHelmet(), chestplate = playerInventory.getChestplate(), leggings = playerInventory.getLeggings(), boots = playerInventory.getBoots();
      if (CustomMaterial.itemStackOf(helmet) == CustomMaterial.SANS_HELMET &&
              CustomMaterial.itemStackOf(chestplate) == CustomMaterial.SANS_CHESTPLATE &&
              CustomMaterial.itemStackOf(leggings) == CustomMaterial.SANS_LEGGINGS &&
              CustomMaterial.itemStackOf(boots) == CustomMaterial.SANS_BOOTS)
      {
        Method2.getNearbyEntitiesAsync(player, 50d).forEach(e -> {
          if (e instanceof AbstractSkeleton abstractSkeleton && abstractSkeleton.getTarget() == player)
          {
            abstractSkeleton.setTarget(null);
          }
        });
      }
    }, 0L);
  }
}
