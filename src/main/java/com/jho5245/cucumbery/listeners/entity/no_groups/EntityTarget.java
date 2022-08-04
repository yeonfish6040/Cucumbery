package com.jho5245.cucumbery.listeners.entity.no_groups;

import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectType;
import com.jho5245.cucumbery.custom.customeffect.children.group.PlayerCustomEffect;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class EntityTarget implements Listener
{
  @EventHandler
  public void onEntityTarget(EntityTargetEvent event)
  {
    Entity entity = event.getEntity(); // 쫓아오는 개체
    if (entity.getScoreboardTags().contains("cucumbery_no_aggro"))
    {
      event.setTarget(null);
      event.setCancelled(true);
      return;
    }
    Entity target = event.getTarget(); // 쫓기는 개체
    TargetReason reason = event.getReason();
    if (target instanceof Player player)
    {
      if (UserData.SPECTATOR_MODE.getBoolean(player))
      {
        event.setCancelled(true);
        return;
      }
    }
    if (reason != TargetReason.TEMPT && !(entity instanceof ExperienceOrb) && target instanceof Player player)
    {
      GameMode gamemode = player.getGameMode();
      if (gamemode != GameMode.SURVIVAL && gamemode != GameMode.ADVENTURE)
      {
        return;
      }
      if (!UserData.ENTITY_AGGRO.getBoolean(player.getUniqueId()) || CustomEffectManager.hasEffect(player, CustomEffectType.NO_ENTITY_AGGRO))
      {
        event.setTarget(null);
        event.setCancelled(true);
        return;
      }
    }
    if (reason == TargetReason.CLOSEST_PLAYER && entity instanceof ExperienceOrb experienceOrb && target instanceof Player player)
    {
      if (CustomEffectManager.hasEffect(experienceOrb, CustomEffectType.COMBO_EXPERIENCE))
      {
        CustomEffect customEffect = CustomEffectManager.getEffect(experienceOrb, CustomEffectType.COMBO_EXPERIENCE);
        if (customEffect instanceof PlayerCustomEffect playerCustomEffect)
        {
          Player p = playerCustomEffect.getPlayer();
          if (!(entity.getUniqueId().equals(p.getUniqueId())))
          {
            Location location = entity.getLocation();
            entity.teleport(location);
            event.setTarget(p);
            entity.teleport(location);
          }
        }
      }
      String pickUpMode = UserData.ITEM_PICKUP_MODE.getString(player);
      switch (pickUpMode)
      {
        case "sneak" -> {
          if (!player.isSneaking())
          {
            event.setTarget(null);
            event.setCancelled(true);
            entity.teleport(entity);
            return;
          }
        }
        case "disabled" -> {
          event.setTarget(null);
          event.setCancelled(true);
          entity.teleport(entity);
          return;
        }
      }
    }
    if (event.isCancelled())
    {
      return;
    }
    this.sansArmor(event);
  }

  private void sansArmor(EntityTargetEvent event)
  {
    Entity entity = event.getEntity(); // 쫓아오는 개체
    Entity target = event.getTarget(); // 쫓기는 개체
    if (target instanceof Player player && entity instanceof AbstractSkeleton)
    {
      PlayerInventory playerInventory = player.getInventory();
      ItemStack helmet = playerInventory.getHelmet(), chestplate = playerInventory.getChestplate(), leggings = playerInventory.getLeggings(), boots = playerInventory.getBoots();
      if (ItemStackUtil.itemExists(helmet) && CustomMaterial.SANS_HELMET.toString().toLowerCase().equals(new NBTItem(helmet).getString("id")) &&
              ItemStackUtil.itemExists(chestplate) && CustomMaterial.SANS_CHESTPLATE.toString().toLowerCase().equals(new NBTItem(chestplate).getString("id")) &&
              ItemStackUtil.itemExists(leggings) && CustomMaterial.SANS_LEGGINGS.toString().toLowerCase().equals(new NBTItem(leggings).getString("id")) &&
              ItemStackUtil.itemExists(boots) && CustomMaterial.SANS_BOOTS.toString().toLowerCase().equals(new NBTItem(boots).getString("id")))
      {
        event.setCancelled(true);
      }
    }
  }
}
